package com.talk2machines.phoneunlockchecker;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.talk2machines.phoneunlockchecker.api.Session;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lin_n on 19.11.2015.
 */
public class CreatGroupActivity extends AppCompatActivity{

    ProgressDialog progress;
    SharedPreferences prefs;
    String response, sessionid;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creatgroup);

        final ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progress = new ProgressDialog(this);
        progress.setMessage(getApplicationContext().getResources().getString(R.string.loading));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        final EditText new_group_name = (EditText) findViewById(R.id.new_group_name);
        final EditText group_pa = (EditText) findViewById(R.id.group_password);
        final Button new_group_create = (Button) findViewById(R.id.newGroup);

        prefs = getSharedPreferences("PUC", 0);

        new_group_create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewGroup(new_group_name, group_pa);
            }
        });


    }


    private void createNewGroup(EditText new_group_name, EditText group_pa) {

        // test, ob die Gruppename und password schon eingetippen sind.
        if (new_group_name.getText().toString().equals("") || group_pa.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.new_group_error, Toast.LENGTH_LONG).show();
        } else {
            progress.show();

            Session newGroup = new Session(new_group_name.getText().toString().trim(),group_pa.getText().toString().trim(), prefs.getString("LOG_NAME",""), prefs.getString("LOG_USERNAME",""), prefs.getString("REG_ID",""));

            newGroup.createNewGroup(getApplicationContext(), new Session.VolleyCallback2() {
                @Override
                public void onSuccess(JSONObject result) {

                    Log.i("CreatGroup", result.toString());
                    try {
                        response = result.getString("response");
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        sessionid = result.getString("id");
                        //test ob ein sessionid zurückbekommen, wenn ja, speichern sessionid in sharePreferences, und leitet zu SessionActivity
                        if(sessionid !=  null ){
                            Log.i("CreatGroup", sessionid);
                            prefs = getSharedPreferences("PUC", 0);
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putBoolean("ADMIN", true);
                            edit.apply();
                            edit.putString("SESSION_STATE", "false");
                            edit.apply();
                            edit.putString("SESSION_ID", sessionid);
                            edit.commit();

                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("s_id",sessionid);
                            bundle.putString("s_state","false");
                            intent.putExtras(bundle);
                            intent.setClass(CreatGroupActivity.this, SessionActivity.class);
                            startActivity(intent);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progress.hide();

                }

                @Override
                public void onError(JSONObject result) {

                }
            });

        }

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
