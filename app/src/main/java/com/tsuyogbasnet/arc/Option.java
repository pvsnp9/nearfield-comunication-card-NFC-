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

    Button bContinueRegistration, bManualRegistration, bUploadtoServer;
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
            if (dataSource.endAndUpload()){
                //TODO write script to upload data to server using objects.
                Toast.makeText(Option.this,"Data has been removed and uploaded to the server",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(Option.this,"App couldn't complete the action, there is no data",Toast.LENGTH_SHORT).show();
            }

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
