package com.talk2machines.phoneunlockchecker.api;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.talk2machines.phoneunlockchecker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lin_n on 14.01.2016.
 */
public class SessionUser {

    public String name, username, unlocks;
    static JSONObject jObj = null;

    public SessionUser(JSONObject object){
        try {
            this.name = object.getString("name");
            this.username = object.getString("username");
            this.unlocks = object.getString("unlocks");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<SessionUser> fromJson(JSONArray jsonObjects) {
        ArrayList<SessionUser> sessionUsers = new ArrayList<SessionUser>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                sessionUsers.add(new SessionUser(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sessionUsers;

    }

    public static JSONObject changeUnlocks(String session_id, String username, final String unlocks, Context mContext, final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="http://test-erik-boege.c9.io/sessions/"+session_id+"/users/"+username;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);

                        try {
                            JSONObject JObj = new JSONObject(response);
                            callback.onSuccess(JObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Session", error.toString());

                try {
                    JSONObject JObj = new JSONObject("{\"response\":\"Server Error\"}");
                    callback.onError(JObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("unlocks",unlocks);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return jObj;
    }


    public static JSONObject logout(String session_id, String username, Context mContext, final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="http://test-erik-boege.c9.io/sessions/"+session_id+"/users/"+username;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);

                        try {
                            JSONObject JObj = new JSONObject(response);
                            callback.onSuccess(JObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Session", error.toString());

                try {
                    JSONObject JObj = new JSONObject("{\"response\":\"Server Error\"}");
                    callback.onError(JObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return jObj;
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject result);
        void onError(JSONObject result);
    }



}
