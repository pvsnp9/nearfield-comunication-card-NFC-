package com.tsuyogbasnet.arc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;
import com.tsuyogbasnet.httpconnections.HttpManager;
import com.tsuyogbasnet.models.Attendance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsuyogbasnet on 28/06/15.
 */
public class UploadRegistration extends ActionBarActivity {

    public static final String LOGTAG ="UPLOAD";
    Button btnUpload;
    ProgressBar progressBar;

    private AlertDialog.Builder dlgBuilder;
    private AlertDialog.Builder setUpBuilder;

    SQLiteOpenHelper databaseHelper;
    SQLiteDatabase database;
    AppDataSource dataSource;
    List<Attendance> attendances = new ArrayList<Attendance>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        databaseHelper = new AppDbOpenHelper(this);
        database = databaseHelper.getWritableDatabase();
        dataSource = new AppDataSource(this);

        btnUpload = (Button)findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(uploadRegistration);

        //display variables
        TextView pCode= (TextView)findViewById(R.id.txtProg);
        TextView subject =(TextView) findViewById(R.id.txtSubj);
        TextView type = (TextView) findViewById(R.id.txtType);
        TextView count = (TextView)findViewById(R.id.txtTotal);

        pCode.setText("Programme: "+SetupVariables.programmeCode);
        subject.setText("Subject: "+SetupVariables.subjectCode);
        type.setText("Class Type: "+SetupVariables.type);
        count.setText("Total Students: "+dataSource.numberOfAttendances()+"");


        progressBar=(ProgressBar) findViewById(R.id.pbUpload);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#B88A00"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.INVISIBLE);
        getAttendanceData();


    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        database=databaseHelper.getWritableDatabase();
    }

    View.OnClickListener uploadRegistration = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
              if (isOnline()){
                  UploadTask uploadTask = new UploadTask();
                  uploadTask.execute("http://virtual.weltec.ac.nz/arc/api/alternateattendance");
              }else {
                  Toast.makeText(getBaseContext(), "Network is not available, Please try again making your device online", Toast.LENGTH_LONG).show();
              }
        }
    };
    protected void getAttendanceData(){
        //attendances = dataSource.findAllAttendance();
        attendances = dataSource.prepUpload();
        Toast.makeText(this,"number ofobjects: "+attendances.size()+"",Toast.LENGTH_SHORT).show();
    }
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else {return false;}
    }


    //Alert Dialogue
    private void completeTask(){
        dlgBuilder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        dlgBuilder.setTitle("Notification !!");
        dlgBuilder.setMessage("Attendance Posted to Server.");

        final ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.tick);
        layout.addView(imageView);
        dlgBuilder.setView(layout);
        dlgBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog dlgDismiss = dlgBuilder.create();
                dlgDismiss.dismiss();
                exitApp();
            }
        });
        AlertDialog dlgLogIn = dlgBuilder.create();
        dlgLogIn.show();

    }
    public void exitApp(){
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private class UploadTask extends AsyncTask<String, String,String>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            //String content = HttpManager.getRequestDataPackage(params[0]);
            String result="";
            for (int i=0; i<attendances.size(); i++){
                Attendance attendance = new Attendance();
                attendance= attendances.get(i);
                result = HttpManager.Post(params[0],attendance);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            dataSource.endAndUpload();
            completeTask();

        }
    }

}
