package com.talk2machines.phoneunlockchecker.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Erik on 10.12.2015.
 */
public class Session {

    static JSONArray jObj = null;

    public String name;
    public String admin;

    public Session() {

    }

    public Session(JSONObject object){
        try {
            this.name = object.getString("name");
            this.admin = object.getString("admin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static JSONArray list(Context mContext, final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="http://test-erik-boege.c9.io/sessions";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        try {
                            JSONArray JObj = new JSONArray(response);
                            callback.onSuccess(JObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("User", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return jObj;
    }

    public static ArrayList<Session> fromJson(JSONArray jsonObjects) {
        ArrayList<Session> sessions = new ArrayList<Session>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                sessions.add(new Session(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sessions;

    }


    public interface VolleyCallback{
        void onSuccess(JSONArray result);
    }

}
