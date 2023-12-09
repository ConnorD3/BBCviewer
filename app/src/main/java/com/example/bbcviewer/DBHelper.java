package com.example.bbcviewer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {//database helper used to create and fill database with favourited articles
    protected static final String DB = "favesDB";
    protected static final int VERSION = 1;
    public static final String TABLE = "mycourses";
    public static final String ID = "id";
    public static final String TITLE = "titles";
    public static final String DESC = "description";
    public static final String URL = "URL";

    public DBHelper(Context context) {
        super(context, DB, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT,"
                + DESC + " TEXT,"
                + URL + " TEXT)";

        db.execSQL(query);
    }

    public void addFave(String fTitle, String fDesc, String fUrl){//call directly to add input data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues vals = new ContentValues();

        vals.put(TITLE, fTitle);
        vals.put(DESC, fDesc);
        vals.put(URL, fUrl);

        db.insert(TABLE, null, vals);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
    }

    public Cursor popList(){//creates cursor to hold data that will populate fields in other activities
       SQLiteDatabase db = this.getReadableDatabase();
        String cols[] = {DBHelper.ID,DBHelper.TITLE,DBHelper.DESC,DBHelper.URL};
        Cursor curs = db.query(DBHelper.TABLE,cols,null,null,null,null,DBHelper.ID);
        return curs;
    }



}
