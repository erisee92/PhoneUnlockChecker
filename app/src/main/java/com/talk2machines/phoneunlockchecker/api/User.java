package com.talk2machines.phoneunlockchecker.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erik on 23.11.2015.
 */
public class User {
    static JSONObject jObj = null;
    static String json = "";
    String name, username, reg_id;


    public User(String na,String una, String rId){
        this.name = na;
        this.username = una;
        this.reg_id = rId;
        Log.i("API",name+" "+username+" "+reg_id);
    }

    public JSONObject login(Context mContext, final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="http://test-erik-boege.c9.io/users";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);

                        try {
                            JSONObject JObj = new JSONObject(response);
                            //String resp = JObj.getString("response");
                            callback.onSuccess(JObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("User", error.toString());

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
                params.put("name", name);
                params.put("username", username);
                params.put("reg_id",reg_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return jObj;
    }

    public static JSONObject logout(String user_id, Context mContext, final VolleyCallback callback) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="http://test-erik-boege.c9.io/users/"+user_id;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);

                        try {
                            JSONObject JObj = new JSONObject(response);
                            //String resp = JObj.getString("response");
                            callback.onSuccess(JObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("User", error.toString());

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

    public interface VolleyCallback{
        void onSuccess(JSONObject result);
        void onError(JSONObject result);
    }
}
