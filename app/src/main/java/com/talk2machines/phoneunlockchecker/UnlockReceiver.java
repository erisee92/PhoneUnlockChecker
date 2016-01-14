package com.talk2machines.phoneunlockchecker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.talk2machines.phoneunlockchecker.api.SessionUser;

import org.json.JSONException;
import org.json.JSONObject;

public class UnlockReceiver extends BroadcastReceiver {

    SharedPreferences prefs;
    String response;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.i("unlock_rec","User Presence Broadcast received");

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            prefs = ctx.getSharedPreferences("PUC", 0);
            Log.i("unlock_rec", "User unlocked phone");
            int unlocks;
            unlocks = prefs.getInt("NUM_UNLOCKS", 0);
            unlocks++;
            Log.i("unlock_rec", Integer.toString(unlocks));

            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("NUM_UNLOCKS", unlocks);
            edit.commit();

            SessionUser.changeUnlocks(prefs.getString("SESSION_ID", ""), prefs.getString("LOG_USERNAME", ""), Integer.toString(unlocks), ctx, new SessionUser.VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        response = result.getString("response");
                        Log.i("unlock_rec", response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(JSONObject result) {
                    try {
                        response = result.getString("response");
                        Log.i("unlock_rec", response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
