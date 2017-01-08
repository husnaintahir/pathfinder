package com.map.pathfinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class WebService {

    //    String serverAddress = "http://172.20.10.2/projects/srf/";
    String serverAddress = "http://192.168.1.6/projects/srf/";
//    String serverAddress = "http://sell4masari.com/masaridata/srf/";


    public WebService(String service) {
        this.serverAddress += service;

    }

    public String makeHTTPRequest(Map<String, String> paramData, String method) throws IOException,
            JSONException {

        String uri = serverAddress;
        String data = makeRequestString(paramData);
        Log.d("InboxServices", "Requesting Data: " + data);
        URL url;
        if ("POST".equals(method))
            url = new URL(uri);
        else
            url = new URL(uri + "?" + data);

        Log.d("InboxServices", "Requesting URL:" + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(30 * 1000);

        if ("POST".equals(method)) {
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            writeStream(connection.getOutputStream(), data);
        } else {
            connection.setRequestMethod(method);
            connection.setDoOutput(false);
        }
        String responeString = readStream(connection.getInputStream());
        Log.d("InboxServices", "makeHTTPRequest Response:" + responeString);

        connection.disconnect();

        return responeString;
    }

    private String readStream(InputStream in) {
        InputStreamReader inreader = new InputStreamReader(in);
        String output = "";
        try {
            int data = inreader.read();
            while (data != -1) {
                System.out.print(String.valueOf((char) data));
                output += String.valueOf((char) data);
                data = inreader.read();
            }
        } catch (IOException e) {
            Log.e("InboxServices",
                    "IOException [WebService.readStream()]:" + e.getMessage(),
                    e);
        }
        return output;
    }

    private void writeStream(OutputStream outputStream, String content) {
        OutputStreamWriter outwriter = new OutputStreamWriter(outputStream);
        try {
            outwriter.write(content.toCharArray());
            outwriter.flush();
        } catch (IOException e) {
            Log.e("InboxServices", "IOException [WebService.writeStream()]:"
                    + e.getMessage(), e);
        }

    }

    private String makeRequestString(Map<String, String> params) {
        Iterator<String> paramIterator = params.keySet().iterator();
        StringBuffer requestParams = new StringBuffer();
        while (paramIterator.hasNext()) {
            String key = paramIterator.next();
            String value = params.get(key);
            try {
                requestParams.append(URLEncoder.encode(key, "UTF-8"));
                requestParams.append("=").append(
                        URLEncoder.encode(value, "UTF-8"));
                requestParams.append("&");
            } catch (UnsupportedEncodingException e) {
                Log.e("InboxServices",
                        "Unsupported Encoding Exception [WebService.makeRequestString()]:"
                                + e.getMessage(), e);
            }
        }
        return requestParams.toString();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean status = netInfo != null && netInfo.isConnectedOrConnecting();
        if (!status)
            Toast.makeText(context, "Network Error Make sure you are connected to internet", Toast.LENGTH_LONG).show();
        return status;

    }

    public Bitmap getImage(String src) {
        Bitmap myBitmap = null;
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.setReadTimeout(60000);
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Log.e("VideoGuru", "[WebService.getImage()]:" + e.getMessage(), e);
            return null;
        }
        return myBitmap;
    }

    public static double calculateDistance(double myLat, double myLng, double otherLat, double otherLng) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = 0, lon1 = 0;

        lat1 = myLat;
        lon1 = myLng;


        double lat2 = otherLat;
        double lon2 = otherLng;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult * 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));

//        Log.d("InboxServices", "********************");
//        Log.d("InboxServices", "	***** Result ***** ");
//        Log.d("InboxServices", "********************");
//        Log.d("InboxServices", "Radius Value: " + valueResult);
//        Log.d("InboxServices", "KM: " + kmInDec);
//        Log.d("InboxServices", "Meter: " + meterInDec);

        return meterInDec;


    }


}