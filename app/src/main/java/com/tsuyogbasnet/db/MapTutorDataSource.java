package com.tsuyogbasnet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tsuyogbasnet.models.MapTutor;

import org.apache.http.auth.NTUserPrincipal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsuyogbasnet on 13/06/15.
 */
public class MapTutorDataSource {
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

    private static final String[] TUTOR_COLUMNS ={
            AppDbOpenHelper.COLUMN_MAP_TUTOR_ID,
            AppDbOpenHelper.COLUMN_MAP_TUTOR_TAG_ID,
            AppDbOpenHelper.COLUMN_MAP_TUTOR_TUTOR_ID
    };

    public MapTutorDataSource(Context context) {
        dbHelper = new AppDbOpenHelper(context);
        database = dbHelper.getWritableDatabase();
    }
    public void open(){
        database = dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }
    //public function to create tutor
    public MapTutor createTutor(MapTutor tutor){
        ContentValues values = new ContentValues();
        values.put(AppDbOpenHelper.COLUMN_MAP_TUTOR_TAG_ID, tutor.getTagId());
        values.put(AppDbOpenHelper.COLUMN_MAP_TUTOR_TUTOR_ID, tutor.getTutorId());

        Long insertId = database.insert(AppDbOpenHelper.TABLE_MAP_TUTOR,null,values);
        tutor.setId(insertId);
        return tutor;
    }
    public List<MapTutor> findTutors(){
        List<MapTutor> tutors=new ArrayList<MapTutor>();
        Cursor cursor = database.query(AppDbOpenHelper.TABLE_MAP_TUTOR,TUTOR_COLUMNS, null,null,null,null,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                MapTutor tutor = new MapTutor();
                tutor.setId(cursor.getLong(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_MAP_TUTOR_ID)));
                tutor.setTagId(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_MAP_TUTOR_TAG_ID)));
                tutor.setTutorId(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_MAP_TUTOR_TUTOR_ID)));
                tutors.add(tutor);
            }
        }
        return tutors;
    }
    public MapTutor findById(String tagID){
        MapTutor tutor = new MapTutor();
        Cursor cursor = database.query(AppDbOpenHelper.TABLE_MAP_TUTOR,TUTOR_COLUMNS,"tagId ='"+tagID+"'",null,null,null,null);
        if (cursor.getCount()>0){
            tutor.setId(cursor.getLong(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_MAP_TUTOR_ID)));
            tutor.setTagId(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_MAP_TUTOR_TAG_ID)));
            tutor.setTutorId(cursor.getString(cursor.getColumnIndex(AppDbOpenHelper.COLUMN_MAP_TUTOR_TUTOR_ID)));
        }
        return tutor;
    }
    public boolean validateTutorByTag(String tagId){
        Cursor cursor = database.query(AppDbOpenHelper.TABLE_MAP_TUTOR,TUTOR_COLUMNS,"tagId ='"+tagId+"'",null,null,null,null);
        if (cursor.getCount()>0){return true;}
        else {return false;}
    }
    public boolean validateTutorById(String Id){
        Cursor cursor = database.query(AppDbOpenHelper.TABLE_MAP_TUTOR,TUTOR_COLUMNS,"tutorId ='"+Id+"'",null,null,null,null);
        if (cursor.getCount()>0){return true;}
        else {return false;}
    }
}
