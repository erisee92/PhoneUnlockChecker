package com.talk2machines.phoneunlockchecker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    public final static String SERVER_ADDR = "eboege.no-ip.org:3000";
    String SENDER_ID = "142896340334";

    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;
    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("PUC", 0);
        context = getApplicationContext();
/*        if(!prefs.getString("REG_FROM","").isEmpty()){
            Fragment user = new UserFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, user);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }else  */
        if(!prefs.getString("REG_ID", "").isEmpty()){
            Fragment reg = new LoginFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, reg);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();

        }else if(checkPlayServices()){

            new Register().execute();

        }else{
            Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private class Register extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    regid = gcm.register(SENDER_ID);
                    Log.e("RegId",regid);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_ID", regid);
                    edit.commit();
                }

                return  regid;

            } catch (IOException ex) {
                Log.e("Error", ex.getMessage());
                return "Fails";

            }
        }
        @Override
        protected void onPostExecute(String json) {
            Fragment reg = new LoginFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, reg);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
