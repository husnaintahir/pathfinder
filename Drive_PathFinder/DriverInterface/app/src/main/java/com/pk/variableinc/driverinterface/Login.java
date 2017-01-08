package com.pk.variableinc.driverinterface;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Husnain on 5/2/2016.
 */
public class Login extends Activity {

    Button loginBtn;
    EditText username, password;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Log.d("inbox", "Welcome Login");

        loginBtn = (Button) findViewById(R.id.loginBtn);

        pb = (ProgressBar) findViewById(R.id.pb);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
    }

    private void validateForm() {

        String u_name = username.getText().toString().trim();
        String pass = password.getText().toString();

        if (u_name.length() < 3) {
            Toast.makeText(Login.this, "Username must be greater than 3", Toast.LENGTH_SHORT).show();
            username.requestFocus();
        } else if (pass.length() < 4) {
            Toast.makeText(Login.this, "Password Must be greater than 4 Charachters", Toast.LENGTH_SHORT).show();
            password.requestFocus();
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("username", u_name);
            params.put("password", pass);
            makeHTTPCall(params);
        }
    }

    private void makeHTTPCall(final Map<String, String> param) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {

                WebService ws = new WebService("login.php");

                try {
                    return ws.makeHTTPRequest(param, "POST");
                } catch (Exception e) {
                    Log.e("inbox", "Exception : " + e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                pb.setVisibility(View.GONE);
                if (s != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("result").equals("1")) {
                            if (jsonObject.getString("msg").equals("1")) {
                                String data = jsonObject.getString("data");
                                JSONObject dataObject = new JSONObject(data);
                                String username = dataObject.getString("username");
                                String DID = dataObject.getString("DID");
                                String full_name = dataObject.getString("full_name");

                                SharedPreferences sp = Login.this.getSharedPreferences(SPNames.spName, MODE_PRIVATE);

                                sp.edit().putString(SPNames.username_key, username).commit();
                                sp.edit().putString(SPNames.DID_key, DID).commit();
                                sp.edit().putString(SPNames.full_name_key, full_name).commit();

                                startActivity(new Intent(Login.this, Dashboard.class));
                                Login.this.finish();

                                Log.d("inbox", data);
                            } else {
                                Toast.makeText(Login.this, "Wrong username/password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("inbox", "Params Missing");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("inbox", "Exception : " + e.getMessage());
                    }
                } else {
                    Toast.makeText(Login.this, "An Error occurred! Try later", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    protected void onStart() {

        SharedPreferences sp = Login.this.getSharedPreferences(SPNames.spName, MODE_PRIVATE);

        if (!sp.getString(SPNames.username_key, "").equals("") && !sp.getString(SPNames.DID_key, "").equals("") && !sp.getString(SPNames.full_name_key, "").equals("")) {
            startActivity(new Intent(Login.this, Dashboard.class));
            Login.this.finish();
        }
        super.onStart();
    }

    public void signUp(View v) {
        startActivity(new Intent(Login.this, SignUp.class));
    }
}
