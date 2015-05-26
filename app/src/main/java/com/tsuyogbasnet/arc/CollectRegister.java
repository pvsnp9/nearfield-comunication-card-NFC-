package com.tsuyogbasnet.arc;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import com.tsuyogbasnet.Utils.UIHelper;

/**
 * Created by tsuyogbasnet on 22/04/15.
 */
public class CollectRegister extends ActionBarActivity {

    public static final String CODE="98765";
    private String inputCode;
    private AlertDialog.Builder dlgBuilder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_register);
    }
    private void prepDialog(){
        dlgBuilder = new AlertDialog.Builder(this);
        final EditText eGetCode = new EditText(this);
        eGetCode.setHint("Enter  Tutor Code");
        eGetCode.setInputType(DEFAULT_KEYS_DIALER);
        dlgBuilder.setTitle("Tutor LogIn");
        dlgBuilder.setView(eGetCode);
        dlgBuilder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputCode=eGetCode.getText().toString();
                if(CODE.equals(inputCode)){
                    startActivity(new Intent(CollectRegister.this,Option.class));
                }else{
                    Toast.makeText(getBaseContext(), "Incorrect Code, Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dlgBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog dlgDismiss = dlgBuilder.create();
                dlgDismiss.dismiss();
                //startActivity(new Intent(CollectRegister.this, CollectRegister.class));
            }
        });
        AlertDialog dlgLogIn = dlgBuilder.create();
        dlgLogIn.show();

    }

    @Override
    public void onBackPressed() {

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
            case R.id.logInOption:
                prepDialog();
                //startActivity(new Intent(CollectRegister.this, Option.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }




//    private void prepPopupWindow(){
//        try {
//            LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//            View popupView = inflater.inflate(R.layout.activity_popup_manual_registration,null);
//            final PopupWindow popupWindow = new PopupWindow(popupView);
//
//            Button dismiss =(Button)popupView.findViewById(R.id.bDismiss);
//            Button manualRegLogin = (Button)popupView.findViewById(R.id.bManualRegLogin);
//
//            dismiss.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popupWindow.dismiss();
//                }
//            });
//            manualRegLogin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getBaseContext(), "Check code here", Toast.LENGTH_SHORT).show();
//                }
//            });
//            popupWindow.showAsDropDown(bManualRegLogin);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


}
