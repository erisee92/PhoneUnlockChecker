package com.talk2machines.phoneunlockchecker.gcm;

/**
 * Created by Erik on 03.11.2015.
 */
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class CloudReceiver  extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("GCMReceiver", "Received");
        Bundle extras = intent.getExtras();
        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("msgType", extras.getString("msgType"));
        msgrcv.putExtra("msgHead", extras.getString("msgHead"));
        msgrcv.putExtra("msgBody", extras.getString("msgBody"));

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        ComponentName comp = new ComponentName(context.getPackageName(),CloudService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}