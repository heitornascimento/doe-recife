package br.com.doe.endpoints;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
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
import java.net.URL;
import java.net.URLEncoder;

import br.com.doe.model.User;
import br.com.doe.utils.PropertiesHelper;

/**
 * Created by heitornascimento on 4/10/16.
 */
public class LoginEndpoint extends IntentService {

    private ResultReceiver mReceiver;
    private static final int OK = 0;

    public LoginEndpoint() {
        super("LoginEndpoint");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent == null || intent.getExtras() == null) {
            return;
        }

        String action = intent.getStringExtra("action");

        try {
            String response = "";
            if (action == null || action.equals("")) {
                response = executeDefaultLogin(intent);
            } else {
                response = executeTokenLogin(intent);
            }
            sendToReceiver(response);
        } catch (IOException e) {
            Bundle bundle = new Bundle();
            bundle.putString(Endpoint.ENDPOINT_RESULT, e.getMessage());
            if (mReceiver != null) {
                mReceiver.send(Endpoint.ERROR, bundle);
            }
            Log.i("doe", e.getMessage());
            e.printStackTrace();
        }
    }

    private String executeDefaultLogin(Intent intent) throws IOException {
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        mReceiver = intent.getParcelableExtra("receiver");

        User user = new User();
        user.setEmail(username);
        user.setPassword(password);

        String response = authenticateToServer(user);

        return response;
    }

    private String executeTokenLogin(Intent intent) throws IOException {
        int id = intent.getIntExtra("user_id", 0);
        String token = intent.getStringExtra("token");
        mReceiver = intent.getParcelableExtra("receiver");

        User user = new User();
        user.setmId(id);
        user.setmAuthToken(token);

        String response = authenticateWithToken(user);

        Log.i("doe", "response = "+response);

        return response;
    }


    /**
     * Do the authentication in the webservice.
     *
     * @param user
     * @return A json should contains the token and User model info.
     * @throws IOException
     */
    private String authenticateWithToken(User user) throws IOException {

        String url = PropertiesHelper.readbaseURLProperties(getApplicationContext());

        url += "users/" + user.getmId()
                + "?auth_token=" + user.getAuthToken();


        String response = Endpoint.doGET(url);
        return response;
    }

    /**
     * Do the authentication in the webservice.
     *
     * @param user
     * @return A json should contains the token and User model info.
     * @throws IOException
     */
    private String authenticateToServer(User user) throws IOException {

        String url = PropertiesHelper.readLoginUrlProperties(getApplicationContext());
        Log.i("doe", "before DoPost");
        Log.i("doe", "url = " + url);

        JsonObject paramsJson = new JsonObject();
        paramsJson.addProperty("email", user.getEmail());
        paramsJson.addProperty("password", user.getPassword());
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(paramsJson);
        JsonObject urlParam = new JsonObject();
        urlParam.add("user", element);
        Log.i("doe", "userJson " + urlParam.toString());

        String response = Endpoint.doPost(url, urlParam);
        return response;
    }

    /**
     * Return to the activities that have called this action.
     *
     * @param response
     */
    private void sendToReceiver(String response) {

        Log.i("doe", "sendToReceiver =" + response);
        Bundle bundle = new Bundle();
        bundle.putString(Endpoint.ENDPOINT_RESULT, response);
        if (mReceiver != null) {
            mReceiver.send(Endpoint.OK, bundle);
        }
    }


}
