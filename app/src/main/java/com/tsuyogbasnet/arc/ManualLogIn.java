package com.tsuyogbasnet.arc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by tsuyogbasnet on 22/04/15.
 */
public class ManualLogIn extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manual_login);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(logIn);
        Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(rollBack);

    }
    View.OnClickListener logIn = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ManualLogIn.this, SetupVariables.class);
            startActivity(intent);
        }
    };
    View.OnClickListener rollBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ManualLogIn.this,MainActivity.class);
            startActivity(intent);
        }
    };




}
