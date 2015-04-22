package com.tsuyogbasnet.arc;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

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
}
