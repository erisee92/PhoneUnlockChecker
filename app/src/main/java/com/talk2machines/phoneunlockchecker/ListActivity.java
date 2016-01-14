package com.talk2machines.phoneunlockchecker;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.talk2machines.phoneunlockchecker.api.Session;
import com.talk2machines.phoneunlockchecker.api.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by lin_n on 26.11.2015.
 */
public class ListActivity extends AppCompatActivity {
    SharedPreferences prefs;

    ListView gl;
    FloatingActionButton fab;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(R.string.join_description);
        prefs = getSharedPreferences("PUC", 0);

        gl = (ListView) findViewById(R.id.gruppelist);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_to_reload);

        getContent();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                     @Override
                                                     public void onRefresh() {
                                                         getContent();
                                                     }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ListActivity.this, CreatGroupActivity.class);
                startActivity(intent);
            }
        });


        gl.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView sessionIdTV = (TextView)view.findViewById(R.id.s_id);
                TextView sessionState = (TextView)view.findViewById(R.id.s_state);

                if (sessionIdTV.getText().toString().equals(prefs.getString("SESSION_ID",""))){
                    Intent intent = new Intent();
                    intent.setClass(ListActivity.this, SessionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("s_id",sessionIdTV.getText().toString());
                    bundle.putString("s_state",sessionState.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    if (prefs.getString("SESSION_ID","").equals("") || prefs.getString("SESSION_ID","").isEmpty()){
                        Intent intent = new Intent();
                        intent.setClass(ListActivity.this, JoinActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("s_id",sessionIdTV.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else Toast.makeText(getApplicationContext(), "You are already in a group", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getContent() {

        Session.list(getApplicationContext(), new Session.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                Log.i("List",result.toString());
                try {
                    if(result.getJSONObject(0).getString("name")!= null && !result.getJSONObject(0).getString("name").isEmpty()){
                        ArrayList<Session> arrayOfSessions = new ArrayList<Session>();

                        SessionAdapter adapter = new SessionAdapter(getApplicationContext(), arrayOfSessions);
                        gl.setAdapter(adapter);

                        ArrayList<Session> newSessions = Session.fromJson(result);
                        adapter.addAll(newSessions);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    gl.setAdapter(null);
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onSuccess(JSONObject result) {
                Log.i("List","something strange happened");
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                Logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Logout() {
        String user_id = prefs.getString("LOG_ID", "");
        JSONObject logout = User.logout(user_id, getApplicationContext(), new User.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.i("Logout", result.toString());
                try {
                    String response = result.getString("response");
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.remove("LOG_ID");
                    edit.apply();
                    edit.remove("LOG_NAME");
                    edit.apply();
                    edit.remove("LOG_USERNAME");
                    edit.apply();
                    edit.remove("SESSION_ID");
                    edit.apply();
                    edit.putBoolean("ADMIN",false);
                    edit.commit();

                    Intent intent = new Intent();
                    intent.setClass(ListActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(JSONObject result) {
                Log.i("Logout", result.toString());
                try {
                    String response = result.getString("response");
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
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

class SessionAdapter extends ArrayAdapter<Session> {

    public SessionAdapter(Context context, ArrayList<Session> sessions) {
        super(context, 0, sessions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Session session = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_session, parent, false);
        }
        TextView sName = (TextView) convertView.findViewById(R.id.s_name);
        TextView sAdmin = (TextView) convertView.findViewById(R.id.s_admin);
        TextView sId = (TextView) convertView.findViewById(R.id.s_id);
        TextView sState = (TextView) convertView.findViewById(R.id.s_state);

        sName.setText(session.name);
        sAdmin.setText(session.admin);
        sId.setText(session.id);
        sState.setText(session.state);

        return convertView;
    }

}