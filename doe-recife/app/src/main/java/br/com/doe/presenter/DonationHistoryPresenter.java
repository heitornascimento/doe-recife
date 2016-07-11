package br.com.doe.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by heitornascimento on 6/14/16.
 */
public class DonationHistoryPresenter extends BasePresenter {

    private Presentable mViewLogin;
    private Context mContext;

    public DonationHistoryPresenter(@NonNull Presentable view, @NonNull Context context) {
        mViewLogin = view;
        mContext = context;
    }

    public void loadDonationHistoryByUser(){

    }

}
