package com.talk2machines.phoneunlockchecker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.talk2machines.phoneunlockchecker.api.Session;

import org.json.JSONObject;

/**
 * Created by lin_n on 07.01.2016.
 */
public class SessionActivity extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session);
        Button startbu = (Button)findViewById(R.id.startbutton);
        Button deletebu = (Button)findViewById(R.id.deletebutton);

        final ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        prefs = getSharedPreferences("PUC", 0);
        SharedPreferences.Editor edit = prefs.edit();


        //Hier wird den aktuellen USERNAME und ADMIN vergliechen. wenn der Username auch den Admin der Gruppe ist, dann wird Start und delete button angezeigt zur verfügung.

        if(!prefs.getBoolean("ADMIN",false)){

            startbu.setVisibility(View.GONE);
            deletebu.setVisibility(View.GONE);
        }

        startbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getString("SESSION_STATE","false").equals("true")){
                Session.changeStateOfSession(prefs.getString("SESSION_ID", ""), "false", getApplicationContext(), new Session.VolleyCallback2() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        //TODO
                    }

                    @Override
                    public void onError(JSONObject result) {

                    }
                });

                }

            }
        });



/*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ListActivity.this, CreatGroupActivity.class);
                startActivity(intent);
            }
        });

        */

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
