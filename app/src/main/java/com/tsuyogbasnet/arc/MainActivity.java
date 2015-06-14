package com.tsuyogbasnet.arc;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsuyogbasnet.Utils.NfcHelper;
import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;
import com.tsuyogbasnet.db.MapTutorDataSource;
import com.tsuyogbasnet.models.MapTutor;


public class MainActivity extends ActionBarActivity {

    private AlertDialog.Builder dlgBuilder;
    //just for demo, delete while using DB
    public static final String TUTORID="244816BF";
    private String inputCode;
    private String inputTutorId;

    //erase when its done
    SQLiteOpenHelper databaseHelper;
    SQLiteDatabase database;
    MapTutorDataSource mapTutorDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new AppDbOpenHelper(this);
        database = databaseHelper.getWritableDatabase();
        mapTutorDataSource = new MapTutorDataSource(this);
        //createTempTutor();
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
            String tagId = NfcHelper.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                if (mapTutorDataSource.validateTutorByTag(tagId)){
                    startActivity(new Intent(MainActivity.this,SetupVariables.class));

                }else {
                    Toast.makeText(getBaseContext(), "You are not recognized as Tutor in this system", Toast.LENGTH_LONG).show();
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
        eGetCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(eGetCode);

        dlgBuilder.setView(layout);

        dlgBuilder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputCode=eGetCode.getText().toString().trim();
                inputTutorId=eGetTutorId.getText().toString().trim();
                if(inputCode.length()>0 && inputTutorId.length()>0){
                    if (inputCode.equals(CollectRegister.CODE) && inputTutorId.equals(TUTORID)) {
                        startActivity(new Intent(MainActivity.this,SetupVariables.class));
                    }
                    else {
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
    public void createTempTutor(){
        MapTutor tutor = new MapTutor();
        tutor.setTagId("244816BF");
        tutor.setTutorId("2144429");
        mapTutorDataSource.createTutor(tutor);
    }
}
