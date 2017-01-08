package com.map.pathfinder;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FindDriver extends android.support.v4.app.FragmentActivity {

    private GoogleMap googleMap;
    String SP_HIRE_KEY = "iHired", SP_HIRE_VALUE = "yes";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_driver);
        sp = getSharedPreferences("routes", MODE_PRIVATE);
        setUpMapIfNeeded();

        getOnlineDriversList();

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String username = marker.getTitle();
                String getUsername[] = username.split(":");
//                Log.d("inbox", username);
//                Log.d("inbox", getUsername[0]);
//                LatLng cLoc = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
                int openPage = 0;
                if (sp.getString(SP_HIRE_KEY, "").equals(getUsername[0])) {
                    openPage = 1;
//                    Log.d("inbox", "i Hired");
                }
                if (username.indexOf("hired") > 0 && openPage == 0) {
                    Toast.makeText(FindDriver.this, "Driver Already Hired .. ", Toast.LENGTH_SHORT).show();
                } else {
                    GetLocation loc = new GetLocation(FindDriver.this);
                    if (loc.getLatLng() != null) {


                        sp.edit().putString("driverInfo", getUsername[0]).commit();
                        sp.edit().putString("myLat", loc.getLat() + "").commit();
                        sp.edit().putString("myLng", loc.getLong() + "").commit();
                        loc.stopRequesting();
                        startActivity(new Intent(FindDriver.this, DriverInfo.class));
                    }
                }
            }
        });

    }

    public void clearMap() {
        googleMap.clear();
    }

    private void setUpMapIfNeeded() {

        if (googleMap == null) {

            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            if (googleMap != null) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.setMyLocationEnabled(true);
            }
        }
    }

    private void makeMarker(LatLng point, String username, String isHired) {

        MarkerOptions selectedLocation = new MarkerOptions()
                .position(point);
        if (isHired.equals("2")) {
            selectedLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.src));
            selectedLocation.title(username + ": already hired");
        } else {
            selectedLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));
            selectedLocation.title(username + ": available");
        }
        googleMap.addMarker(selectedLocation);

    }

    private void getOnlineDriversList() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {

                Map<String, String> map = new HashMap<>();
                map.put("findAllDrivers", "");
                WebService ws = new WebService("findDrivers.php");
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
                        JSONObject obj = new JSONObject(s);
                        if (obj.getString("result").equals("1") && obj.getString("msg").equals("1")) {
                            Log.d("inbox", obj.getString("data"));
                            JSONArray jsonArray = new JSONArray(obj.getString("data"));
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String location = jsonObject.getString("location");
                                    String username = jsonObject.getString("username");
                                    String isHired = jsonObject.getString("isHired");
                                    String[] locArr = location.split(":");
                                    String lat = locArr[0];
                                    String lng = locArr[1];
                                    LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                    makeMarker(latLng, username, isHired);
                                }
                            } else {
                                Toast.makeText(FindDriver.this, "No Drivers Online Yet", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(FindDriver.this, "NoInformation Yet", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
