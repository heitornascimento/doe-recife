package br.com.doe.endpoints;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import br.com.doe.utils.PropertiesHelper;

/**
 * Created by heitornascimento on 4/14/16.
 */
public class RegisterEndpoint extends IntentService {

    private ResultReceiver mReceiver;

    public RegisterEndpoint() {
        super("RegisterEndpoint");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return;
        }

        try {
            Log.i("doe", "onHandleIntent Register User");
            //Connects the service to the presenter.
            mReceiver = intent.getParcelableExtra(Endpoint.RECEIVER);
            String response = authenticateToServer(intent);
            sendToReceiver(response);
        } catch (IOException e) {
            Log.i("doe", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Wrap the parameter into a json object and proceed a POST.
     * @param intent
     * @return
     * @throws IOException
     */
    private String authenticateToServer(Intent intent) throws IOException {

        String email = intent.getStringExtra(Endpoint.EMAIL);
        String password = intent.getStringExtra(Endpoint.PASSWORD);
        String repeatPassword = intent.getStringExtra(Endpoint.REPEAT_PASSWORD);
        String name = intent.getStringExtra(Endpoint.NAME);

        String url = PropertiesHelper.readRegisterUrlProperties(getApplication());
        Log.i("doe","before DoPost");

        JsonObject paramsJson = new JsonObject();
        paramsJson.addProperty("email",email);
        paramsJson.addProperty("password",password);
        paramsJson.addProperty("password_confirmation",repeatPassword);
        paramsJson.addProperty("name",name);

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(paramsJson);
        JsonObject urlParam = new JsonObject();
        urlParam.add("user",element);

        Log.i("doe","userJson "+urlParam.toString());

        String response = Endpoint.doPost(url, urlParam);
        return response;

    }

    private void sendToReceiver(String response) {

        Log.i("doe","sendToReceiver ="+response);
        Bundle bundle = new Bundle();
        bundle.putString(Endpoint.ENDPOINT_RESULT, response);
        if(mReceiver != null){
            mReceiver.send(Endpoint.OK, bundle);
        }

    }
}
