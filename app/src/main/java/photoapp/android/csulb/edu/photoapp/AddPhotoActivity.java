package photoapp.android.csulb.edu.photoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.ImageData;

public class AddPhotoActivity extends AppCompatActivity implements View.OnClickListener{

    Button mClickPhotoButton;
    Button mSavePhotoButton;
    ImageView mimageView;
    EditText mCaptionText;

    String mCurrentPhotoPath;
    String mCurrentPhotoCaption;
    Bitmap mImageBitMap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 11;

    public void PhotoViewActivity()
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        mClickPhotoButton = (Button)findViewById(R.id.clickPhotoButton);
        mClickPhotoButton.setOnClickListener(this);
        mSavePhotoButton =(Button)findViewById(R.id.savePhotoButton);
        mSavePhotoButton.setOnClickListener(this);
        mCaptionText =(EditText) findViewById(R.id.caption);
        mimageView =(ImageView)findViewById(R.id.imageClickView);



    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.getExternalStorageDirectory() + "/DCIM");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            File file = new File(mCurrentPhotoPath);
            Uri uri = Uri.fromFile(file);
            Bitmap bm;
            try
            {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                mImageBitMap =bm;
                mimageView.setImageBitmap(mImageBitMap);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            mimageView.setImageBitmap(mImageBitMap);
            mCaptionText.setEnabled(true);
            mSavePhotoButton.setEnabled(true);
            mCaptionText.requestFocus();
        }
    }

    private boolean checkStoragePermission()
    {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

    }
    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id)
        {
            case R.id.clickPhotoButton:
            {
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile =null;
                    try
                    {

                        photoFile = createImageFile();
                    }
                    catch (IOException ex)
                    {
                        String s = ex.toString();
                    }

                    if(photoFile!=null)
                    {
                        Uri photoURI = FileProvider.getUriForFile(getBaseContext(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
                    }

                }
                break;

            }
            case R.id.savePhotoButton:
            {

                if(mCaptionText.getText().length()!=0)
                {

                    if(checkStoragePermission())
                    {
                          writeData();
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_EXTERNAL_STORAGE);
                    }
                }
                else
                {
                    Toast.makeText(this,"Please write a caption for the photo",Toast.LENGTH_SHORT).show();
                }

                break;

            }

        }
    }

    private void writeData()
    {
        Bitmap bitmap = mImageBitMap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(createImageFile());
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Cant write to storage", Toast.LENGTH_SHORT).show();
        }

        PhotoAppDBHelper db = new PhotoAppDBHelper(this);
        db.insertRecord(new ImageData(mCurrentPhotoPath,mCaptionText.getText().toString()));
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    writeData();
                } else {
                    Toast.makeText(this, "Permission denied for external storage", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
        }
    }


}
