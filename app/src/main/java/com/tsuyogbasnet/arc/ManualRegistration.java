package com.tsuyogbasnet.arc;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tsuyogbasnet.Utils.UIHelper;
import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;
import com.tsuyogbasnet.models.Attendance;

import java.util.List;

/**
 * Created by tsuyogbasnet on 18/05/15.
 */
public class ManualRegistration extends ActionBarActivity {

    public static final String TAG = "INSERTDATA";

    SQLiteOpenHelper databaseHelper;
    SQLiteDatabase database;
    AppDataSource dataSource;
    private List<String> manualRegistered; /// created to display manually registered students.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_registration);
        databaseHelper = new AppDbOpenHelper(this);
        //this method will automatically call the onCreate method of AppDbOpenHelper.
        database = databaseHelper.getWritableDatabase();
        dataSource = new AppDataSource(this);

        final EditText editTextStudentId = (EditText) findViewById(R.id.edittxtStudentId);
        Button btnRegistration = (Button) findViewById(R.id.btnRegister);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attendance attendance = new Attendance();
                createAttendance(attendance);
                editTextStudentId.setText("");
//
            }
        });
    }
//    public void manualRegistration(View v){
//        //Toast.makeText(this, "Prgramme to handle insert students registration", Toast.LENGTH_LONG).show();
//        createAttendance();
//
//    }
    public void createAttendance(Attendance attendance){
        attendance.setStudentId(UIHelper.getText(this,R.id.edittxtStudentId));
        //manualRegistered.add(attendance.getStudentId());
        attendance.setTutorId(ManualLogIn.tutorId);
        attendance.setProgrammeCode(SetupVariables.programmeCode);
        attendance.setSubjectCode(SetupVariables.subjectCode);
        attendance.setRoomCode(SetupVariables.roomCode);
        attendance.setDate(SetupVariables.date);
        attendance.setType(SetupVariables.type);
        attendance=dataSource.create(attendance);
        //Log.i(TAG,attendance.getStudentId()+"/"+attendance.getTutorId()+"/"+attendance.getProgrammeCode()+"/"+attendance.getSubjectCode()+"/"+attendance.getRoomCode()+"/"+attendance.getDate()+"/"+attendance.getType());
        Toast.makeText(this, "Attendance has been created with" +attendance.getAttendanceId(),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }
}
