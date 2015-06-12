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
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tsuyogbasnet.Utils.NfcHelper;
import com.tsuyogbasnet.Utils.UIHelper;
import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;
import com.tsuyogbasnet.models.Attendance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsuyogbasnet on 22/04/15.
 */
public class CollectRegister extends ActionBarActivity {
    //following List is defined here to populate list view from inner class
    private List<Attendance> attendances = new ArrayList<Attendance>();

    SQLiteOpenHelper databaseHelper;
    SQLiteDatabase database;
    AppDataSource dataSource;

    private String studentID;
    public static final String CODE = "98765";
    private String inputCode;
    private AlertDialog.Builder dlgBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_register);
        databaseHelper = new AppDbOpenHelper(this);
        database = databaseHelper.getWritableDatabase();
        dataSource = new AppDataSource(this);
    }

    private void prepDialog() {
        dlgBuilder = new AlertDialog.Builder(this);
        final EditText eGetCode = new EditText(this);
        eGetCode.setHint("Enter  Tutor Code");
        eGetCode.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                this.studentID = NfcHelper.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                dataSource.open();
                createAttendance();
                Toast.makeText(this, "Attendance has been created with" + this.studentID + "Tag Number", Toast.LENGTH_LONG).show();
                 attendances = dataSource.findAllAttendance();
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
        attendance.setStudentId(this.studentID);
        attendance.setTutorId(ManualLogIn.tutorId);
        attendance.setProgrammeCode(SetupVariables.programmeCode);
        attendance.setSubjectCode(SetupVariables.subjectCode);
        attendance.setRoomCode(SetupVariables.roomCode);
        attendance.setDate(SetupVariables.date);
        attendance.setType(SetupVariables.type);
        attendance = dataSource.create(attendance);
        //Log.i(TAG,attendance.getStudentId()+"/"+attendance.getTutorId()+"/"+attendance.getProgrammeCode()+"/"+attendance.getSubjectCode()+"/"+attendance.getRoomCode()+"/"+attendance.getDate()+"/"+attendance.getType());
        //Toast.makeText(this, "Attendance has been created with" +this.studentID +"Tag Number",Toast.LENGTH_LONG).show();
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
}


//    private void prepPopupWindow(){
//        try {
//            LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//            View popupView = inflater.inflate(R.layout.activity_popup_manual_registration,null);
//            final PopupWindow popupWindow = new PopupWindow(popupView);
//
//            Button dismiss =(Button)popupView.findViewById(R.id.bDismiss);
//            Button manualRegLogin = (Button)popupView.findViewById(R.id.bManualRegLogin);
//
//            dismiss.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popupWindow.dismiss();
//                }
//            });
//            manualRegLogin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getBaseContext(), "Check code here", Toast.LENGTH_SHORT).show();
//                }
//            });
//            popupWindow.showAsDropDown(bManualRegLogin);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }



