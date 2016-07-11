package br.com.doe.endpoints;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URLEncoder;

import br.com.doe.utils.PropertiesHelper;

/**
 * Created by heitornascimento on 6/12/16.
 */
public class RegisterDonationEndpoint extends IntentService {

    private ResultReceiver mReceiver;

    public RegisterDonationEndpoint() {
        super("registeDonation");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent == null || intent.getExtras() == null) {
            return;
        }


        try {
            Log.i("doe", "onHandleIntent");
            String userId = intent.getStringExtra(Endpoint.USER_ID);
            String donationPreference = intent.getStringExtra(Endpoint.DONATION_PREFERENCE);
             String institutionId = intent.getStringExtra(Endpoint.INSTITUTION_ID);
            String token = intent.getStringExtra(Endpoint.TOKEN);
            mReceiver = intent.getParcelableExtra("receiver");
            String response = registerDonation(userId, donationPreference,
                    institutionId, token);
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

    /**
     * TODO encapsulate into a POJO
     *
     * @param userId
     * @param donationDescription
     * @param institutionId
     * @param token
     * @return
     */
    private String registerDonation(@NonNull String userId,
                                    @NonNull String donationDescription,
                                    @NonNull String institutionId,
                                    @NonNull String token) throws IOException {

        String url = PropertiesHelper.readbaseURLProperties(getApplication());
        url += "donations?auth_token=" + token;

        JsonObject paramsJson = new JsonObject();
        paramsJson.addProperty("user_id", URLEncoder.encode(
                userId, "UTF-8"));
        paramsJson.addProperty("donation_description", URLEncoder.encode(
                donationDescription, "UTF-8"));
        paramsJson.addProperty("institution_id", URLEncoder.encode(
                institutionId, "UTF-8"));

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(paramsJson);
        JsonObject urlParam = new JsonObject();
        urlParam.add("donation", element);

        Log.i("doe", "donation " + urlParam.toString());

        String response = Endpoint.doPost(url, urlParam);
        return response;

    }

    private void sendToReceiver(String response) {

        Log.i("doe", "sendToReceiver =" + response);
        Bundle bundle = new Bundle();
        bundle.putString(Endpoint.ENDPOINT_RESULT, response);
        if (mReceiver != null) {
            mReceiver.send(Endpoint.OK, bundle);
        }
    }
}
