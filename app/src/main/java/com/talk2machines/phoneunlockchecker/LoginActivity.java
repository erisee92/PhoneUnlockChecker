package com.talk2machines.phoneunlockchecker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        final EditText ln = (EditText) findViewById(R.id.loginName);
        final EditText lun = (EditText) findViewById(R.id.loginUsername);
        final Button lb = (Button) findViewById(R.id.loginbutton);

        lb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // test, ob die name und username schon eingetippen sind.
                if(ln.getText().toString().equals("") || lun.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), R.string.loginerror, Toast.LENGTH_LONG).show();
                }else{
                //schicken Name und Username an Server weiter
                    //TODO  import user.class schiecken datei an server

                //test ob ein userid zurückbekommen, wenn ja, speichern userid in sharePreferences, und leitet zu ListActivity
                String userid = "du bis kacke";
                if(userid !=  null ){

                    prefs = getSharedPreferences("PUC", 0);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_ID", userid);
                    edit.commit();

                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, ListActivity.class);
                    startActivity(intent);
                    finish();

                }

                }

            }
        });


    }


        @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onRestart() {
        super.onRestart();


    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onPause() {

        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

}
