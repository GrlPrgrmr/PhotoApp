package photoapp.android.csulb.edu.photoapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import models.ImageData;

public class List_PhotoActivity extends AppCompatActivity {

    private PhotoAppDBHelper db;
    private OnItemSelectedListViewListener mListener;

    private RecyclerView recyclerView;
    private FloatingActionButton floatButton;
    private List<ImageData> imageList= new ArrayList<>();
    private ImageDataAdapter adapter;
    public static final String POSITIONVALUE ="position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__photo);

        recyclerView = (RecyclerView)findViewById(R.id.rvlistItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        floatButton =(FloatingActionButton)findViewById(R.id.floatbutton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List_PhotoActivity.this,AddPhotoActivity.class);

                startActivity(intent);
            }
        });
        //adapter = new ImageDataAdapter(this,getListFromDataBase());
        //recyclerView.setAdapter(adapter);


    }


    private List<ImageData> getListFromDataBase()
    {
        db = new PhotoAppDBHelper(this);

        try
        {

            //db.deleteAll();

            if(db.getRowCountTable()>0)
            {
                imageList = db.readDBFull();


            }
            /*else
            {
                Intent intent = new Intent(this,AddPhotoActivity.class);
                startActivity(intent);
            }*/



        }
        catch (Exception ex)
        {
            String error = ex.getMessage();
        }

        return imageList;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        adapter = new ImageDataAdapter(this,getListFromDataBase());
        adapter.setOnItemSelectedListener(new OnItemSelectedListViewListener() {
            @Override
            public void onImageItemSelected(int pos) {

                System.out.println(pos);

                Intent intent = new Intent(List_PhotoActivity.this,ViewPhotoActivity.class);
                intent.putExtra(POSITIONVALUE,pos);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
    }

}
