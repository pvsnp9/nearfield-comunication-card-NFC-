package com.tsuyogbasnet.arc;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tsuyogbasnet.Utils.NfcHelper;
import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;
import com.tsuyogbasnet.db.MapTutorDataSource;
import com.tsuyogbasnet.db.SetupVarDataSource;
import com.tsuyogbasnet.httpconnections.HttpManager;
import com.tsuyogbasnet.models.MapTutor;
import com.tsuyogbasnet.models.SetupVar;
import com.tsuyogbasnet.parsers.MapTutorJsonParser;
import com.tsuyogbasnet.parsers.MapTutorParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private AlertDialog.Builder dlgBuilder;
    private AlertDialog.Builder setUpBuilder;
    private String date;
    private ProgressBar progressBar;
    private TextView notifyUser;
    private TextView scanNfc;
    //it holds tutor TagId
    public static  String TUTORID;
    //it holds tutor ID number
    public static String UNIVERSAL_TUTOR_ID;
    private String inputCode;
    private String inputTutorId;

    //erase when its done
    SQLiteOpenHelper databaseHelper;
    AppDataSource appDataSource;
    MapTutorDataSource mapTutorDataSource;
    SetupVarDataSource setupVarDataSource;
    //setting list to POJO objects data form API
    List<MapTutor> mapTutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notifyUser = (TextView)findViewById(R.id.txtNotifyUser);
        scanNfc = (TextView)findViewById(R.id.txtView2);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#B88A00"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.INVISIBLE);
        //hasNfcFeature(this);
        databaseHelper = new AppDbOpenHelper(this);
        appDataSource=new AppDataSource(this);
        mapTutorDataSource = new MapTutorDataSource(this);
        setupVarDataSource = new SetupVarDataSource(this);
        mapTutorDataSource.eraseTutor();
        downloadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, NfcHelper.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            appDataSource.open();
            //checking whether the data has been uploaded to the server or not.
            if (!appDataSource.checkAttendanceData()){
                TUTORID=NfcHelper.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                if (mapTutorDataSource.validateTutorByTag(TUTORID)){
                    //gettting and setting Tutor ID to acces over thw application.
                    MapTutor tutorDataSource= mapTutorDataSource.findByTagId(TUTORID);
                    UNIVERSAL_TUTOR_ID=tutorDataSource.getTutorId();
                    //startActivity(new Intent(MainActivity.this,SetupVariables.class));
                    findSetup();//getting previous setup data

                }else {
                    Toast.makeText(getBaseContext(), "You are not recognized as Tutor in this system", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(this,"Application has Registratation Data, Please choose option to go",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,Option.class));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.login:
                prepDialog();
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.login) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepDialog(){
        dlgBuilder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        dlgBuilder.setTitle("Manual LogIn");
        dlgBuilder.setMessage("Enter Tutor ID and Code");

        final EditText eGetTutorId = new EditText(this);
        eGetTutorId.setHint("Tutor ID");
        eGetTutorId.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(eGetTutorId);
        final EditText eGetCode = new EditText(this);
        eGetCode.setHint("Enter Code");
        eGetCode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        layout.addView(eGetCode);

        dlgBuilder.setView(layout);

        dlgBuilder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputCode=eGetCode.getText().toString().trim();
                inputTutorId=eGetTutorId.getText().toString().trim();
                if(inputCode.length()>0 && inputTutorId.length()>0){
                    if (mapTutorDataSource.validateTutorById(inputTutorId) && inputCode.equals(CollectRegister.CODE)){
                        UNIVERSAL_TUTOR_ID = inputTutorId;
                        findSetup();//finding setup for class
                    }else {
                        Toast.makeText(getBaseContext(), "Incorrect Code, Try Again", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(), "Please Input Tutor ID and Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dlgBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog dlgDismiss = dlgBuilder.create();
                dlgDismiss.dismiss();
            }
        });
        AlertDialog dlgLogIn = dlgBuilder.create();
        dlgLogIn.show();

    }
    public void findSetup(){
        SetupVar setupVar;
        String filter;
        String dayname = null;

        Integer classTime;
        Integer classEndTime;
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
        classTime = Integer.parseInt(separateTime[0]);
        classEndTime = classTime+1;
        filter ="tutorId='"+UNIVERSAL_TUTOR_ID+"'"+"AND"+" day='"+dayname+"'"+"AND"+"( time >= "+classTime+" OR"+" time <= "+classEndTime+" )";
        setupVar=setupVarDataSource.findFilter(filter);
        if (setupVar !=null){
            setupVarDlg(setupVar);
        }else {
            startActivity(new Intent(MainActivity.this,SetupVariables.class));
        }
    }
    //getting and displaying setup data
    private void setupVarDlg(final SetupVar setupVar){
        setUpBuilder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        setUpBuilder.setTitle("Registration Variables");
        setUpBuilder.setMessage("We found following data");

        final TextView tutorId = new TextView(this);
        tutorId.setText("Tutor: "+UNIVERSAL_TUTOR_ID);
        tutorId.setTextColor(Color.parseColor("#ff1f6515"));
        tutorId.setTypeface(Typeface.SANS_SERIF);
        layout.addView(tutorId);
        final TextView programmeCode = new TextView(this);
        programmeCode.setText("Programme: "+setupVar.getProgrammerCode());
        programmeCode.setTextColor(Color.parseColor("#ff1f6515"));
        programmeCode.setTypeface(Typeface.SANS_SERIF);
        layout.addView(programmeCode);
        final TextView subjectCode = new TextView(this);
        subjectCode.setText("Subject:" +setupVar.getSubjectCode());
        subjectCode.setTextColor(Color.parseColor("#ff1f6515"));
        subjectCode.setTypeface(Typeface.SANS_SERIF);
        layout.addView(subjectCode);
        final TextView roomNumber = new TextView(this);
        roomNumber.setText("Room: "+setupVar.getRoomNumber());
        roomNumber.setTextColor(Color.parseColor("#ff1f6515"));
        roomNumber.setTypeface(Typeface.SANS_SERIF);
        layout.addView(roomNumber);
        final TextView day = new TextView(this);
        day.setText("Day: "+setupVar.getDay());
        day.setTextColor(Color.parseColor("#ff1f6515"));
        day.setTypeface(Typeface.SANS_SERIF);
        layout.addView(day);
        final TextView time = new TextView(this);
        time.setText("Time: " +setupVar.getTime());
        time.setTextColor(Color.parseColor("#ff1f6515"));
        time.setTypeface(Typeface.SANS_SERIF);
        layout.addView(time);
        final TextView type = new TextView(this);
        type.setText(setupVar.getType());
        type.setTextColor(Color.parseColor("#ff1f6515"));
        type.setTypeface(Typeface.SANS_SERIF);
        layout.addView(type);
        setUpBuilder.setView(layout);

        setUpBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //initializing setup variables.
                initializeSetUp(setupVar);
                startActivity(new Intent(MainActivity.this,CollectRegister.class));
            }
        });
        setUpBuilder.setNegativeButton("NO, Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this,SetupVariables.class));
            }
        });
        AlertDialog dlgLogIn = setUpBuilder.create();
        dlgLogIn.show();

    }
    public void initializeSetUp(SetupVar setupVar){
        SetupVariables.programmeCode=setupVar.getProgrammerCode();
        SetupVariables.subjectCode=setupVar.getSubjectCode();
        SetupVariables.roomCode=setupVar.getRoomNumber();
        SetupVariables.date=date;
        SetupVariables.type=setupVar.getType();
    }

    public void createTempTutor(){
        //you might want to update this logic
        MapTutor tutor = new MapTutor();
        tutor.setTagId("244816BF");
        tutor.setTutorId("2144429");
        mapTutorDataSource.createTutor(tutor);
        tutor.setTagId("A22D3733");
        tutor.setTutorId("900977");
        mapTutorDataSource.createTutor(tutor);

        //Toast.makeText(this,"Tutor Created", Toast.LENGTH_LONG).show();

//        tutor.setTagId("9431E410");
//        tutor.setTutorId("2143940");
//        mapTutorDataSource.createTutor(tutor);
    }
    //this method receives data from on post execute and calls the createTutor  method of MapTutorData source to create data.
    public void createTutor(){
        if (mapTutors != null){
            for (int i=0; i<mapTutors.size();i++){
                MapTutor tutor = new MapTutor();
                tutor=mapTutors.get(i);
                if(mapTutorDataSource.createTutor(tutor) != null){
                    Log.i("PARSING","Success");
                }else {Log.i("PARSING","Failed");}
            }

        }else {
            Toast.makeText(this,"Sorry, App did not received any data from List",Toast.LENGTH_LONG).show();
        }

    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else {return false;}
    }
    protected void downloadData(){
        if (isOnline()){
            requestData("http://virtual.weltec.ac.nz/arc/api/maptutors");
        }else {
            Toast.makeText(this, "Network is not available, Please try again making your device online", Toast.LENGTH_LONG).show();
        }
    }
    protected void requestData(String uri){
        MainTask task = new MainTask();
        //executing asyncTask inner class
        task.execute(uri);
    }
    protected boolean hasNfcFeature(Context context){
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)){
           return true;
        }else {
           return false;
        }
    }


    private class MainTask extends AsyncTask<String, String, String>{
        // we can have access to the main UI thread. this sections is to display progress bar or something to notify users
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            notifyUser.setText("Loading Data please wait ....");
        }

        @Override
        protected String doInBackground(String... params) {
            //calling API to given URI
            String content = HttpManager.getData(params[0]);
            return content;
        }
        //receives a result. the values returned by doInBackground method directly passed to the onPostExecute.
        //at this method we have access to UI or main thread.
        @Override
        protected void onPostExecute(String result) {
           if (result != null){
               mapTutors = MapTutorJsonParser.ParseTutors(result);
               createTutor();
               progressBar.setVisibility(View.INVISIBLE);
               notifyUser.setText("");
               scanNfc.setText("Please Scan Your NFC ID Card");
           }else {
               Toast.makeText(getBaseContext(),"Data not Found, Check Network Status",Toast.LENGTH_LONG).show();
               progressBar.setVisibility(View.INVISIBLE);
               notifyUser.setText("");
           }
        }
        //this method receives parameters from on call of publishProgress method of doInBackgroundMethod.
        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }
    }
}
