package com.talk2machines.phoneunlockchecker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class UnlockReceiver extends BroadcastReceiver {

    SharedPreferences prefs;

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

        }
    }
}
