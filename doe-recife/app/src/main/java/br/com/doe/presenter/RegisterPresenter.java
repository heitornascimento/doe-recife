package br.com.doe.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import br.com.doe.R;
import br.com.doe.endpoints.DoeException;
import br.com.doe.endpoints.Endpoint;
import br.com.doe.endpoints.EndpointResult;
import br.com.doe.endpoints.RegisterEndpoint;
import br.com.doe.model.Donation;
import br.com.doe.model.User;

/**
 * Created by heitornascimento on 4/14/16.
 */
public class RegisterPresenter extends BasePresenter {

    private Presentable mView;
    private Context mContext;

    public RegisterPresenter(Presentable view, Context context) {
        mView = view;
        mContext = context;
    }

    public void registerUser(@NonNull String name,
                             @NonNull String email, @NonNull String password, @NonNull String repeatPassword) {

        Intent registerService = new Intent(mContext, RegisterEndpoint.class);
        EndpointResult receiver = new EndpointResult(new Handler());
        receiver.setReceiver(this);
        registerService.putExtra(Endpoint.EMAIL, email);
        registerService.putExtra(Endpoint.PASSWORD, password);
        registerService.putExtra(Endpoint.NAME, name);
        registerService.putExtra(Endpoint.REPEAT_PASSWORD, repeatPassword);
        registerService.putExtra(Endpoint.RECEIVER, receiver);
        //bind service!!
        mContext.startService(registerService);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        if (resultCode == Endpoint.OK && resultData != null) {
            String result = resultData.getString(Endpoint.ENDPOINT_RESULT);
            try {
                User user = loadUserData(result);
                //get back to activity/fragment
                mView.onSuccess(user);
            } catch (Exception e) {
                mView.onFailure(mContext.getResources().getString(R.string.error_registration));
            }
        } else {
            mView.onFailure(mContext.getResources().getString(R.string.error_registration));
        }

    }

    private User loadUserData(@NonNull String result) throws JSONException,
            UnsupportedEncodingException, DoeException {

        JSONObject json = new JSONObject(result);

        if (json.getJSONObject("data") == null) {
            throw new DoeException("Endpoint Result - Data is Null");
        }

        Gson gson = new Gson();

        String token = json.getJSONObject("data").getString(
                "auth_token");
        JSONObject jsonObject = json.getJSONObject("data")
                .getJSONObject("user");
        User user = gson.fromJson(jsonObject.toString(), User.class);
        user.setmAuthToken(token);
        loadDonationPerUser(json, user);

        JSONArray jsonDonations = json.getJSONObject("data").getJSONArray(
                "donations");
        user.setmDonationAmount(jsonDonations.length());

        return user;
    }

    private void loadDonationPerUser(JSONObject jsonObject, User user)
            throws JSONException, UnsupportedEncodingException {

        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray(
                "donations");

        Gson gson = new Gson();

        if (jsonArray != null && jsonArray.length() > 0) {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj != null) {

                    Donation donation = gson.fromJson(jsonObj.toString(),
                            Donation.class);

                    String type = donation.getDonation_description();
                    type = URLDecoder.decode(type, "utf-8");
                    donation.setDonation_description(type);

                    user.setDonationList(donation);
                }
            }
        }
    }


}
