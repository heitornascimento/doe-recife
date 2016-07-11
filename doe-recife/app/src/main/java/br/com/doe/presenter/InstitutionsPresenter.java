package br.com.doe.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import org.antlr.v4.runtime.misc.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.doe.R;
import br.com.doe.endpoints.Endpoint;
import br.com.doe.endpoints.EndpointResult;
import br.com.doe.endpoints.InstitutionsEndpoint;
import br.com.doe.model.Donation;
import br.com.doe.model.Institution;

/**
 * Created by heitornascimento on 4/28/16.
 */
public class InstitutionsPresenter extends BasePresenter {

    //weak reference
    private Presentable mViewInstitutionList;
    private Context mContext;
    List<Institution> mData;


    public InstitutionsPresenter(@NonNull Presentable view, @NonNull Context context) {
        mViewInstitutionList = view;
        mContext = context;
    }

    /**
     * Call Institutions' endpoint
     *
     * @param donationType
     * @param token
     */
    public void loadInstitutions(@NotNull String donationType, @NonNull String token) {
        if (isForeground()) {
            Intent institutionIntent = new Intent(mContext, InstitutionsEndpoint.class);
            EndpointResult receiver = new EndpointResult(new Handler());
            receiver.setReceiver(this);
            institutionIntent.putExtra("donation", donationType);
            institutionIntent.putExtra("token", token);
            institutionIntent.putExtra("receiver", receiver);
            mContext.startService(institutionIntent);
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == Endpoint.OK && resultData != null) {

            String result = resultData.getString(Endpoint.ENDPOINT_RESULT);
            Log.i("doe", "doe = " + result);
            //Data for recyclerview
            mData = new ArrayList<Institution>();
            //Data for spinner
            List<String> districtList = new ArrayList<>();
            districtList.add(mContext.getResources().
                    getString(R.string.default_district));

            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONArray institutionsArray = jsonObject
                        .getJSONArray("institutions");
                Gson gson = new Gson();

                for (int i = 0; i < institutionsArray.length(); i++) {

                    JSONObject jsonObj = institutionsArray
                            .getJSONObject(i);
                    if (jsonObj != null) {

                        Institution inst = gson.fromJson(
                                jsonObj.toString(), Institution.class);
                        inst.index = (i+1);
                        loadDonationPerInstitution(jsonObj, inst);
                        if (inst != null) {
                            mData.add(inst);
                            loadDistricts(districtList, inst);
                        }
                    }
                }

                ((Addressable) mViewInstitutionList).onReceiveDistrictList(districtList);
                mViewInstitutionList.onSuccess(mData);

            } catch (JSONException e) {
                e.printStackTrace();
                mViewInstitutionList.onFailure("Error while Loading institutions");
            }


        }
    }

    /**
     * Parser ..
     *
     * @param jsonObject
     * @param inst
     * @throws JSONException
     */
    private void loadDonationPerInstitution(JSONObject jsonObject,
                                            Institution inst) throws JSONException {

        JSONArray jsonArray = jsonObject.getJSONArray("donations");
        Gson gson = new Gson();

        if (jsonArray != null && jsonArray.length() > 0) {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj != null) {

                    Donation donation = gson.fromJson(jsonObj.toString(),
                            Donation.class);

                    inst.setDonationList(donation);
                }
            }
        }
    }

    /**
     * @param districtList
     * @param institution
     */
    private void loadDistricts(List<String> districtList, Institution institution) {

        String address = institution.getAddress();
        String district = "";

        String[] addressArray = address.split("-");
        if (addressArray.length > 0 && addressArray[1] != null) {
            district = addressArray[1];
            districtList.add(district.trim());
        }

    }

    /**
     * Load a list of institution according to district selected in
     * spinner view.
     *
     * @param district
     * @return
     */
    public ArrayList<Institution> loadInstitutionPerDistrict(String district) {

        ArrayList<Institution> newList = new ArrayList<Institution>();

        if (district.equals
                (mContext.getResources().getString(R.string.default_district))) {
            newList = (ArrayList<Institution>) mData;
        } else {
            int index = 0;
            for (int i = 0; i < mData.size(); i++) {
                Institution inst = mData.get(i);
                if (inst.getAddress().contains(district)) {
                    inst.index = ++index;
                    newList.add(inst);
                }
            }

        }

        return newList;
    }

    public static interface Addressable {
        public void onReceiveDistrictList(List<String> districts);
    }


}
