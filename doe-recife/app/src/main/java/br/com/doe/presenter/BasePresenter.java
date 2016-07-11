package br.com.doe.presenter;

import android.os.Bundle;

import br.com.doe.endpoints.EndpointResult;

/**
 * Created by heitornascimento on 4/15/16.
 */
public abstract class BasePresenter implements EndpointResult.Receiver {

    private boolean isForeground;
    public String DEBUG = "doe";


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    public boolean isForeground(){
        return  isForeground;
    }

    public void setForeground(boolean isForeground){
        this.isForeground = isForeground;
    }

    public static interface Presentable<T> {

        public void onSuccess(T resultObj);

        public void onFailure(String msg);
    }
}
