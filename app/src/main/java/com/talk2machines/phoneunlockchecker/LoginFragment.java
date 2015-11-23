package com.talk2machines.phoneunlockchecker;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.talk2machines.phoneunlockchecker.api.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Erik on 23.11.2015.
 */
public class LoginFragment extends Fragment {
    SharedPreferences prefs;
    EditText name, username;
    Button login;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment,container,false);
        prefs = getActivity().getSharedPreferences("PUC",0);

        name = (EditText) view.findViewById(R.id.name);
        username = (EditText) view.findViewById(R.id.username);
        login = (Button) view.findViewById(R.id.log_btn);
        progress = new ProgressDialog(getActivity());
        progress.setMessage(getText(R.string.registering_message));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("REG_FROM",username.getText().toString());
                editor.putString("FROM_NAME",name.getText().toString());
                editor.commit();
                User user = new User(name.getText().toString(),username.getText().toString(),prefs.getString("REG_ID",""));
                JSONObject test = user.login();
                try{
                    Log.i("Login",test.getString("name"));
                    if(Objects.equals(test.getString("name"), "erik")){
                        progress.hide();
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

            }
        });

        return view;
    }
}
