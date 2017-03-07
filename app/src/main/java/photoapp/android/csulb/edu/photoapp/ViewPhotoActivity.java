package photoapp.android.csulb.edu.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import models.ImageData;

/**
 * Created by Neha on 3/6/2017.
 */

public class ViewPhotoActivity extends AppCompatActivity {

   public static final String POSITIONVALUE ="position";
    private TextView captionTextView;
    private ImageView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_item);

        captionTextView =(TextView)findViewById(R.id.captionItem);
        photoView = (ImageView)findViewById(R.id.imageViewItem);

        Intent intent = getIntent();
        int posVal = intent.getIntExtra(POSITIONVALUE,-1);

        System.out.println("Photo position :"+posVal);

        if(posVal==-1)
        {
            Toast.makeText(this,"Error inside PhotoViewActivity",Toast.LENGTH_SHORT).show();
            finish();

        }
        else
        {
            PhotoAppDBHelper db = new PhotoAppDBHelper(this);
            ImageData imgData = db.getRow(posVal);

            captionTextView.setText(imgData.getCaption());

            String path = imgData.getImagepath();
            System.out.println("Path : " + path);

            Bitmap bmap = BitmapFactory.decodeFile(path);
            photoView.setImageBitmap(bmap);
        }


    }
}
