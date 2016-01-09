package com.talk2machines.phoneunlockchecker;


import android.app.ProgressDialog;
import android.content.Intent;
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

public class JoinActivity extends AppCompatActivity {

    ProgressDialog progress;
    SharedPreferences prefs;
    String sessionid, response;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        final ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progress = new ProgressDialog(this);
        progress.setMessage(getApplicationContext().getResources().getString(R.string.loading));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        prefs = getSharedPreferences("PUC", 0);

        Bundle bunde = this.getIntent().getExtras();
        final String s_id = bunde.getString("s_id");

        Button joinBtn = (Button) findViewById(R.id.joinbutton);
        final TextView textPW = (TextView) findViewById(R.id.loginpassword);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                Session.login(prefs.getString("REG_ID", ""),s_id, prefs.getString("LOG_NAME", ""), textPW.getText().toString(), getApplicationContext(), new Session.VolleyCallback2() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.i("Login", result.toString());


                        try {
                            response = result.getString("response");
                            //test ob erfolgreich, wenn ja, speichern sessionid in sharePreferences, und leitet zu SessionActivity
                            if(response.equals("Updated Sucessfully") ){
                                prefs = getSharedPreferences("PUC", 0);
                                SharedPreferences.Editor edit = prefs.edit();
                                edit.putString("SESSION_ID", s_id);
                                edit.apply();
                                edit.putBoolean("ADMIN",false);
                                edit.commit();
                                progress.hide();
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(JoinActivity.this, SessionActivity.class);
                                startActivity(intent);
                            } else if(response.equals("Wrong Password")){
                                progress.hide();
                                Toast.makeText(getApplicationContext(), R.string.wrongpw, Toast.LENGTH_SHORT).show();
                            } else {
                                progress.hide();
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.hide();
                    }

                    @Override
                    public void onError(JSONObject result) {
                        Log.i("Login", result.toString());
                        try {
                            response = result.getString("response");
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.hide();
                    }
                });
            }
        });

        Session.getSession(s_id, getApplicationContext(), new Session.VolleyCallback(){

            @Override
            public void onSuccess(JSONObject result) {
                Log.i("Join", result.toString());
                try {
                    String name = result.getString("name");
                    String admin = result.getString("admin");
                    JSONArray userlist = result.getJSONArray("users");

                    TextView gname = (TextView)findViewById(R.id.gruppename);
                    TextView ad = (TextView)findViewById(R.id.admin);

                    gname.setText(name);
                    ad.setText(admin);

                    ListView glist = (ListView)findViewById(R.id.gruppelist);

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

