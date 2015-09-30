package com.tsuyogbasnet.arc;

import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tsuyogbasnet.Utils.ListFilter;
import com.tsuyogbasnet.Utils.UIHelper;
import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;
import com.tsuyogbasnet.db.SetupVarDataSource;
import com.tsuyogbasnet.httpconnections.HttpManager;
import com.tsuyogbasnet.models.Programme;
import com.tsuyogbasnet.models.SetupVar;
import com.tsuyogbasnet.parsers.MapStudentJsonParser;
import com.tsuyogbasnet.parsers.ProgrammeJsonParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.tsuyogbasnet.arc.R.id.edittxtSubjectCode;
import static com.tsuyogbasnet.arc.R.id.spnType;

/**
 * Created by tsuyogbasnet on 24/04/15.
 */
public class SetupVariables extends ActionBarActivity {

    public static  String programmeCode;
    public static String subjectCode;
    public static String roomCode;
    public static String date;
    public static String type;
    public static String dayname;
    public static String classTime;

    private final String LOGTAG="TAGCOUNT";
    List<Programme> programmes;

    Spinner spinner;
    Spinner spinnerProgramme;

    SQLiteOpenHelper databaseHelper;
    SQLiteDatabase database;
    SetupVarDataSource varDataSource;

    public static final String TAG = "DATE";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_variables);

        databaseHelper = new AppDbOpenHelper(this);
        //this method will automatically call the onCreate method of AppDbOpenHelper.
        database = databaseHelper.getWritableDatabase();
        varDataSource = new SetupVarDataSource(this);
        if (isOnline()){
            SetUpTask task = new SetUpTask();
            task.execute("http://virtual.weltec.ac.nz/arc/api/programme");
        }


        //setting lecture type combo.
        spinnerProgramme = (Spinner)findViewById(R.id.spnType);
        List<String> typeItems = new ArrayList<>();
        typeItems.add("Class Type");
        typeItems.add("LAB");
        typeItems.add("LECTURE");
        typeItems.add("TUTORIALS");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,typeItems);
        spinnerProgramme.setAdapter(typeAdapter);

    }

    public void startRegistration(View v){
        database=databaseHelper.getWritableDatabase();
        programmeCode = spinner.getSelectedItem().toString();
        type = spinnerProgramme.getSelectedItem().toString();
        subjectCode = UIHelper.getText(this, R.id.edittxtSubjectCode);
        roomCode = UIHelper.getText(this,R.id.edittxtRoomNumber);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(calendar.DAY_OF_WEEK);
        if (day==1){ dayname ="SUNDAY";}
        else if(day==2){dayname="MONDAY";}
        else if(day==3){dayname="TUESDAY";}
        else if(day==4){dayname="WEDNESDAY";}
        else if(day==5){dayname="THURSDAY";}
        else if(day==6){dayname="FRIDAY";}
        else if(day==7){dayname="SATURDAY";}

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        String[] separate = date.split(" ");
        String[] separateTime = separate[1].split(":");
        classTime = separateTime[0];
        if (!subjectCode.equals("") && !roomCode.equals("") && !programmeCode.equals("----- Please Choose Course Code -----") && !type.equals("Class Type")){
            createSetup();
            startActivity(new Intent(SetupVariables.this, CollectRegister.class));
        }
        else {
            Toast.makeText(this,"Please setup variables for registration",Toast.LENGTH_LONG).show();

        }
    }

    public void createSetup(){
        SetupVar setupVar = new SetupVar();
        setupVar.setTutorId(MainActivity.UNIVERSAL_TUTOR_ID);
        setupVar.setProgrammerCode(programmeCode);
        setupVar.setSubjectCode(subjectCode);
        setupVar.setRoomNumber(roomCode);
        setupVar.setDay(dayname);
        setupVar.setTime(classTime);
        setupVar.setType(type);
        varDataSource.createSetup(setupVar);
    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else {return false;}
    }

    public void addSpinner(){
        List<String> programmeCode = new ArrayList<>();

        spinner = (Spinner)findViewById(R.id.spnCodeCourse);
        programmeCode = ListFilter.createProgrammeData(programmes);
        programmeCode.add(0,"----- Please Choose Course Code -----");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,programmeCode);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        Log.i(LOGTAG,programmeCode.size()+"");
    }

    private class SetUpTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            //update UI to notify user
        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            programmes = ProgrammeJsonParser.parseProgramme(s);
            addSpinner();
            //Toast.makeText(getBaseContext(),programmes.size()+"",Toast.LENGTH_SHORT).show();
        }
    }
}






















