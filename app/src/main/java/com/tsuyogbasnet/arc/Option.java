package com.tsuyogbasnet.arc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by tsuyogbasnet on 26/05/15.
 */
public class Option extends ActionBarActivity {

    Button bContinueRegistration, bManualRegistration, bUploadtoServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        bContinueRegistration = (Button) findViewById(R.id.bContinueRegistration);
        bManualRegistration = (Button) findViewById(R.id.bManualRegistration);
        bUploadtoServer = (Button) findViewById(R.id.bUploadtoServer);

        bContinueRegistration.setOnClickListener(continueRegistration);
        bManualRegistration.setOnClickListener(manualRegistration);
        bUploadtoServer.setOnClickListener(uploadtoServer);
    }
    View.OnClickListener continueRegistration = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            startActivity(new Intent(Option.this, CollectRegister.class));
        }
    };
    View.OnClickListener manualRegistration = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            startActivity(new Intent(Option.this,ManualRegistration.class));
        }
    };
    View.OnClickListener uploadtoServer = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Toast.makeText(Option.this,"Write Script to upload data to server",Toast.LENGTH_SHORT).show();
        }
    };

}
