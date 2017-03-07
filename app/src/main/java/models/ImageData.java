package models;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Neha on 3/4/2017.
 */

public class ImageData {

    private String mCaption;
    private String mImagePath;
    private String mName;
    private Bitmap mImageBitmap;

    public ImageData(String path,String caption)
    {
        mCaption = caption;
        mImagePath=path;

    }

    public ImageData()
    {

    }
    public String getCaption()
    {
        return mCaption;
    }

    public void setCaption(String caption)
    {
        this.mCaption = caption;
    }
    public String getImagepath()
    {
        return mImagePath;
    }

    public void setImagePath(String pathString)
    {
        this.mImagePath = pathString;
    }

    public String getName()
    {
        return this.mName;
    }

    public String setName(String name)
    {
        return this.mName;
    }

    public Bitmap getImage()
    {
        return this.mImageBitmap;
    }

    public void setImage(Bitmap bm)
    {
        this.mImageBitmap = bm;
    }
    public static ArrayList<ImageData> createImageList()
    {
        ArrayList<ImageData> data = new ArrayList<ImageData>();

        try
        {

        }
        catch (Exception ex)
        {
            String s = ex.getMessage();

        }

        return data;
    }

}
