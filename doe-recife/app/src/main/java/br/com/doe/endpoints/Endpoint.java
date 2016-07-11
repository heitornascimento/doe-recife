package br.com.doe.endpoints;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by heitornascimento on 4/11/16.
 */
public class Endpoint {

    public static final int OK = 0;
    public static final int ERROR = 400;
    public static final String ENDPOINT_RESULT = "result";


    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String REPEAT_PASSWORD = "repeat_password";

    public static final String USER_ID = "user_id";
    public static final String DONATION_PREFERENCE = "donation_preference";
    public static final String INSTITUTION_ID = "institution_id";
    public static final String TOKEN = "token";



    public static final String RECEIVER = "receiver";


    /**
     * Do POST Http on the webservice.
     *
     * @param urlName
     * @param params
     * @return
     * @throws IOException
     */
    public static String doPost(String urlName, JsonObject params) throws IOException {

        URL url = new URL(urlName);
        Log.i("doe", "url " + url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        byte[] bytes = params.toString().getBytes("UTF-8");

        Log.i("doe", "bytes " + Integer.toString(bytes.length));
        int dataSize = bytes.length;
        Log.i("doe", "dataSize " + dataSize);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(dataSize));
        //connection.setFixedLengthStreamingMode(dataSize);

        //Write the params
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes(params.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        //PrintWriter out = new PrintWriter(connection.getOutputStream());

        //out.write(URLEncoder.encode(userJson.toString().getBytes("UTF-8"),"UTF-8"));
        //out.close();


        //retrieve response
        InputStream inputStream = connection.getInputStream();
        StringBuilder sb = new StringBuilder();
        Log.i("doe", "after sb" + sb.toString());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));) {
            String nextLine = "";
            while ((nextLine = br.readLine()) != null) {
                sb.append(nextLine);
            }
            Log.i("doe", "after sb" + sb.toString());
            return sb.toString();
        } finally {
            //out.close();
        }
    }

    public static boolean isValidConnection(Context ctx) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null) {
                return netInfo.isConnectedOrConnecting();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public static String doGET(String urlName) throws IOException {

        Log.i("doe", "GET");
        URL url = new URL(urlName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        //connection.setDoOutput(true);
        //connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        StringBuilder sb = new StringBuilder();
        Log.i("doe", "after sb" + sb.toString());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));) {
            String nextLine = "";
            while ((nextLine = br.readLine()) != null) {
                sb.append(nextLine);
            }
            Log.i("doe", "after sb" + sb.toString());
            return sb.toString();
        } finally {
            //out.close();
        }


    }

}
