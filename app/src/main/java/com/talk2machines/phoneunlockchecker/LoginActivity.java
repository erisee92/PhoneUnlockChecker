package com.talk2machines.phoneunlockchecker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.talk2machines.phoneunlockchecker.api.CustomVolleyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements Response.Listener,Response.ErrorListener {

        public static final String REQUEST_TAG = "LoginActivity";
        private RequestQueue mQueue;
        ProgressDialog progress;
        SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        final EditText ln = (EditText) findViewById(R.id.loginName);
        final EditText lu = (EditText) findViewById(R.id.loginUsername);
        final Button lb = (Button) findViewById(R.id.loginbutton);


        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        prefs = getSharedPreferences("PUC", 0);
        final String reg_id=prefs.getString("REG_ID", "");


        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://test-erik-boege.c9.io/users";
        final StringRequest stringPost = new StringRequest(Request.Method.POST,url,this,this){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", ln.getText().toString().trim());
                params.put("username", lu.getText().toString().trim());
                params.put("reg_id",reg_id);
                return params;
            }
        };

        lb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mQueue.add(stringPost);
                progress.show();
            }
        });


    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(REQUEST_TAG, error.toString());
    }

    @Override
    public void onResponse(Object response) {
        Log.i("Response",response.toString());

        try {
            JSONObject JObj = new JSONObject(response.toString());
            String resp = JObj.getString("response");
            String regid = JObj.getString("id");
            Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("LOG_ID", regid);
            edit.commit();
            if (resp.equals("Sucessfully Registered")){
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        progress.hide();
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
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

}
