
package com.map.pathfinder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverInfo extends Activity {

    String SP_HIRE_KEY = "iHired", SP_HIRE_VALUE = "yes";

    TextView name, contactPhone, addr, veh_name, veh_number, distance;

    ProgressBar pb;

    SharedPreferences sp;

    Button makeCall;

    String callNumber = "";

    ToggleButton hireBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.driver_info);

        sp = getSharedPreferences("routes", MODE_PRIVATE);

        name = (TextView) findViewById(R.id.name);
        contactPhone = (TextView) findViewById(R.id.phone);
        addr = (TextView) findViewById(R.id.address);
        veh_name = (TextView) findViewById(R.id.v_name);
        veh_number = (TextView) findViewById(R.id.v_number);
        distance = (TextView) findViewById(R.id.distance);

        makeCall = (Button) findViewById(R.id.makeCall);

        pb = (ProgressBar) findViewById(R.id.pb);

        hireBtn = (ToggleButton) findViewById(R.id.hireBtn);

        if (sp.getString(SP_HIRE_KEY, "456").equals(sp.getString("driverInfo", "123"))) {
            Log.d("inbox", "hired by me already");
            hireBtn.setChecked(true);
        }

        getDriverInfo();

        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("inbox", "Number : " + callNumber);
                if (!callNumber.equals("")) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + callNumber));
                    Toast.makeText(DriverInfo.this, "Calling ...", Toast.LENGTH_SHORT).show();
                    startActivity(callIntent);
                } else {
                    Toast.makeText(DriverInfo.this, "Number not defined", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hireBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // change status to hire from user side
                    hireUnhireDriver(1);
                    SP_HIRE_VALUE = sp.getString("driverInfo", "");
                    sp.edit().putString(SP_HIRE_KEY, SP_HIRE_VALUE).commit();
                } else {
                    // change status to unhire from user side
                    hireUnhireDriver(0);
                    sp.edit().remove(SP_HIRE_KEY);
                }
                hireBtn.setEnabled(false);
                new CountDownTimer(1000 * 10, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        hireBtn.setEnabled(true);
                    }
                }.start();
            }
        });
    }

    private void hireUnhireDriver(final int status) {
        if (WebService.isOnline(DriverInfo.this)) {
            final WebService ws = new WebService("hireDriver.php");
            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... params) {
                    Map<String, String> map = new HashMap<String, String>();
                    if (status == 1) {
                        map.put("userHiring", "");
                    } else if (status == 0) {
                        map.put("userUnHiring", "");
                    }
                    map.put("driverName", sp.getString("driverInfo", ""));

                    try {
                        return ws.makeHTTPRequest(map, "POST");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }
    }

    private void getDriverInfo() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {

                Map<String, String> map = new HashMap<String, String>();

                map.put("findDriverInfo", "123");
                map.put("username", sp.getString("driverInfo", ""));
//                map.put("username", "tahir");

                try {
                    WebService ws = new WebService("getDriverInfo.php");
                    String response = ws.makeHTTPRequest(map, "POST");
                    Log.d("inbox", "response : " + response);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    try {

                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("result").equals("1") && jsonObject.getString("msg").equals("1")) {
                            JSONObject obj = new JSONObject(jsonObject.getString("data"));

                            String fullname;
                            String phone;
                            String address;
                            String v_name;
                            String v_number;
                            String location;

                            fullname = obj.getString("full_name");
                            phone = obj.getString("phone_num");
                            address = obj.getString("address");
                            v_name = obj.getString("vehicle_name");
                            v_number = obj.getString("vehicle_num");
                            location = obj.getString("location");

                            callNumber = phone;

                            String[] locArr = location.split(":");
                            String lat = locArr[0];
                            String lng = locArr[1];

                            double totalDistance = WebService.calculateDistance(Double.parseDouble(sp.getString("myLat", "")), Double.parseDouble(sp.getString("myLng", "")), Double.parseDouble(lat), Double.parseDouble(lng));

                            name.setText(fullname);
                            contactPhone.setText(phone);
                            addr.setText(address);
                            veh_name.setText(v_name);
                            veh_number.setText(v_number);
                            distance.setText(totalDistance + "");

                            Log.d("inbox", "responseh : " + s);

                            pb.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(DriverInfo.this, "An Error Occurred Try Later", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.i("inbox", e.getMessage() + "");
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
