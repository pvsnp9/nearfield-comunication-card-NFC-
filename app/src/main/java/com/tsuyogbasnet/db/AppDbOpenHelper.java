package com.tsuyogbasnet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tsuyogbasnet on 1/04/15.
 */
public class AppDbOpenHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "SQLITEDB";

    private static final String DATABASE_NAME = "tours.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TOURS = "tours";
    public static final String COLUMN_ID = "tourId";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TOURS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESC + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_PRICE + " NUMERIC " +
                    ")";


    //String name, SQLiteDatabase.CursorFactory factory, int version :: THESE ARE taken out from parameter because
    //these are defined inside this class as a CONST
    public AppDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //executing command to create a table in app, receiving db instance.
        db.execSQL(TABLE_CREATE);
        Log.i(LOGTAG, "Table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TOURS);
        onCreate(db);
    }
}
