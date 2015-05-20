package com.tsuyogbasnet.arc;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tsuyogbasnet.Utils.UIHelper;
import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;
import com.tsuyogbasnet.models.Attendance;

/**
 * Created by tsuyogbasnet on 18/05/15.
 */
public class ManualRegistration extends ActionBarActivity {

    public static final String TAG = "INSERTDATA";

    SQLiteOpenHelper databaseHelper;
    SQLiteDatabase database;
    AppDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_registration);

        databaseHelper = new AppDbOpenHelper(this);
        //this methid will automatically call the onCreate method of AppDbOpenHelper.
        database = databaseHelper.getWritableDatabase();
        //dataSource.open();
    }
    public void manualRegistration(View v){
        //Toast.makeText(this, "Prgramme to handle insert students registration", Toast.LENGTH_LONG).show();
        createAttendance();

    }
    public void createAttendance(){
        Attendance attendance = new Attendance();
        attendance.setStudentId(UIHelper.getText(this,R.id.edittxtStudentId));
        attendance.setTutorId(ManualLogIn.tutorId);
        attendance.setProgrammeCode(SetupVariables.programmeCode);
        attendance.setSubjectCode(SetupVariables.subjectCode);
        attendance.setRoomCode(SetupVariables.roomCode);
        attendance.setDate(SetupVariables.date);
        attendance.setType(SetupVariables.type);
        //Log.i(TAG,attendance.getStudentId()+","+attendance.getProgrammeCode()+","+attendance.getTutorId());


    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        dataSource.close();
//        Log.i(TAG,"Db Closed");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        dataSource.open();
//        Log.i(TAG,"DB Opened");
//    }
}
