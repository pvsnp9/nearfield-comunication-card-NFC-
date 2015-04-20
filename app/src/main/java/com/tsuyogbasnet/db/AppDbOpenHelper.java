package com.tsuyogbasnet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.PrivateKey;

/**
 * Created by tsuyogbasnet on 1/04/15.
 */
public class AppDbOpenHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "SQLITEDB";

    private static final String DATABASE_NAME = "arc.db";
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
    private static final String CREATE_TUTOR_TABLE =
            "CREATE TABLE" +"TUTOR" + "(" + "tutorId"+ "TEXT PRIMARY KEY," +
                    "firstName"+ "TEXT,"+
                    "LastName"+"TEXT,"+
                    "cellNumber"+"TEXT,"+
                    "email"+"TEXT,";
    private static final String CREATE="";//write SQl command her to create table.


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
