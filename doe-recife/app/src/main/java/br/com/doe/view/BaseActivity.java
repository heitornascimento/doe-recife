package br.com.doe.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import br.com.doe.R;
import br.com.doe.endpoints.Endpoint;

/**
 * Created by heitornascimento on 6/29/16.
 */
public class BaseActivity extends AppCompatActivity {

    public String DEBUG = "doe";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(DEBUG, "base activity on create");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(DEBUG, "base activity on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(DEBUG, "base activity on pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(DEBUG, "base activity on destroy");
    }

    protected boolean checkInternetConnection() {

        boolean result = Endpoint.isValidConnection(this);

        if (!result) {
            Snackbar snackbar = Snackbar.
                    make(findViewById(R.id.cl),
                            getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).setAction("Settings", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                }
            });
            snackbar.show();
        }
        return result;
    }
}
