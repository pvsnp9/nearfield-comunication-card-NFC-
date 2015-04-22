package com.tsuyogbasnet.arc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    NfcAdapter nfcAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
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
                Intent intent = new Intent(MainActivity.this,com.tsuyogbasnet.arc.ManualLogIn.class);
                startActivity(intent);

        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.login) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
