package com.tsuyogbasnet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tsuyogbasnet.models.Attendance;

import java.security.PrivateKey;
import java.util.Date;

/**
 * Created by tsuyogbasnet on 1/04/15.
 */
public class AppDbOpenHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "SQLITEDB";

    private static final String DATABASE_NAME = "arc.db";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE_ARC = "attendance";
    public static final String COLUMN_ID = "attendanceId";
    public static final String COLUMN_STUDENT_ID = "studentId";
    public static final String COLUMN_TUTOR_ID = "tutorId";
    public static final String COLUMN_PROGRAMME_CODE = "programmeCode";
    public static final String COLUMN_SUBJECT_CODE = "subjectCode";
    public static final String COLUMN_ROOM_CODE= "roomCode";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TYPE="type";

    //creating tutor map table
    public static final String TABLE_MAP_TUTOR ="maptutor";
    public static final String COLUMN_MAP_TUTOR_ID = "Id";
    public static final String COLUMN_MAP_TUTOR_TAG_ID="tagId";
    public static final String COLUMN_MAP_TUTOR_TUTOR_ID="tutorId";

    private static final String CREATE_ATTENDANCE_TABLE =
            "CREATE TABLE " +TABLE_ARC + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_STUDENT_ID + " TEXT,"+
                    COLUMN_TUTOR_ID + " TEXT,"+
                    COLUMN_PROGRAMME_CODE +" TEXT,"+
                    COLUMN_SUBJECT_CODE +" TEXT,"+
                    COLUMN_ROOM_CODE +" TEXT,"+
                    COLUMN_DATE + " TEXT,"+
                    COLUMN_TYPE +" TEXT" +
                    ")";
    private static final String CREATE_MAP_TUTOR_TABLE=
            "CREATE TABLE " +TABLE_MAP_TUTOR + "("+COLUMN_MAP_TUTOR_ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_MAP_TUTOR_TAG_ID +"TEXT,"+
                    COLUMN_MAP_TUTOR_TUTOR_ID +"TEXT"+")";
    //String name, SQLiteDatabase.CursorFactory factory, int version :: THESE ARE taken out from parameter because
    //these are defined inside this class as a CONST
    public AppDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //executing command to create a table in app, receiving db instance.
        db.execSQL(CREATE_ATTENDANCE_TABLE);
        db.execSQL(CREATE_MAP_TUTOR_TABLE);
        Log.i(LOGTAG, "Table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + CREATE_ATTENDANCE_TABLE);
        onCreate(db);
    }
}
