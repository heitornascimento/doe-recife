package br.com.doe.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

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
import br.com.doe.endpoints.LoginEndpoint;
import br.com.doe.model.Donation;
import br.com.doe.model.Institution;
import br.com.doe.model.User;
import br.com.doe.utils.DoeUtils;

/**
 * Created by heitornascimento on 4/14/16.
 */
public class LoginPresenter extends BasePresenter {

    private Presentable mViewLogin;
    private Context mContext;

    public LoginPresenter(@NonNull Presentable view, @NonNull Context context) {
        mViewLogin = view;
        mContext = context;
    }

    /**
     * Call the service to authenticate the user.
     *
     * @param email
     * @param password
     */
    public void executeLogin(@NonNull String email, @NonNull String password) {
        if (isForeground()) {
            Intent loginService = new Intent(mContext, LoginEndpoint.class);
            EndpointResult receiver = new EndpointResult(new Handler());
            receiver.setReceiver(this);
            loginService.putExtra("username", email);
            loginService.putExtra("password", password);
            loginService.putExtra("receiver", receiver);
            mContext.startService(loginService);
        }
    }

    /**
     * Login with a token.
     *
     * @param userId
     * @param token
     */
    public void executeTokenLogin(@NonNull int userId, @NonNull String token) {
        if (isForeground()) {
            Intent loginService = new Intent(mContext, LoginEndpoint.class);
            EndpointResult receiver = new EndpointResult(new Handler());
            receiver.setReceiver(this);
            loginService.putExtra("action", "token_login");
            loginService.putExtra("user_id", userId);
            loginService.putExtra("token", token);
            loginService.putExtra("receiver", receiver);
            mContext.startService(loginService);
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        if (isForeground()) {
            if (resultCode == Endpoint.OK && resultData != null) {
                String result = resultData.getString(Endpoint.ENDPOINT_RESULT);
                if (result != null && result != "") {
                    User user = null;
                    try {
                        user = buildUserModel(result);
                    } catch (Exception e) {
                        mViewLogin.onFailure("Error attempting login - " + e.getMessage());
                    }
                    mViewLogin.onSuccess(user);
                } else {
                    mViewLogin.onFailure("Error attempting login - result is null");
                }
            } else {
                mViewLogin.onFailure("Error attempting login");
            }
        }
    }

    /**
     * Parse the Json into User Object
     *
     * @param result
     * @return
     * @throws JSONException
     * @throws DoeException
     */
    private User buildUserModel(String result) throws JSONException, DoeException, UnsupportedEncodingException {

        User user = new User();
        JSONObject jsonObject = new JSONObject(result);

        if (!jsonObject.has("success")) {
            JSONObject jsonObject1 = new JSONObject(result);
            return loadUserDataLogin(jsonObject1, user);
        } else {

            boolean isSuccess = jsonObject.getBoolean("success");
            if (!isSuccess) {
                throw new DoeException("Error attempting login");
            } else {
                JSONObject jsonData = jsonObject.getJSONObject("data");
                String authToken = jsonData.getString("auth_token");

                JSONObject userData = jsonObject.getJSONObject("data").getJSONObject(
                        "user_data");
                Gson gson = new Gson();
                user = gson.fromJson(userData.toString(), User.class);
                loadDonationPerUserLogin(jsonData, user);
                loadInstitutionPerUserLogin(jsonData, user);
                user.setmAuthToken(authToken);
                return user;
            }
        }
    }

    private User loadUserDataLogin(JSONObject json, User user) throws JSONException,
            UnsupportedEncodingException {

        user = new User();
        String userEmail = json.getString("email");
        String name = json.getString("name");
        int id = json.getInt("id");
        user.setEmail(userEmail);
        user.setmName(name);
        user.setmId(id);

        loadDonationPerUserLogin(json, user);
        loadInstitutionPerUserLogin2(json, user);

        JSONArray jsonDonations = json.getJSONArray("donations");
        user.setmDonationAmount(jsonDonations.length());

        return user;
    }

    private void loadDonationPerUserLogin(JSONObject jsonObject, User user)
            throws JSONException, UnsupportedEncodingException {

        JSONArray jsonArray = jsonObject.getJSONArray("donations");

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

    private void loadInstitutionPerUserLogin2(JSONObject jsonObject, User user)
            throws JSONException, UnsupportedEncodingException {

        JSONArray jsonArray = jsonObject.getJSONArray("institutions");

        Gson gson = new Gson();

        if (jsonArray != null && jsonArray.length() > 0) {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj != null) {

                    Institution instituion = gson.fromJson(jsonObj.toString(),
                            Institution.class);

                    // String type = donation.getDonation_description();
                    // type = URLDecoder.decode(type, "utf-8");
                    // donation.setDonation_description(type);

                    user.setInstitutionList(instituion);
                }
            }
        }
    }

    private void loadInstitutionPerUserLogin(JSONObject jsonObject, User user)
            throws JSONException, UnsupportedEncodingException {

        JSONArray jsonArray = jsonObject.getJSONArray("institution");

        Gson gson = new Gson();

        if (jsonArray != null && jsonArray.length() > 0) {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj != null) {

                    Institution institution = gson.fromJson(jsonObj.toString(),
                            Institution.class);

                    // String type = donation.getDonation_description();
                    // type = URLDecoder.decode(type, "utf-8");
                    // donation.setDonation_description(type);

                    user.setInstitutionList(institution);
                }
            }
        }
    }

    /**
     * Check if the user has logged in before.
     */
    public void loginWithUserToken() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(DoeUtils.CURRENT_USER_SP, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String token = sharedPreferences.getString(DoeUtils.TOKEN_SP, "");
        int userId = sharedPreferences.getInt(DoeUtils.USER_ID, 0);

        if (token.equals("") || userId == 0) {
            Log.i(DEBUG, "not authenticated");
            return;//User is not authenticated.
        }

        Log.i(DEBUG, "try with token");
        ((LoginView) mViewLogin).showLoading();
        executeTokenLogin(userId, token);
    }

    public interface LoginView {
        public void showLoading();

        public String getUserEmail();

        public String getPassword();
    }
}
