package com.tsuyogbasnet.arc;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by tsuyogbasnet on 22/04/15.
 */
public class CollectRegister extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_collect_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_option, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.manualRegistration:
                startActivity(new Intent(CollectRegister.this, ManualRegistration.class));
                //Intent intent = new Intent(CollectRegister.this,ManualRegistration.class);
                break;
            case R.id.continueRegistration:
                Toast.makeText(this,"Continue to collection", Toast.LENGTH_LONG).show();
                break;
            case R.id.endAndUpload:
                Toast.makeText(this,"End and upload Data to server",Toast.LENGTH_LONG).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
  }
