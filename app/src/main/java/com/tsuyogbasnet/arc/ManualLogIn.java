package com.tsuyogbasnet.arc;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tsuyogbasnet.Utils.UIHelper;
import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;

/**
 * Created by tsuyogbasnet on 22/04/15.
 */
public class ManualLogIn extends ActionBarActivity {
    public static String tutorId;
    private String password;
    SQLiteOpenHelper databaseHelper;
    AppDataSource appDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new AppDbOpenHelper(this);
        appDataSource=new AppDataSource(this);

        setContentView(R.layout.activity_manual_login);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(logIn);
        Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(rollBack);

    }
    View.OnClickListener logIn = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            makeTutorId();
            getPassword();
            if (!tutorId.equals("") && !password.equals("")) {
                appDataSource.open();

                Intent intent = new Intent(ManualLogIn.this, SetupVariables.class);
                startActivity(intent);
            }else {
               Toast.makeText(getBaseContext(),"Please provide LogIn Credentials",Toast.LENGTH_LONG).show();
            }
        }
    };
    View.OnClickListener rollBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ManualLogIn.this,MainActivity.class);
            startActivity(intent);
        }
    };
    private void makeTutorId(){
        tutorId= UIHelper.getText(this,R.id.editTxtId);
    }
    private void getPassword(){password=UIHelper.getText(this,R.id.editTxtPassword);}




}
