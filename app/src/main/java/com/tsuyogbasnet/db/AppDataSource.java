package com.tsuyogbasnet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tsuyogbasnet on 1/04/15.
 */
public class AppDataSource {

    public static final String LOGTAG = "SQLITEDB";
    public static final String LOGCAT = "DEBUGTOOL";
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

    public String[] allColumns = {
            AppDbOpenHelper.COLUMN_ID,
            AppDbOpenHelper.COLUMN_TUTOR_ID,
            AppDbOpenHelper.COLUMN_FIRST_NAME,
            AppDbOpenHelper.COLUMN_LAST_NAME,
            AppDbOpenHelper.COLUMN_TUTOR_CELL,
            AppDbOpenHelper.COLUMN_TUTOR_EMAIL
    };

    public AppDataSource(Context context){
        dbHelper = new AppDbOpenHelper(context);
    }
    public void open(){
        Log.i(LOGTAG,"Database is now Open");
        database = dbHelper.getWritableDatabase();
    }
    public void close(){
        Log.i(LOGTAG,"Database is now Closed");
        dbHelper.close();

    }


}
