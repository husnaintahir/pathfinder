package com.map.pathfinder;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Husnain on 4/20/2016.
 */
public class GMapV2Distance {

    private String makeUrl(LatLng src, LatLng dest) {


        String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + src.latitude + "," + src.longitude + "&destinations=" + dest.latitude + "%2C" + dest.longitude + "&key=AIzaSyBEr62vxCQnUXSn1qFCvrD5zUs1uEC7pTo";
        Log.d("inbox", "URL=" + URL);

        return URL;
//        return "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592&key=AIzaSyBEr62vxCQnUXSn1qFCvrD5zUs1uEC7pTo";
    }

    public String getDistance(LatLng src, LatLng dest) {
        if (src != null || dest != null) {
            // connect to map web service
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(makeUrl(src, dest));
            HttpResponse response;
            try {
                response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                InputStream is = null;

                is = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                String line = "0";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                reader.close();
                String result = sb.toString();
                Log.d("inbox", "Distsnce Results : " + result);
                return result;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return null;
    }
}
