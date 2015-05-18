package com.tsuyogbasnet.arc;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by tsuyogbasnet on 18/05/15.
 */
public class ManualRegistration extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_registration);
    }
    public void manualRegistration(View v){
        Toast.makeText(this, "Prgramme to handle insert students registration", Toast.LENGTH_LONG).show();

    }
}
