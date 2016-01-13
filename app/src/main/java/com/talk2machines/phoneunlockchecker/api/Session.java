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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erik on 10.12.2015.
 */
public class Session {

    static JSONArray jArr = null;
    static JSONObject jObj = null;

    public String name;
    public String admin;
    public String id;
    public String password;
    public String reg_id;

    public Session() {

    }

    //Struktur für new group
    public Session(String groupname,String Password, String adminname, String reg_id){
        this.name = groupname;
        this.admin = adminname;
        this.password = Password;
        this.reg_id = reg_id;
        Log.i("API",name+" "+admin+" "+password +" "+reg_id);

    }

    public Session(JSONObject object){
        try {
            this.name = object.getString("name");
            this.admin = object.getString("admin");
            this.id = object.getString("id");
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
                            JSONArray jArr = new JSONArray(response);
                            callback.onSuccess(jArr);
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
        return jArr;
    }

    public static JSONObject getSession(String id, Context mContext, final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="http://test-erik-boege.c9.io/sessions/"+id;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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


    public JSONObject createNewGroup(Context mContext, final VolleyCallback2 callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="http://test-erik-boege.c9.io/sessions";

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
                params.put("password", password);
                params.put("admin",admin);
                params.put("reg_id",reg_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return jObj;
    }

    public static JSONObject login(final String reg_id,final String id, final String name, final String pw, Context mContext, final VolleyCallback2 callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="http://test-erik-boege.c9.io/sessions/"+id+"/users";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
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
                params.put("password", pw);
                params.put("reg_id", reg_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return jObj;
    }


    public static JSONObject changeStateOfSession(final String session_id,final String state, Context mContext, final VolleyCallback2 callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="http://test-erik-boege.c9.io/sessions/"+session_id+"/state";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
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
                params.put("started",state);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return jObj;
    }


    public interface VolleyCallback{
        void onSuccess(JSONArray result);
        void onSuccess(JSONObject result);
    }

    public interface VolleyCallback2 {
        void onSuccess(JSONObject result);
        void onError(JSONObject result);
    }

}
