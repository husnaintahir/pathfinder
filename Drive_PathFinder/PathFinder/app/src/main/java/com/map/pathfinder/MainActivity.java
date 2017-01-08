package com.map.pathfinder;


import java.util.List;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends android.support.v4.app.FragmentActivity
        implements OnClickListener, OnInfoWindowClickListener,
        GMapV2Direction.DirecitonReceivedListener {


    private GoogleMap googleMap;
    private Button btnDirection;

    LatLng startPosition;
    String startPositionTitle;

    LatLng destinationPosition;
    String destinationPositionTitle;

    LatLng newSelectedLocation, myCurrentLocation;

    ToggleButton tbMode;

    TextView errorInResults, timeDistance, locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startPosition = new LatLng(33.5984, 73.0441);
        startPositionTitle = "Source";

        destinationPosition = new LatLng(33.7294, 73.0931);
        destinationPositionTitle = "Destination";

        btnDirection = (Button) findViewById(R.id.btnDirection);
        btnDirection.setOnClickListener(this);

        errorInResults = (TextView) findViewById(R.id.errorInResults);
        timeDistance = (TextView) findViewById(R.id.timeDistance);
        locations = (TextView) findViewById(R.id.locations);

        tbMode = (ToggleButton) findViewById(R.id.tbMode);

        tbMode.setChecked(true);

        setUpMapIfNeeded();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                makeMarker(latLng);
                newSelectedLocation = latLng;
            }
        });

    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    private void makeMarker(LatLng point) {
        clearMap();
        MarkerOptions selectedLocation = new MarkerOptions()
                .position(point)
                .title("Current Position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));

        googleMap.addMarker(selectedLocation);

    }

    private void setUpMap() {

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMyLocationEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);

        googleMap.setOnInfoWindowClickListener(this);

    }

    public void clearMap() {
        googleMap.clear();
    }

    @Override
    public void onClick(View v) {
        if (v == btnDirection) {


            errorInResults.setVisibility(View.INVISIBLE);
            timeDistance.setVisibility(View.VISIBLE);
            locations.setVisibility(View.VISIBLE);


            timeDistance.setText("");
            locations.setText("");
            clearMap();

            MarkerOptions mDestination = new MarkerOptions()
                    .position(newSelectedLocation)
                    .title(destinationPositionTitle)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));


            LatLng cLoc = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
            myCurrentLocation = cLoc;

            MarkerOptions mStart = new MarkerOptions()
                    .position(myCurrentLocation)
                    .title(startPositionTitle)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.src));

            googleMap.addMarker(mDestination);
            googleMap.addMarker(mStart);

            if (tbMode.isChecked()) {
               /* new GetRotueListTask(MainActivity.this, startPosition,
                        destinationPosition, GMapV2Direction.MODE_DRIVING, this)
                        .execute();*/
                new GetRotueListTask(MainActivity.this, myCurrentLocation,
                        newSelectedLocation, GMapV2Direction.MODE_DRIVING, this)
                        .execute();

                findDirection();

            } else {
               /* new GetRotueListTask(MainActivity.this, startPosition,
                        destinationPosition, GMapV2Direction.MODE_WALKING, this)
                        .execute();*/
                new GetRotueListTask(MainActivity.this, myCurrentLocation,
                        newSelectedLocation, GMapV2Direction.MODE_WALKING, this)
                        .execute();
            }
        }
    }

    private void findDirection() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                GMapV2Distance gMapV2Distance = new GMapV2Distance();
                String response = gMapV2Distance.getDistance(myCurrentLocation, newSelectedLocation);

                if (response != null)
                    return response;

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String status = jsonObject.getString("status");
                        if (status.equals("OK")) {
                            //////////////////////
                            String src_addr = jsonObject.getString("origin_addresses");
                            String dest_addr = jsonObject.getString("destination_addresses");
                            /////////////////////
                            JSONArray jsonArray = new JSONArray(src_addr);
                            src_addr = jsonArray.getString(0);
                            jsonArray = new JSONArray(dest_addr);
                            dest_addr = jsonArray.getString(0);
                            /////////////////////
                            String rows = jsonObject.getString("rows");
                            jsonArray = new JSONArray(rows);
                            String elements = jsonArray.getString(0);

                            JSONObject elementsObject = new JSONObject(elements);
                            String elemObjs = elementsObject.getString("elements");
                            JSONArray elementsArr = new JSONArray(elemObjs);

                            JSONObject elemStatus = elementsArr.getJSONObject(0);

                            String forStatus = elemStatus.getString("status");
                            if (forStatus.equals("OK")) {
                                String forDistance = elemStatus.getString("distance");
                                String forDuration = elemStatus.getString("duration");

                                JSONObject distanceObjects = new JSONObject(forDistance);
                                JSONObject durationObjects = new JSONObject(forDuration);

                                String finalFetchedDistance = distanceObjects.getString("text");
                                String finalFetchedDuration = durationObjects.getString("text");

                                finalFetchedDistance = finalFetchedDistance.replace("mi", "Miles");


                                timeDistance.setText("Approx. " + finalFetchedDistance + " in " + finalFetchedDuration);
                                locations.setText("From : " + src_addr + " To : " + dest_addr);

                                Log.d("inbox", " forStatus : " + finalFetchedDistance + " : " + finalFetchedDuration);
                            } else {
                                // no information
                                errorInResults.setVisibility(View.VISIBLE);
                                timeDistance.setVisibility(View.VISIBLE);
                                locations.setVisibility(View.VISIBLE);
                            }

                            ///////////////////
                            Log.d("inbox", "S : " + src_addr + " D : " + dest_addr);
                        } else {
                            // unable to find distance/duration
                            errorInResults.setVisibility(View.VISIBLE);
                            timeDistance.setVisibility(View.VISIBLE);
                            locations.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // unable to fetch distance/duration
                    errorInResults.setVisibility(View.VISIBLE);
                    timeDistance.setVisibility(View.VISIBLE);
                    locations.setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }

    @Override
    public void OnDirectionListReceived(List<LatLng> mPointList) {
        if (mPointList != null) {
            PolylineOptions rectLine = new PolylineOptions().width(10).color(
                    Color.BLUE);
            for (int i = 0; i < mPointList.size(); i++) {
                rectLine.add(mPointList.get(i));
            }
            googleMap.addPolyline(rectLine);

            CameraPosition mCPFrom = new CameraPosition.Builder()
                    .target(myCurrentLocation).zoom(15.5f).bearing(0).tilt(25)
                    .build();
            final CameraPosition mCPTo = new CameraPosition.Builder()
                    .target(newSelectedLocation).zoom(15.5f).bearing(0)
                    .tilt(50).build();

            changeCamera(CameraUpdateFactory.newCameraPosition(mCPFrom),
                    new CancelableCallback() {
                        @Override
                        public void onFinish() {
                            changeCamera(CameraUpdateFactory
                                            .newCameraPosition(mCPTo),
                                    new CancelableCallback() {

                                        @Override
                                        public void onFinish() {

                                            LatLngBounds bounds = new LatLngBounds.Builder()
                                                    .include(myCurrentLocation)
                                                    .include(
                                                            newSelectedLocation)
                                                    .build();
                                            changeCamera(
                                                    CameraUpdateFactory
                                                            .newLatLngBounds(
                                                                    bounds, 50),
                                                    null, false);
                                        }

                                        @Override
                                        public void onCancel() {
                                        }
                                    }, false);
                        }

                        @Override
                        public void onCancel() {
                        }
                    }, true);
        }
    }

    /**
     * Change the camera position by moving or animating the camera depending on
     * input parameter.
     */
    private void changeCamera(CameraUpdate update, CancelableCallback callback,
                              boolean instant) {

        if (instant) {
            googleMap.animateCamera(update, 1, callback);
        } else {
            googleMap.animateCamera(update, 4000, callback);
        }
    }

    @Override
    public void onInfoWindowClick(Marker selectedMarker) {

        if (selectedMarker.getTitle().equals(startPositionTitle)) {
            Toast.makeText(this, "Marker Clicked: " + startPositionTitle,
                    Toast.LENGTH_LONG).show();
        } else if (selectedMarker.getTitle().equals(destinationPositionTitle)) {
            Toast.makeText(this, "Marker Clicked: " + destinationPositionTitle,
                    Toast.LENGTH_LONG).show();
        }
        selectedMarker.hideInfoWindow();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}