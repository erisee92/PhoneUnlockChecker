package com.talk2machines.phoneunlockchecker.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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

    public JSONObject login() {
        // try parse the string to a JSON object
        json += "{name:"+name+",username:"+username+",reg_id:"+reg_id+"}";
        try {
            jObj = new JSONObject(json);
            Log.i("API",jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;
    }
}
