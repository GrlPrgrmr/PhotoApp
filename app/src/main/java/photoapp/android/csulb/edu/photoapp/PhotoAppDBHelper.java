package photoapp.android.csulb.edu.photoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import models.ImageData;

/**
 * Created by Neha on 3/1/2017.
 */

public class PhotoAppDBHelper extends SQLiteOpenHelper {

    private static final String ID_COLUMN = "_id";
    private static final String FILE_PATH_COLUMN = "filepath";
    private static final String CAPTION_COLUMN = "caption";


    private static final String DATABASE_TABLE_NAME = "PhotoText";
    private static final String DATABASE_NAME = "photoApp.db";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE =
            String.format("CREATE TABLE %s (" +"  %s integer primary key autoincrement, "
                    +"  %s text," +"  %s text )", DATABASE_TABLE_NAME, ID_COLUMN,FILE_PATH_COLUMN,CAPTION_COLUMN);


    public PhotoAppDBHelper(Context context)
    {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        onCreate(db);
    }

    public void deleteAll()
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("Delete from "+DATABASE_TABLE_NAME);
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DATABASE_TABLE_NAME + "'");
        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
        }
    }
    public Cursor readDBFull(String query)
    {

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor result = db.rawQuery(query, null);
            return  result;
        }
        catch (Exception ex)
        {
           String error = ex.getMessage();
            return null;
        }

    }

    public int getRowCountTable()
    {
        int count=0;
        try{

            SQLiteDatabase db = this.getReadableDatabase();

            count = (int)DatabaseUtils.queryNumEntries(db,DATABASE_TABLE_NAME);

            db.close();

        }
        catch (Exception ex)
        {
            String err =ex.getMessage();
        }

      return count;
    }

    public boolean insertRecord(ImageData img)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cValues = new ContentValues();
            cValues.put(FILE_PATH_COLUMN, img.getImagepath());
            cValues.put(CAPTION_COLUMN, img.getCaption());

            db.insert(DATABASE_TABLE_NAME, null, cValues);
            return true;
        }
        catch (Exception ex)
        {
            String message = ex.getMessage();
            return false;
        }
    }

    public ImageData getRow(int position) {
        System.out.println("db get row position = " + position);
        SQLiteDatabase db = this.getReadableDatabase();
        ImageData photo = null;

        Cursor cursor = db.query(DATABASE_TABLE_NAME, new String[]{ "filepath","caption"},
                "_id = ?", new String[]{String.valueOf(position+1)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex("filepath"));
            String caption = cursor.getString(cursor.getColumnIndex("caption"));

            photo = new ImageData(path,caption );
            cursor.close();
        }

        db.close();
        return photo;
    }


    // get all entries in the table
    public List<ImageData> readDBFull() {
        List<ImageData> photos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String _id ="" ;
        if (cursor.moveToFirst()) {
            do {
                ImageData photo = new ImageData();
                photo.setCaption(cursor.getString(2));
                photo.setImagePath(cursor.getString(1));
                _id = cursor.getString(0);
                photos.add(photo);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return photos;
    }

}
