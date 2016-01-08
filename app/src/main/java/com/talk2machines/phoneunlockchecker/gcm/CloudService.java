package com.talk2machines.phoneunlockchecker.gcm;

/**
 * Created by Erik on 03.11.2015.
 */
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.talk2machines.phoneunlockchecker.R;
import com.talk2machines.phoneunlockchecker.SessionActivity;
import com.talk2machines.phoneunlockchecker.api.Session;


public class CloudService extends IntentService {

    SharedPreferences prefs;
    NotificationCompat.Builder notification;
    NotificationManager manager;


    public CloudService() {
        super("CloudService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("GCMService", "Received: ");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        prefs = getSharedPreferences("PUC", 0);


        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("GCM","Error");

            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("GCM","Error");

            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {


                    sendNotification(extras.getString("msgType"), extras.getString("body"));

                Log.i("TAG", "Received: " + extras.getString("body"));
            }
        }
        CloudReceiver.completeWakefulIntent(intent);
    }




    private void sendNotification(String msgType,String body) {

        Bundle args = new Bundle();
        args.putString("msgType", msgType);
        args.putString("body", body);
        Intent chat = new Intent(this, SessionActivity.class);
        chat.putExtra("INFO", args);
        notification = new NotificationCompat.Builder(this);
        notification.setContentText(body);
        notification.setContentTitle(msgType);
        notification.setTicker("New Message !");
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setDefaults(Notification.DEFAULT_VIBRATE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 1000,
                chat, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setContentIntent(contentIntent);
        notification.setAutoCancel(true);
        manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }


}
