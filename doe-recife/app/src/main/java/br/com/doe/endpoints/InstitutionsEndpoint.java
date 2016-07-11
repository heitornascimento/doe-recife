package br.com.doe.endpoints;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;

import br.com.doe.utils.PropertiesHelper;

/**
 * Created by heitornascimento on 4/28/16.
 */
public class InstitutionsEndpoint extends IntentService {

    private ResultReceiver mReceiver;
    private static final int OK = 0;


    public InstitutionsEndpoint() {
        super("InstitutionsEndpoint");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent == null || intent.getExtras() == null) {
            return;
        }

        try {
            Log.i("doe", "onHandleIntent");
            String donation = intent.getStringExtra("donation");
            String token = intent.getStringExtra("token");
            mReceiver = intent.getParcelableExtra("receiver");
            String response = loadInstitutionsByDonation(donation,token);
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

    private String loadInstitutionsByDonation(String donation, String token) throws IOException {

        String url = PropertiesHelper.readInstituionsURLProperties(getApplication());

        url+= token
                + "&preference="
                + URLEncoder.encode(donation + "", "UTF-8");

        String response = Endpoint.doGET(url);
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
