package com.pk.variableinc.driverinterface;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Button signUpBtn;
    EditText username, fullname, password, repass, v_name, v_number, phone, address;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signUpBtn = (Button) findViewById(R.id.signupBtn);

        pb = (ProgressBar) findViewById(R.id.pb);

        username = (EditText) findViewById(R.id.username);
        fullname = (EditText) findViewById(R.id.fullname);
        password = (EditText) findViewById(R.id.password);
        repass = (EditText) findViewById(R.id.repass);
        v_name = (EditText) findViewById(R.id.v_name);
        v_number = (EditText) findViewById(R.id.v_num);
        phone = (EditText) findViewById(R.id.phone_num);
        address = (EditText) findViewById(R.id.addr);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

    }

    private void validateForm() {
        String u_name = username.getText().toString().trim();
        String f_name = fullname.getText().toString().trim();
        String pass = password.getText().toString();
        String re_pass = repass.getText().toString();
        String veh_name = v_name.getText().toString().trim();
        String veh_num = v_number.getText().toString().trim();
        String u_phone = phone.getText().toString().trim();
        String u_addr = address.getText().toString().trim();

        if (u_name.length() < 3) {
            Toast.makeText(SignUp.this, "Username must be greater than 3", Toast.LENGTH_SHORT).show();
            username.requestFocus();
        } else if (f_name.length() < 3) {
            Toast.makeText(SignUp.this, "Invalid Full Name", Toast.LENGTH_SHORT).show();
            fullname.requestFocus();
        } else if (pass.length() < 4) {
            Toast.makeText(SignUp.this, "Password Must be greater than 4 Charachters", Toast.LENGTH_SHORT).show();
            password.requestFocus();
        } else if (veh_name.length() < 4) {
            v_name.requestFocus();
            Toast.makeText(SignUp.this, "Invalid Vehicle Name", Toast.LENGTH_SHORT).show();
        } else if (veh_num.length() < 3) {
            Toast.makeText(SignUp.this, "Invalid Vehicle Number", Toast.LENGTH_SHORT).show();
            v_number.requestFocus();
        } else if (u_phone.length() < 5) {
            Toast.makeText(SignUp.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
        } else if (u_addr.length() < 7) {
            Toast.makeText(SignUp.this, "Invalid Address", Toast.LENGTH_SHORT).show();
            address.requestFocus();
        } else if (!pass.equals(re_pass)) {
            Toast.makeText(SignUp.this, "Password Fields do not match", Toast.LENGTH_SHORT).show();
            password.requestFocus();
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("username", u_name);
            params.put("fullname", f_name);
            params.put("password", pass);
            params.put("vehicle_name", veh_name);
            params.put("vehicle_number", veh_num);
            params.put("phone", u_phone);
            params.put("address", u_addr);
            makeHTTPCall(params);
        }

    }

    private void makeHTTPCall(final Map<String, String> param) {
        pb.setVisibility(View.VISIBLE);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                WebService ws = new WebService("signup.php");
                try {

                    return ws.makeHTTPRequest(param, "POST");

                } catch (Exception e) {
                    Log.e("inbox", "Exception : " + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                pb.setVisibility(View.GONE);
                super.onPostExecute(s);
            }
        }.execute();
    }
}
