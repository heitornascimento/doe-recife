package br.com.doe.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.doe.presenter.LoginPresenter;
import br.com.doe.R;
import br.com.doe.endpoints.Endpoint;
import br.com.doe.model.User;
import br.com.doe.utils.DoeUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener,
        LoginPresenter.Presentable, LoginPresenter.LoginView {

    private Button mBtnLogin;
    private TextView mTxtRegister;
    private EditText mUserEmailTxt;
    private EditText mUserPasswordTxt;
    private String mUserEmail;
    private String mUserPassword;

    private LoginPresenter mPresenter;
    private ProgressDialog mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresenter = new LoginPresenter(this, getApplicationContext());
        loadView();
    }

    @Override
    protected void onResume() {

        mPresenter.setForeground(true);
        if (checkInternetConnection()) {
            mPresenter.loginWithUserToken();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPresenter.setForeground(false);
        super.onPause();
    }


    /**
     * TODO replace by data binding or butterknife lib
     */
    private void loadView() {

        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);

        mUserEmailTxt = (EditText) findViewById(R.id.user_email);
        mUserEmailTxt.setOnClickListener(this);

        mTxtRegister = (TextView) findViewById(R.id.txtRegister);
        mTxtRegister.setOnClickListener(this);

        mUserPasswordTxt = (EditText) findViewById(R.id.password);
        mUserPasswordTxt.setOnClickListener(this);

        mLoadingView = new ProgressDialog(this);
        mLoadingView.setIndeterminate(true);
    }

    @Override
    public void onClick(View v) {
        if (checkInternetConnection()) {
            if (v.getId() == mBtnLogin.getId() && isValid()) {
                showLoading();
                mPresenter.executeLogin(mUserEmail, mUserPassword);
            } else if (v.getId() == mTxtRegister.getId()) {
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
            }
        }
    }

    @Override
    public void showLoading() {
        mLoadingView.show();
        mLoadingView.setMessage(getResources().getString(R.string.waiting));
    }

    @Override
    public String getUserEmail() {
        if (mUserEmailTxt != null) {
            return mUserEmailTxt.getText().toString();
        }
        return null;
    }

    @Override
    public String getPassword() {
        if (mUserPasswordTxt != null) {
            return mUserPasswordTxt.getText().toString();
        }
        return null;
    }

    /**
     * Validate the email and password fields.
     *
     * @return
     */
    private boolean isValid() {
        mUserEmail = mUserEmailTxt.getText().toString();
        mUserPassword = mUserPasswordTxt.getText().toString();

        mUserEmailTxt.setError(null);
        mUserPasswordTxt.setError(null);

        if (mUserEmail.isEmpty()) {
            mUserEmailTxt.setError(getResources().getString(R.string.mandatory_fields));
            return false;
        } else if (mUserPassword.isEmpty()) {
            mUserPasswordTxt.setError(getResources().getString(R.string.mandatory_fields));
            return false;
        } else if (!DoeUtils.isValidEmail(mUserEmail)) {
            mUserEmailTxt.setError(getResources().getString(R.string.error_invalid_email));
            return false;
        } else if (mUserPassword.length() < 8) {
            mUserPasswordTxt.setError(getResources().getString(R.string.error_min_eight_characters));
            return false;
        } else if (mUserEmail.length() > 0 && mUserPassword.length() > 0) {
            return true;
        }

        return false;
    }


    @Override
    public void onSuccess(Object resultObj) {
        User user = (User) resultObj;
        mLoadingView.dismiss();
        if (user != null) {
            Intent intent = new Intent("DONATION_LIST");
            intent.putExtra(DoeUtils.USER_EXTRA, user);
            DoeUtils.saveUserData(this,user);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(this, "Error " + msg, Toast.LENGTH_SHORT).show();
        mLoadingView.dismiss();
    }

    /**
     * Once logged in, store the token.
     *
     * @param user
     */
    /*private void saveUserData(User user) {

        Log.i(DEBUG, "saving token");

        if (user.getAuthToken() != null && !user.getAuthToken().equals("")) {
            SharedPreferences sharedPreferences = getSharedPreferences(DoeUtils.CURRENT_USER_SP, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(DoeUtils.TOKEN_SP, user.getAuthToken());
            editor.putInt(DoeUtils.USER_ID, user.getmId());
            editor.commit();
        }
    }*/


}
