package com.tsuyogbasnet.arc;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tsuyogbasnet.Utils.ListFilter;
import com.tsuyogbasnet.Utils.NfcHelper;
import com.tsuyogbasnet.Utils.UIHelper;
import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;
import com.tsuyogbasnet.db.MapStudentDataSource;
import com.tsuyogbasnet.httpconnections.HttpManager;
import com.tsuyogbasnet.models.Attendance;
import com.tsuyogbasnet.models.MapStudents;
import com.tsuyogbasnet.models.SetupVar;
import com.tsuyogbasnet.parsers.MapStudentJsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsuyogbasnet on 22/04/15.
 */
public class CollectRegister extends ActionBarActivity {
    //following List is defined here to populate list view from inner class
    public static List<MapStudents> mapStudentses = new ArrayList<MapStudents>();
    private List<Attendance> attendances = new ArrayList<Attendance>();

    private AlertDialog.Builder dlgBuilder;
    private String displayFlag ="";

    SQLiteOpenHelper databaseHelper;
    SQLiteDatabase database;
    AppDataSource dataSource;
    MapStudentDataSource mapStudentDataSource;

    private String studentTagId;
    private String idNumber;
    public static final String CODE = "56789";
    private String inputCode;
    TextView count;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_register);
        //temp data but load from db setupvar.
        TextView pCode= (TextView)findViewById(R.id.txtprgrm);
        TextView subject =(TextView) findViewById(R.id.txtSubj);
        TextView room =(TextView) findViewById(R.id.txtRoom);
        count = (TextView)findViewById(R.id.txtCount);

        databaseHelper = new AppDbOpenHelper(this);
        database = databaseHelper.getWritableDatabase();
        dataSource = new AppDataSource(this);
        mapStudentDataSource = new MapStudentDataSource(this);

        pCode.setText("Program: "+SetupVariables.programmeCode);
        subject.setText("Subject: "+SetupVariables.subjectCode);
        room.setText("Room: "+SetupVariables.roomCode);
        count.setText("Count: "+dataSource.numberOfAttendances());

        progressBar=(ProgressBar) findViewById(R.id.collectPb);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#B88A00"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.INVISIBLE);
        //downloading students data from server via API
        downloadStudents();

    }

    private void prepDialog() {
        dlgBuilder = new AlertDialog.Builder(this);
        final EditText eGetCode = new EditText(this);
        eGetCode.setHint("Enter  Tutor Code");
        eGetCode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        dlgBuilder.setTitle("Tutor LogIn");
        dlgBuilder.setView(eGetCode);
        dlgBuilder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputCode = eGetCode.getText().toString();
                if (CODE.equals(inputCode)) {
                    startActivity(new Intent(CollectRegister.this, Option.class));
                } else {
                    Toast.makeText(getBaseContext(), "Incorrect Code, Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dlgBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog dlgDismiss = dlgBuilder.create();
                dlgDismiss.dismiss();
                //startActivity(new Intent(CollectRegister.this, CollectRegister.class));
            }
        });
        AlertDialog dlgLogIn = dlgBuilder.create();
        dlgLogIn.show();

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

        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
        dataSource.close();
    }

    @Override
    public void onBackPressed() {
        /// this method is written here for do nothing if back button is pressed.
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            //check ID card whether its Tutor or Student ID card TAG.
            if (NfcHelper.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)).equals(MainActivity.TUTORID)) {
                startActivity(new Intent(CollectRegister.this, Option.class));
            } else {
                this.studentTagId = NfcHelper.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                dataSource.open();
                //validating students
                String findStudent=ListFilter.findStudent(mapStudentses,studentTagId);
                if (findStudent !=null){
                    idNumber=findStudent;
                    //stopping duplicate registration
                    if (!dataSource.isRegistered("studentId ='"+idNumber+"'")){
                        displayFlag="SUCCESS";
                        createAttendance();
                        notifyUser();
                        attendances = dataSource.findAllAttendance();
                        count.setText("Count: "+dataSource.numberOfAttendances());
                    }else {
                        displayFlag="FAIL";
                        notifyUser();
                    }
                }else {
                    displayFlag="UNKNOWN";
                    notifyUser();
                }

                //instantiating inner class SimpleListADapter
                ArrayAdapter<Attendance> arrayAdapter = new SimpleListADapter();
                //finding listview in main view.
                ListView lv = (ListView) findViewById(R.id.listView);
                lv.setAdapter(arrayAdapter);
                //setListAdapter(arrayAdapter);
            }
        }
    }

    public void createAttendance() {
        Attendance attendance = new Attendance();
        attendance.setStudentId(this.idNumber);
        attendance.setTutorId(MainActivity.UNIVERSAL_TUTOR_ID);
        attendance.setProgrammeCode(SetupVariables.programmeCode);
        attendance.setSubjectCode(SetupVariables.subjectCode);
        attendance.setRoomCode(SetupVariables.roomCode);
        attendance.setDate(SetupVariables.date);
        attendance.setType(SetupVariables.type);
        attendance = dataSource.create(attendance);
        //Log.i(TAG,attendance.getStudentId()+"/"+attendance.getTutorId()+"/"+attendance.getProgrammeCode()+"/"+attendance.getSubjectCode()+"/"+attendance.getRoomCode()+"/"+attendance.getDate()+"/"+attendance.getType());
        //Toast.makeText(this, "Attendance has been created with" +this.studentID +"Tag Number",Toast.LENGTH_LONG).show();
    }

    private void downloadStudents(){
        if (isOnline()){
            requestStudentData("http://virtual.weltec.ac.nz/arc/api/mapstudentId");
        }else {
            Toast.makeText(this, "Network is not available, Please try again making your device online", Toast.LENGTH_LONG).show();
        }
    }
    private void requestStudentData(String uri){
        CollectTask collectTask = new CollectTask();
        collectTask.execute(uri);
    }
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else {return false;}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_option, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logInOption:
                prepDialog();
                //startActivity(new Intent(CollectRegister.this, Option.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void notifyUser(){
        dlgBuilder = new AlertDialog.Builder(this);
        String  title="Notification";
        String message="";
        ImageView imageView = new ImageView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        if (displayFlag.equals("SUCCESS")){
            message="Success";
            imageView.setImageResource(R.drawable.tick);
        }else if (displayFlag.equals("UNKNOWN")){
            message="Unknown Card !!";
            imageView.setImageResource(R.drawable.unknown);
        }else if (displayFlag.equals("FAIL")){
            message="You have already registered";
            imageView.setImageResource(R.drawable.warning);
        }

        dlgBuilder.setTitle(title);
        dlgBuilder.setMessage(message);

        layout.addView(imageView);
        dlgBuilder.setView(layout);
        dlgBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog dlgDismiss = dlgBuilder.create();
                dlgDismiss.dismiss();
            }
        });
        AlertDialog dlgLogIn = dlgBuilder.create();
        dlgLogIn.show();

    }

  /// creating list view to populate id
  private class SimpleListADapter extends ArrayAdapter<Attendance> {

    public SimpleListADapter() {
        super(CollectRegister.this, R.layout.activity_student_id,attendances );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView ==  null){
            itemView = getLayoutInflater().inflate(R.layout.activity_student_id,parent,false);
        }
        //taking current position of attendance list.
        Attendance currentAttendance = attendances.get(position);
        TextView makeText = (TextView) itemView.findViewById(R.id.studentId);
        makeText.setText(currentAttendance.getStudentId());
        return itemView;
    }
}

  private class CollectTask extends AsyncTask<String, String, String >{
      @Override
      protected void onPreExecute() {
          super.onPreExecute();
          progressBar.setVisibility(View.VISIBLE);
      }

      @Override
      protected String doInBackground(String... params) {
          String content = HttpManager.getData(params[0]);
          return content;
      }

      @Override
      protected void onPostExecute(String result) {
          if (result != null){
              mapStudentses = MapStudentJsonParser.ParseStudents(result);
              progressBar.setVisibility(View.INVISIBLE);
          }else {
              progressBar.setVisibility(View.INVISIBLE);
              Toast.makeText(getBaseContext(),"Data not Found, Check Network Status",Toast.LENGTH_LONG).show();
          }
      }
  }
}




