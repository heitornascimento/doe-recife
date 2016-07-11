package br.com.doe.endpoints;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by heitornascimento on 4/11/16.
 */
@SuppressLint("ParcelCreator")
public class EndpointResult extends ResultReceiver {

    private Receiver mReceiver;
    private static final int OK = 0;

    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public EndpointResult(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver){
        mReceiver = receiver;
    }
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver != null){
            mReceiver.onReceiveResult(OK, resultData);
        }
    }

    public interface Receiver{
        public void onReceiveResult(int resultCode, Bundle resultData);
    }
}
