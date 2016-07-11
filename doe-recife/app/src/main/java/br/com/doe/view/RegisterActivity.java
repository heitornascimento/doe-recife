package br.com.doe.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.doe.presenter.RegisterPresenter;
import br.com.doe.R;
import br.com.doe.model.User;
import br.com.doe.utils.DoeUtils;

/**
 * Created by heitornascimento on 4/14/16.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterPresenter.Presentable {

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEdiTextPassword;
    private EditText mEditTextPasswordRepeat;

    private String mUserName;
    private String mUserEmail;
    private String mUserPassword;
    private String mUserRepeatPassword;

    private Button mBtnSingUp;

    private RegisterPresenter mRegisterPresenter;
    private ProgressDialog mLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadRegisterView();
        mRegisterPresenter = new RegisterPresenter(this, getApplicationContext());
    }

    private void loadRegisterView() {

        mEditTextName = (EditText) findViewById(R.id.user_name);
        mEditTextName.setOnClickListener(this);

        mEditTextEmail = (EditText) findViewById(R.id.user_email);
        mEditTextEmail.setOnClickListener(this);

        mEdiTextPassword = (EditText) findViewById(R.id.user_password);
        mEdiTextPassword.setOnClickListener(this);

        mEditTextPasswordRepeat = (EditText) findViewById(R.id.user_password_confirm);
        mEditTextPasswordRepeat.setOnClickListener(this);

        mBtnSingUp = (Button) findViewById(R.id.btn_signup);
        mBtnSingUp.setOnClickListener(this);

        mLoadingView = new ProgressDialog(this);
        mLoadingView.setIndeterminate(true);
    }

    @Override
    public void onClick(View v) {

        if (checkInternetConnection()) {
            if (v.getId() == R.id.btn_signup && isValid()) {
                mLoadingView.setMessage(getResources().getString(R.string.waiting));
                mLoadingView.show();
                mRegisterPresenter.registerUser(mUserName, mUserEmail, mUserPassword, mUserRepeatPassword);
            }
        }
    }

    private boolean isValid() {
        mUserEmail = mEditTextEmail.getText().toString();
        mUserName = mEditTextName.getText().toString();
        mUserPassword = mEdiTextPassword.getText().toString();
        mUserRepeatPassword = mEditTextPasswordRepeat.getText().toString();

        if (mUserName.isEmpty()) {
            mEditTextName.setError(getResources().getString(R.string.mandatory_fields));
            return false;
        }
        if (mUserEmail.isEmpty()) {
            mEditTextEmail.setError(getResources().getString(R.string.mandatory_fields));
            return false;
        } else if (mUserPassword.isEmpty()) {
            mEdiTextPassword.setError(getResources().getString(R.string.mandatory_fields));
            return false;
        } else if (mUserRepeatPassword.isEmpty()) {
            mEditTextPasswordRepeat.setError(getResources().getString(R.string.mandatory_fields));
            return false;
        } else if (!DoeUtils.isValidEmail(mUserEmail)) {
            mEditTextEmail.setError(getResources().getString(R.string.error_invalid_email));
            return false;
        } else if (!mUserPassword.equals(mUserRepeatPassword)) {
            mEdiTextPassword.setError(getResources().getString(R.string.error_password_not_match));
            return false;
        } else if (mUserPassword.length() < 8) {
            mEdiTextPassword.setError(getResources().getString(R.string.error_min_eight_characters));
            return false;
        } else if (mUserRepeatPassword.length() < 8) {
            mEdiTextPassword.setError(getResources().getString(R.string.error_min_eight_characters));
            return false;
        }
        return true;
    }


    @Override
    public void onSuccess(Object resultObj) {
        User user = (User) resultObj;
        mLoadingView.dismiss();
        Intent intent = new Intent(getApplicationContext(),
                DonationActivity.class);
        intent.putExtra(DoeUtils.USER_EXTRA, user);
        DoeUtils.saveUserData(this,user);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        mLoadingView.dismiss();
    }
}
