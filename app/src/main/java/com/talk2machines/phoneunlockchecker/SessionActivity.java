package com.talk2machines.phoneunlockchecker;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.talk2machines.phoneunlockchecker.api.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lin_n on 07.01.2016.
 */
public class SessionActivity extends AppCompatActivity {

    SharedPreferences prefs;
    String newstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session);
        final Button startbu = (Button)findViewById(R.id.startbutton);
        Button deletebu = (Button)findViewById(R.id.deletebutton);

        Bundle bundle = this.getIntent().getExtras();
        final String s_id = bundle.getString("s_id");
        final String s_state = bundle.getString("s_state");

        final ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        prefs = getSharedPreferences("PUC", 0);



        //Hier wird den aktuellen USERNAME und ADMIN vergliechen. wenn der Username auch den Admin der Gruppe ist, dann wird Start und delete button angezeigt zur verfügung.

        if(!prefs.getBoolean("ADMIN",false)){
            startbu.setVisibility(View.GONE);
            deletebu.setVisibility(View.GONE);
        }

        if (s_state != null && s_state.equals("true")) {
            startbu.setText(R.string.sessioncontrol_2);
        } else if (s_state != null && s_state.equals("false")) {
            startbu.setText(R.string.sessioncontrol_1);
        }

        startbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getString("SESSION_STATE","false").equals("true")){
                    newstate = "false";
                } else {
                    newstate = "true";
                }

                Session.changeStateOfSession(prefs.getString("SESSION_ID", ""), newstate, getApplicationContext(), new Session.VolleyCallback2() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String response = result.getString("response");
                            if(response.equals("stopped")){
                                final SharedPreferences.Editor edit = prefs.edit();
                                edit.putString("SESSION_STATE","false");
                                edit.commit();
                                startbu.setText(R.string.sessioncontrol_1);
                            } else if (response.equals("started")){
                                final SharedPreferences.Editor edit = prefs.edit();
                                edit.putString("SESSION_STATE","true");
                                edit.commit();
                                startbu.setText(R.string.sessioncontrol_2);
                            }

                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(JSONObject result) {

                    }
                });



            }
        });


        Session.getSession(s_id, getApplicationContext(), new Session.VolleyCallback(){

            @Override
            public void onSuccess(JSONObject result) {
                Log.i("Join", result.toString());
                try {
                    String admin = result.getString("admin");
                    JSONArray userlist = result.getJSONArray("users");

                    TextView ad = (TextView)findViewById(R.id.sessionadmin);

                    ad.setText(admin);

                    ListView glist = (ListView)findViewById(R.id.teilnehmerlist);

                    ArrayList<String> ulist = new ArrayList<String>();
                    for(int i = 0; i < userlist.length(); i++){
                        ulist.add(userlist.getJSONObject(i).getString("name"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, ulist){

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };

                    glist.setAdapter(adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(JSONArray result) {
                Log.i("Join", result.toString());
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
