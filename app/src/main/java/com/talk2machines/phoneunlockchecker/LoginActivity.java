package com.talk2machines.phoneunlockchecker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        final EditText ln = (EditText) findViewById(R.id.loginName);
        final EditText lu = (EditText) findViewById(R.id.loginUsername);
        final Button lb = (Button) findViewById(R.id.loginbutton);

        lb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {



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
