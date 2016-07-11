package br.com.doe.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.doe.R;
import br.com.doe.core.UserHolder;
import br.com.doe.model.User;
import br.com.doe.utils.DoeUtils;

/**
 * @author heitornsouza Copyright 2013. All rights reserved
 */
public class DonationActivity extends BaseActivity implements
        OnClickListener, FragmentDrawer.DrawerListener {

    private User mUser;
    private TextView userName;
    private TextView donationCount;
    private TextView donationLabel;
    private LinearLayout mLLDonationHistorical;

    private Button btnLogout;
    private User userHolder;

    private Toolbar mToolBar;
    private FragmentDrawer mFragmentDrawer;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_layout);
        loadDonationPreferenceView();
        Intent it = getIntent();
        if (it != null) {
            mUser = it.getParcelableExtra(DoeUtils.USER_EXTRA);
            UserHolder.setUserHolder(mUser);
        }

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        if (mToolBar != null && getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar,
                    R.string.app_name, R.string.app_name);
            mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
            getSupportActionBar().setTitle(getResources().getString(R.string.donation_action));
            mToolBar.getMenu().clear();
        }

        mFragmentDrawer = (FragmentDrawer) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_drawer);

        mFragmentDrawer.setDrawerListener(this);
        mFragmentDrawer.setUser(mUser);
        try {
            mFragmentDrawer.setData(DoeUtils.parserUserData(mUser.getDonationList(), getApplication()));
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
         * (non-Javadoc)
         *
         * @see android.app.Activity#onPause()
         */
    @Override
    protected void onPause() {
        super.onPause();
        //mLLDonationHistorical.removeAllViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionBarDrawerToggle.onOptionsItemSelected(item);
    }

    private void loadDonationPreferenceView() {

        RelativeLayout mBrinquedos = (RelativeLayout) findViewById(R.id.briquedos_categories);
        mBrinquedos.setOnClickListener(this);
        RelativeLayout mCesta_basica = (RelativeLayout) findViewById(R.id.cesta_categories);
        mCesta_basica.setOnClickListener(this);

        RelativeLayout mHigiene = (RelativeLayout) findViewById(R.id.higiene_categories);
        mHigiene.setOnClickListener(this);

        RelativeLayout mLimpeza = (RelativeLayout) findViewById(R.id.limpeza_categories);
        mLimpeza.setOnClickListener(this);

        RelativeLayout mPedagogico = (RelativeLayout) findViewById(R.id.pedacogico_categories);
        mPedagogico.setOnClickListener(this);

        RelativeLayout mRemedios = (RelativeLayout) findViewById(R.id.remedio_categories);
        mRemedios.setOnClickListener(this);

        RelativeLayout mToalhaDeBanho = (RelativeLayout) findViewById(R.id.banho_categories);
        mToalhaDeBanho.setOnClickListener(this);

        RelativeLayout mToalhaDeMesa = (RelativeLayout) findViewById(R.id.mesa_categories);
        mToalhaDeMesa.setOnClickListener(this);

        RelativeLayout mRecurso = (RelativeLayout) findViewById(R.id.financeiro_categories);
        mRecurso.setOnClickListener(this);

        RelativeLayout mRoupas = (RelativeLayout) findViewById(R.id.roupas_categories);
        mRoupas.setOnClickListener(this);

        RelativeLayout mOutros = (RelativeLayout) findViewById(R.id.outros_categories);
        mOutros.setOnClickListener(this);

        RelativeLayout mLencol = (RelativeLayout) findViewById(R.id.lencol_categories);
        mLencol.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.donation, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        Intent it = new Intent(this, InstitutionListActivity.class);
        it.putExtra(getResources().getString(R.string.user),
                mUser);

        if (checkInternetConnection()) {

            if (R.id.briquedos_categories == v.getId()) {
                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_brinquedos_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.brinquedos));
            } else if (R.id.cesta_categories == v.getId()) {
                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_cesta_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.cesta_basica));
            } else if (R.id.higiene_categories == v.getId()) {
                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_higiene_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.higiene_pessoal));
            } else if (R.id.limpeza_categories == v.getId()) {

                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_limpeza_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.material_limpeza));

            } else if (R.id.pedacogico_categories == v.getId()) {

                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_pedagogico_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.material_pedagogico));

            } else if (R.id.financeiro_categories == v.getId()) {

                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_financeiro_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.recursos_financeiros));
            } else if (R.id.remedio_categories == v.getId()) {
                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_remedios_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.remedio));
            } else if (R.id.banho_categories == v.getId()) {
                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_banho_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.toalha_banho));
            } else if (R.id.mesa_categories == v.getId()) {
                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_mesa_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.toalha_mesa));
            } else if (R.id.lencol_categories == v.getId()) {
                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_lencol_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.lencol));
            } else if (R.id.roupas_categories == v.getId()) {
                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_roupa_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.roupas));
            } else if (R.id.outros_categories == v.getId()) {
                it.putExtra(getResources().getString(R.string.donation_id),
                        R.drawable.ic_outros_white);
                it.putExtra(getResources().getString(R.string.donation),
                        getResources().getString(R.string.outros));
            }

            startActivity(it);
        }

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }

    @Override
    public void onLogout() {

        SharedPreferences mTokenPreference =
                getSharedPreferences(DoeUtils.CURRENT_USER_SP, MODE_PRIVATE);

        SharedPreferences.Editor editor = mTokenPreference.edit();

        editor.putString(DoeUtils.TOKEN_SP, "");
        editor.putInt("UserID", 0);
        editor.commit();

        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
        finish();

    }


}
