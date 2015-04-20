package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



import java.util.ArrayList;
import java.util.List;

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
            AppDbOpenHelper.COLUMN_TITLE,
            AppDbOpenHelper.COLUMN_DESC,
            AppDbOpenHelper.COLUMN_PRICE,
            AppDbOpenHelper.COLUMN_IMAGE
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
