package com.tsuyogbasnet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tsuyogbasnet.models.Attendance;

/**
 * Created by tsuyogbasnet on 1/04/15.
 */
public class AppDataSource {

    public static final String LOGTAG = "SQLITEDB";
    public static final String LOGCAT = "DEBUGTOOL";
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

//    public String[] allColumns = {
//            AppDbOpenHelper.COLUMN_ID,
//            AppDbOpenHelper.COLUMN_TUTOR_ID,
//            AppDbOpenHelper.COLUMN_FIRST_NAME,
//            AppDbOpenHelper.COLUMN_LAST_NAME,
//            AppDbOpenHelper.COLUMN_TUTOR_CELL,
//            AppDbOpenHelper.COLUMN_TUTOR_EMAIL
//    };

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
    public Attendance create (Attendance attendance){
        ContentValues values = new ContentValues();
        values.put(AppDbOpenHelper.COLUMN_STUDENT_ID, attendance.getStudentId());
        values.put(AppDbOpenHelper.COLUMN_TUTOR_ID, attendance.getTutorId());
        values.put(AppDbOpenHelper.COLUMN_PROGRAMME_CODE, attendance.getProgrammeCode());
        values.put(AppDbOpenHelper.COLUMN_SUBJECT_CODE, attendance.getSubjectCode());
        values.put(AppDbOpenHelper.COLUMN_ROOM_CODE, attendance.getRoomCode());
        values.put(AppDbOpenHelper.COLUMN_DATE, attendance.getDate());
        values.put(AppDbOpenHelper.COLUMN_TYPE, attendance.getType());

        Long insertId = database.insertOrThrow(AppDbOpenHelper.TABLE_ARC,null,values);
        attendance.setAttendanceId(insertId);

        return attendance;
    }


}
