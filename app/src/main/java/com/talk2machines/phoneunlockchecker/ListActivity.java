package com.talk2machines.phoneunlockchecker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.talk2machines.phoneunlockchecker.api.Session;

import org.json.JSONArray;

/**
 * Created by lin_n on 26.11.2015.
 */
public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        final ListView gl = (ListView) findViewById(R.id.gruppelist);
        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        Session session = new Session();

        session.list(getApplicationContext(), new Session.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                Log.i("",result.toString());
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