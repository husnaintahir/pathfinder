package com.pk.variableinc.driverinterface;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by androidkhan on 2/16/16.
 */
public class GetLocation {

    public static boolean Arabic;

    Context c;

    LocationManager locationManager;
    LocationListener locationListener;

    float latitude, longitude;

    public GetLocation(Context c) {
        this.c = c;
        locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);

    }

    public Location getLatLng() {

        Location lastLocation = null;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {


                    latitude = (float) location.getLatitude();
                    longitude = (float) location.getLongitude();

                    Log.d("locationinfo", "New Location : " + latitude + ", " + longitude);

                    locationManager.removeUpdates(locationListener);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        } else {
            Toast.makeText(c, "Location Services and GPS not enabled", Toast.LENGTH_LONG).show();
        }


        if (lastLocation != null) {

            latitude = (float) lastLocation.getLatitude();
            longitude = (float) lastLocation.getLongitude();

            locationManager.removeUpdates(locationListener);


            Log.d("locationinfo", "Last Location : " + latitude + ", " + longitude);


        }
        return lastLocation;
    }

    public float getLat() {
        return latitude;
    }

    public float getLong() {
        return longitude;
    }

    public void stopRequesting() {
        locationManager.removeUpdates(locationListener);
    }
}
