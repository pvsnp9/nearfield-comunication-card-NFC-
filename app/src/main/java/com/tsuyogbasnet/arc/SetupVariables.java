package com.tsuyogbasnet.arc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsuyogbasnet on 24/04/15.
 */
public class SetupVariables extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_variables);

        Button btnStartReg = (Button) findViewById(R.id.btnStartRegistration);
        btnStartReg.setOnClickListener(collectRegister);

        //preparing drop down
        Spinner dropDown = (Spinner)findViewById(R.id.spnCodeCourse);
        List<String> items = new ArrayList<>();
        items.add("Course Code");
        items.add("HV618");
        items.add("GDIT32");
        items.add("BIT604");

       // ArrayAdapter<String> spinnerAdapter = new Ar
    }

    View.OnClickListener collectRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SetupVariables.this,CollectRegister.class);
            startActivity(intent);
        }
    };
}
