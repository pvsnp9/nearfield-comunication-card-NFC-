package com.tsuyogbasnet.arc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tsuyogbasnet.db.AppDataSource;


public class MainActivity extends ActionBarActivity {

    NfcAdapter nfcAdapter;
    private AlertDialog.Builder dlgBuilder;
    //just for demo, delete while using DB
    private final String TUTORID="2144429";
    private String inputCode;
    private String inputTutorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // nfcAdapter.enableForegroundDispatch(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                //Intent intent = new Intent(MainActivity.this,com.tsuyogbasnet.arc.ManualLogIn.class);
                //startActivity(intent);

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
        //eGetTutorId.setInputType(DEFAULT_KEYS_DIALER);
        layout.addView(eGetTutorId);
        final EditText eGetCode = new EditText(this);
        eGetCode.setHint("Enter Code");
        //eGetCode.setInputType(eGetCode.TYPE_CLASS_TEXT);
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
                //startActivity(new Intent(CollectRegister.this, CollectRegister.class));
            }
        });
        AlertDialog dlgLogIn = dlgBuilder.create();
        dlgLogIn.show();

    }
}
