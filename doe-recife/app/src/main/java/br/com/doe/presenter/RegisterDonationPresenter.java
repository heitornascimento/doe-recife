package br.com.doe.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.BaseAdapter;

import br.com.doe.endpoints.Endpoint;
import br.com.doe.endpoints.EndpointResult;
import br.com.doe.endpoints.RegisterDonationEndpoint;
import br.com.doe.endpoints.RegisterEndpoint;

/**
 * Created by heitornascimento on 6/11/16.
 */
public class RegisterDonationPresenter extends BasePresenter {

    private Context mContext;
    private Presentable mView;

    public RegisterDonationPresenter(Presentable view, Context ctx) {
        this.mView = view;
        this.mContext = ctx;
    }

    public void registerDonation(@NonNull String userId,
                                 @NonNull String donationDescription,
                                 @NonNull String institutionId, @NonNull String token) {

        Intent registerService = new Intent(mContext, RegisterDonationEndpoint.class);
        EndpointResult receiver = new EndpointResult(new Handler());
        receiver.setReceiver(this);
        registerService.putExtra(Endpoint.USER_ID, userId);
        registerService.putExtra(Endpoint.DONATION_PREFERENCE, donationDescription);
        registerService.putExtra(Endpoint.INSTITUTION_ID, institutionId);
        registerService.putExtra(Endpoint.TOKEN, token);
        registerService.putExtra(Endpoint.RECEIVER, receiver);
        //bind service!!
        mContext.startService(registerService);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        if(resultCode == Endpoint.OK && resultData != null){
            String result = resultData.getString(Endpoint.ENDPOINT_RESULT);

            if(result != null && !result.equals("")){
                mView.onSuccess(result);
            } else{
                mView.onFailure("");
            }
        } else{
            mView.onFailure("");
        }


    }
}
