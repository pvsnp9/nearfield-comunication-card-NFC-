package com.tsuyogbasnet.arc;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tsuyogbasnet.db.AppDataSource;
import com.tsuyogbasnet.db.AppDbOpenHelper;

/**
 * Created by tsuyogbasnet on 26/05/15.
 */
public class Option extends ActionBarActivity {

    SQLiteOpenHelper databaseHelper;
    SQLiteDatabase database;
    AppDataSource dataSource;

    Button bContinueRegistration, bManualRegistration, bUploadtoServer,bResetSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        databaseHelper = new AppDbOpenHelper(this);
        database = databaseHelper.getWritableDatabase();
        dataSource = new AppDataSource(this);

        bContinueRegistration = (Button) findViewById(R.id.bContinueRegistration);
        bManualRegistration = (Button) findViewById(R.id.bManualRegistration);
        bUploadtoServer = (Button) findViewById(R.id.bUploadtoServer);
        bResetSession = (Button) findViewById(R.id.btnResteSession);

        bContinueRegistration.setOnClickListener(continueRegistration);
        bManualRegistration.setOnClickListener(manualRegistration);
        bUploadtoServer.setOnClickListener(uploadtoServer);
        bResetSession.setOnClickListener(resetSession);

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
            startActivity(new Intent(Option.this, UploadRegistration.class));
//            if (dataSource.endAndUpload()){
//                //TODO write script to upload data to server using objects.
//                Toast.makeText(Option.this,"Data has been removed and uploaded to the server",Toast.LENGTH_SHORT).show();
//                finish();
//                System.exit(0);
//            }else {
//                Toast.makeText(Option.this,"App couldn't complete the action, there is no data",Toast.LENGTH_SHORT).show();
//            }

        }
    };
    View.OnClickListener resetSession = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            dataSource.endAndUpload();
            finish();
           startActivity(new Intent(Option.this,SetupVariables.class));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
}
