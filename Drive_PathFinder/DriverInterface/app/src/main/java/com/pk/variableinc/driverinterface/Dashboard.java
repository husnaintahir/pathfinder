package com.pk.variableinc.driverinterface;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Husnain on 5/2/2016.
 */
public class Dashboard extends Activity {
    TextView onLineStatus, name;
    Button logoutBtn, acceptJobBtn;

    SharedPreferences sp;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    private GetLocation location;
    float latitude = 0;
    float longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);
        Log.d("inbox", "Welcome Dashboard");

        sp = Dashboard.this.getSharedPreferences(SPNames.spName, MODE_PRIVATE);

        name = (TextView) findViewById(R.id.name);
        onLineStatus = (TextView) findViewById(R.id.onLineStatus);

        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        acceptJobBtn = (Button) findViewById(R.id.acceptJobBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoginCreds("rm");
            }
        });
        acceptJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptJobBtn.setEnabled(false);
                acceptOffer();
            }
        });


        mHandler = new Handler();
        //startRepeatingTask();
    }


    private void acceptOffer() {
        if (WebService.isOnline(Dashboard.this)) {
            final WebService ws = new WebService("hireDriver.php");
            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... params) {
                    Map<String, String> map = new HashMap<String, String>();

                    map.put("driverAccepting", "");
                    map.put("driverName", sp.getString(SPNames.username_key, ""));

                    try {
                        return ws.makeHTTPRequest(map, "POST");
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
                                acceptJobBtn.setEnabled(false);
                            } else {
                                acceptJobBtn.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.execute();
        }
    }

    private void findJob() {
        if (WebService.isOnline(Dashboard.this)) {
            final WebService ws = new WebService("hireDriver.php");
            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... params) {
                    Map<String, String> map = new HashMap<>();

                    map.put("findJob", "");
                    map.put("driverName", sp.getString(SPNames.username_key, ""));

                    try {
                        return ws.makeHTTPRequest(map, "POST");
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
                                Log.d("inbox", jsonObject.getString("data"));
                                JSONObject jObj = new JSONObject(jsonObject.getString("data"));
                                acceptJobBtn.setEnabled(true);
                                acceptJobBtn.setVisibility(View.VISIBLE);
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.execute();
        }
    }


    private void checkLoginCreds(String str) {

        if (str.equals("chk")) {
            if (sp.getString(SPNames.username_key, "").equals("") || sp.getString(SPNames.DID_key, "").equals("") || sp.getString(SPNames.full_name_key, "").equals("")) {
                startActivity(new Intent(Dashboard.this, Login.class));
                Dashboard.this.finish();
            } else {
                name.setText(sp.getString(SPNames.full_name_key, ""));
                onLineStatus.setText("Online");
                onLineStatus.setTextColor(Color.BLACK);
                onLineStatus.setBackgroundColor(Color.rgb(2, 204, 32));

                changeOnlineStatusInDB(1);
            }
        } else if (str.equals("rm")) {
            changeOnlineStatusInDB(0);

            sp.edit().remove(SPNames.username_key).commit();
            sp.edit().remove(SPNames.DID_key).commit();
            sp.edit().remove(SPNames.full_name_key).commit();


            startActivity(new Intent(Dashboard.this, Login.class));
            Dashboard.this.finish();

        }
    }

    private void getLocation() {
        location = new GetLocation(Dashboard.this);
        if (location.getLatLng() != null) {
            latitude = location.getLat();
            longitude = location.getLong();

            location.stopRequesting();

        }
    }

    private void changeOnlineStatusInDB(final int onlineStatus) {
        getLocation();
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {

                WebService ws = new WebService("changeOnlineStatus.php");

                Map<String, String> map = new HashMap<String, String>();

                map.put("did", sp.getString(SPNames.DID_key, ""));
                map.put("status", onlineStatus + "");
                if (onlineStatus == 1) {
                    if (latitude != 0 && longitude != 0) {
                        map.put("latlng", latitude + ":" + longitude);
                    } else {
                        map.put("latlng", 0.0 + "");
                    }
                } else if (onlineStatus == 0) {
                    map.put("latlng", 0.0 + "");
                }
                try {
                    ws.makeHTTPRequest(map, "POST");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void checkOnlineStatus() {
        if (WebService.isOnline(Dashboard.this)) {
            onLineStatus.setText("Online");
            onLineStatus.setTextColor(Color.BLACK);
            onLineStatus.setBackgroundColor(Color.rgb(2, 204, 32));

            logoutBtn.setEnabled(true);
        } else {
            onLineStatus.setText("Offline");
            onLineStatus.setTextColor(Color.WHITE);
            onLineStatus.setBackgroundColor(Color.rgb(233, 16, 23));
            logoutBtn.setEnabled(false);
        }
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkOnlineStatus();
                findJob();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };


    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    protected void onStart() {
        checkLoginCreds("chk");
        startRepeatingTask();
        super.onStart();

    }

    @Override
    protected void onPause() {
        changeOnlineStatusInDB(0);
        stopRepeatingTask();
        super.onPause();
    }
}
