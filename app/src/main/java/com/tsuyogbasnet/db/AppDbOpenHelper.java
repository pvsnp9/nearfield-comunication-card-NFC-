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


    public static final String TABLE_ARC = "tours";
    public static final String COLUMN_ID = "dataId";
    public static final String COLUMN_TUTOR_ID = "tutorId";
    public static final String COLUMN_FIRST_NAME = "tutorFirstName";
    public static final String COLUMN_LAST_NAME = "tutorLastName";
    public static final String COLUMN_TUTOR_CELL = "tutorCell";
    public static final String COLUMN_TUTOR_EMAIL= "tutorEmail";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ARC + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TUTOR_ID + " TEXT, " +
                    COLUMN_FIRST_NAME + " TEXT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_TUTOR_CELL + " TEXT " +
                    COLUMN_TUTOR_EMAIL +"TEXT" +
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
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ARC);
        onCreate(db);
    }
}
