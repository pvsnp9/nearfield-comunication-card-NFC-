package com.tsuyogbasnet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import com.tsuyogbasnet.models.Attendance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsuyogbasnet on 1/04/15.
 */
public class AppDataSource {

    public static final String LOGTAG = "SQLITEDB";
    public static final String LOGCAT = "DEBUGTOOL";
    //creating private array of string to retrieve from attendance table
    private static final String[] attendanceCol = {
            AppDbOpenHelper.COLUMN_ID,
            AppDbOpenHelper.COLUMN_STUDENT_ID,
            AppDbOpenHelper.COLUMN_TUTOR_ID,
            AppDbOpenHelper.COLUMN_PROGRAMME_CODE,
            AppDbOpenHelper.COLUMN_SUBJECT_CODE,
            AppDbOpenHelper.COLUMN_ROOM_CODE,
            AppDbOpenHelper.COLUMN_DATE,
            AppDbOpenHelper.COLUMN_TYPE
    };
    private static final String[] attendanceStudentId = {
            AppDbOpenHelper.COLUMN_STUDENT_ID
    };
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

        Long insertId = database.insert(AppDbOpenHelper.TABLE_ARC,null,values);
        attendance.setAttendanceId(insertId);

        return attendance;
    }
    // method that returns all of the attendance from attendance table.
    public List<Attendance> findAllAttendance(){
        List<Attendance> attendances = new ArrayList<Attendance>();
        Cursor cursor = database.query(AppDbOpenHelper.TABLE_ARC,attendanceCol,null,null,null,null,null );
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                Attendance attendance = new Attendance();
                attendance.setAttendanceId(cursor.getLong(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_ID)));
                attendance.setStudentId(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_STUDENT_ID)));
                attendance.setTutorId(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_TUTOR_ID)));
                attendance.setProgrammeCode(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_PROGRAMME_CODE)));
                attendance.setSubjectCode(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_SUBJECT_CODE)));
                attendance.setRoomCode(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_ROOM_CODE)));
                attendance.setDate(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_DATE)));
                attendance.setType(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_TYPE)));
                attendances.add(attendance);
            }
        }
        return attendances;
    }
    //retrieves only ID from attendance table
    public List<Attendance> findAllIds(){
        List<Attendance> attendances = new ArrayList<Attendance>();
        Cursor cursor = database.query(AppDbOpenHelper.TABLE_ARC,attendanceStudentId,null,null,null,null,null);
        Log.i(LOGCAT,"Returned"+cursor.getCount() +"rows");
        if (cursor.getCount()>0){
            while(cursor.moveToNext()){
                Attendance attendance = new Attendance();
                attendance.setStudentId(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_STUDENT_ID)));
                attendances.add(attendance);
            }
        }
        return attendances;
    }
    public boolean endAndUpload (){
        int result = database.delete(AppDbOpenHelper.TABLE_ARC,null,null);
        return (result >= 1);
    }


}
