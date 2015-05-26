package com.tsuyogbasnet.arc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tsuyogbasnet.Utils.UIHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.tsuyogbasnet.arc.R.id.edittxtSubjectCode;

/**
 * Created by tsuyogbasnet on 24/04/15.
 */
public class SetupVariables extends ActionBarActivity {

    public static  String programmeCode;
    public static String subjectCode;
    public static String roomCode;
    public static String date;
    public static String type;

    Spinner spinner;
    ToggleButton tglbtn;

    public static final String TAG = "DATE";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_variables);

       // EditText subjectCode = (EditText) findViewById(edittxtSubjectCode);
        Button btnStartReg = (Button) findViewById(R.id.btnStartRegistration);
        //btnStartReg.setOnClickListener(collectRegister);
        tglbtn = (ToggleButton) findViewById(R.id.toggleButton);
        tglbtn.setOnClickListener(tglChange);

        //preparing drop down
        spinner = (Spinner)findViewById(R.id.spnCodeCourse);
        List<String> items = new ArrayList<>();
        items.add("----- Please Choose Course Code -----");
        items.add("HV618");
        items.add("GDIT32");
        items.add("BIT604");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

    public void startRegistration(View v){
        programmeCode = spinner.getSelectedItem().toString();
        subjectCode = UIHelper.getText(this, R.id.edittxtSubjectCode);
        roomCode = UIHelper.getText(this,R.id.edittxtRoomNumber);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        if (!subjectCode.equals("") && !roomCode.equals("") && !programmeCode.equals("----- Please Choose Course Code -----")){
            startActivity(new Intent(SetupVariables.this, CollectRegister.class));
        }
        else {
            Toast.makeText(this,"Please setup variables for registration",Toast.LENGTH_LONG).show();

        }
    }
    View.OnClickListener tglChange = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (tglbtn.isChecked()){
                type = "LECTURE";
            }else {
                type = "LAB";
            }
        }
    };
   // public void onItemSelected(AdapterView<?>stringAdapterView,)

//    View.OnClickListener collectRegister = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            this.subjectCode= UIHelper.getText(this,R.id.edittxtSubjectCode);
//            Intent intent = new Intent(SetupVariables.this,CollectRegister.class);
//            //subjectCode = UIHelper.getText(this,R.id.edittxtSubjectCode);
//            //startActivity(intent);
//        }
//    };

}
