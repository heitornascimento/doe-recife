package br.com.doe.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.doe.R;
import br.com.doe.core.UserHolder;
import br.com.doe.model.Donation;
import br.com.doe.model.Institution;
import br.com.doe.model.User;
import br.com.doe.presenter.BasePresenter;
import br.com.doe.presenter.RegisterDonationPresenter;
import br.com.doe.utils.DoeUtils;

/**
 * @author heitornsouza Copyright 2013. All rights reserved
 */
public class InstitutionActivity extends AppCompatActivity implements OnClickListener, BasePresenter.Presentable {

    private TextView mTxtName;
    private TextView mTxtAddress;
    private TextView mTxtCnpj;
    private TextView mTxtTel;
    private TextView mTxtEmail;

    private Institution mInstitution;

    private ImageView mImagePhoneCall;
    private ImageView mImageEmail;
    private RelativeLayout rlDoar;

    private String muserPreferenceDonation;

    private User mUser;

    private ProgressDialog mProgressDialog;
    private TextView txtCountNumber;
    private TextView txtCounterName;

    private int countDonation = 0;
    private MapFragment mapFragment;
    private GoogleMap map;
    private LatLngBounds bounds;
    private LatLng latLng;
    private AlertDialog builder;
    private ImageView imgMap;

    private RelativeLayout rlAddress;


    private RegisterDonationPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.institution_layout);
        mPresenter = new RegisterDonationPresenter(this, getApplicationContext());
        loadInstitution(getIntent());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * populate activity data
     *
     * @param intent
     */
    private void loadInstitution(Intent intent) {

        if (intent != null) {
            mInstitution = intent.getParcelableExtra(getResources().getString(
                    R.string.institution));

            muserPreferenceDonation = intent.getStringExtra(getResources()
                    .getString(R.string.user_preference_donaton));

            /*if (UserHolder.getUserHolder() != null) {
                mUser = UserHolder.getUserHolder();
            } else {*/
                mUser = intent.getParcelableExtra(getResources().getString(
                        R.string.user));
            //}
            loadViewsById();
            loadDonationImageInLayout();

            mapFragment = (MapFragment) getFragmentManager().findFragmentById(
                    R.id.institutionsMap);

            map = mapFragment.getMap();
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            latLng = new LatLng(Double.parseDouble(mInstitution.getLat()),
                    Double.parseDouble(mInstitution.getLgn()));

            MarkerOptions marker = new MarkerOptions();
            marker.position(latLng).title(mInstitution.getRazao_social())
                    .snippet(mInstitution.getAddress());
            marker.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_marker_map));
            Marker mk = map.addMarker(marker);
        }

    }

    /**
     * load up all view.
     */
    private void loadViewsById() {


        mTxtAddress = (TextView) findViewById(R.id.inst_endereco);
        mTxtName = (TextView) findViewById(R.id.inst_razao_social);
        mImageEmail = (ImageView) findViewById(R.id.img_email);
        mImageEmail.setOnClickListener(this);
        mImagePhoneCall = (ImageView) findViewById(R.id.img_call);
        mImagePhoneCall.setOnClickListener(this);
        rlDoar = (RelativeLayout) findViewById(R.id.rl_registar);
        rlDoar.setOnClickListener(this);
        mTxtCnpj = (TextView) findViewById(R.id.inst_cnpj);
        mTxtEmail = (TextView) findViewById(R.id.inst_email);
        mTxtTel = (TextView) findViewById(R.id.inst_tel);

        mTxtCnpj.setText(mInstitution.getCnpj());
        mTxtEmail.setText(mInstitution.getEmail());
        mTxtTel.setText(mInstitution.getFone());

        mTxtAddress.setText(mInstitution.getAddress());
        mTxtName.setText(mInstitution.getRazao_social());

        txtCounterName = (TextView) findViewById(R.id.inst_counter_name);
        txtCountNumber = (TextView) findViewById(R.id.inst_counter_number);

        countDonation = filterDonationUserByCurrentInstitution(mUser).size();

        imgMap = (ImageView) findViewById(R.id.img_google_maps);
        imgMap.setOnClickListener(this);

        rlAddress = (RelativeLayout) findViewById(R.id.rl_address);
        rlAddress.setOnClickListener(this);

        txtCountNumber.setText(String.valueOf(countDonation));
        txtCounterName.setText(getResources().
                getQuantityString(R.plurals.user_donation_action, countDonation, countDonation));
    }

    /**
     * Generic Dialog
     *
     * @param message
     */
    private void loadPopUp(String message) {

        builder = new AlertDialog.Builder(this).create();
        builder.setTitle("Doe Recife");
        builder.setMessage(message);

        builder.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        registerDonation();
                    }
                });

        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
                new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });
        builder.show();

    }

    /**
     * Make a phone to the desired institution
     */
    private void loadPopUpPhoneCall() {

        builder = new AlertDialog.Builder(this).create();
        builder.setTitle(getResources().getString(R.string.title_dialog));
        builder.setMessage(getResources().getString(R.string.make_phone_call));

        builder.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        builder.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        if (mInstitution.getFone() != null
                                && !mInstitution.getFone().isEmpty()) {

                            intent.setData(Uri.parse("tel:"
                                    + mInstitution.getFone()));
                            startActivity(intent);
                        }
                    }
                });

        builder.show();
    }

    @Override
    public void onClick(View v) {

        if (R.id.img_email == v.getId()) {

            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);

            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{mInstitution.getEmail()});
            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(intent,
                    0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches) {
                if (info.activityInfo.packageName.endsWith(".gm")
                        || info.activityInfo.name.toLowerCase().contains(
                        "gmail"))
                    best = info;
                if (best != null) {
                    intent.setClassName(best.activityInfo.packageName,
                            best.activityInfo.name);
                    startActivity(intent);
                }
            }

        } else if (R.id.img_call == v.getId()) {

            loadPopUpPhoneCall();

        } else if (R.id.rl_registar == v.getId()) {

            loadPopUp(getResources().getString(
                    R.string.donation_registration_message));
        } else if (R.id.img_google_maps == v.getId() || R.id.rl_address == v.getId()) {

            String uri = String.format(Locale.ENGLISH,
                    "geo:%f,%f?z=%d&q=%f,%f (%s)",
                    Float.parseFloat(mInstitution.getLat()),
                    Float.parseFloat(mInstitution.getLgn()), 20,
                    Float.parseFloat(mInstitution.getLat()),
                    Float.parseFloat(mInstitution.getLgn()),
                    mInstitution.getRazao_social());

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }

    }

    /**
     * TODO unfinished
     */
    private void registerDonation() {

        createProgressDialog();

        mPresenter.registerDonation(String.valueOf(mUser.getmId()), muserPreferenceDonation,
                String.valueOf(mInstitution.getId()), getToken());

    }

    private void loadDonationImageInLayout() {

        LinearLayout llDonationImages = (LinearLayout) findViewById(R.id.ll_img_donation);
        llDonationImages
                .addView(getUserPreferenceDonationView(muserPreferenceDonation));

        List<ImageView> list = getDonationImageView(muserPreferenceDonation);

        for (ImageView imageView : list) {
            llDonationImages.addView(imageView);

        }

    }

    private String removePreference(String preference) {

        StringBuilder mDonationBuilder = new StringBuilder(
                mInstitution.getDonation_preference());
        String instPreference;

        int index = mDonationBuilder.indexOf(preference);

        if (index >= 0) {

            int firstIndex = index == 0 ? index : index - 1;

            mDonationBuilder.replace(firstIndex, index + preference.length(),
                    "");
        }

        instPreference = mDonationBuilder.toString();

        return instPreference;
    }

    private List<ImageView> getDonationImageView(String userDonation) {
        String instDonPreference = mInstitution.getDonation_preference();
        List<ImageView> listImg = new ArrayList<ImageView>();

        instDonPreference = removePreference(userDonation);

        if (instDonPreference != null) {

            String[] arrayDon = instDonPreference.split("-");

            if (arrayDon.length > 0) {

                for (String donatinon : arrayDon) {
                    if (!donatinon.isEmpty()) {
                        if (donatinon.equals(getResources().getString(
                                R.string.material_limpeza))) {

                            listImg.add(createImageDonation(R.drawable.ic_limpeza_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.cesta_basica))) {

                            listImg.add(createImageDonation(R.drawable.ic_cesta_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.higiene_pessoal))) {

                            listImg.add(createImageDonation(R.drawable.ic_higiene_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.material_pedagogico))) {

                            listImg.add(createImageDonation(R.drawable.ic_pedagogico_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.remedio))) {

                            listImg.add(createImageDonation(R.drawable.ic_remedios_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.brinquedos))) {

                            listImg.add(createImageDonation(R.drawable.ic_brinquedos_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.outros))) {

                            listImg.add(createImageDonation(R.drawable.ic_outros_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.recursos_financeiros))) {

                            listImg.add(createImageDonation(R.drawable.ic_financeiro_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.toalha_banho))) {

                            listImg.add(createImageDonation(R.drawable.ic_banho_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.toalha_mesa))) {

                            listImg.add(createImageDonation(R.drawable.ic_mesa_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.roupas))) {

                            listImg.add(createImageDonation(R.drawable.ic_roupa_off));

                        } else if (donatinon.equals(getResources().getString(
                                R.string.lencol))) {

                            listImg.add(createImageDonation(R.drawable.ic_lencol_off));

                        }
                    }
                }
            }
        }

        return listImg;
    }

    private ImageView createImageDonation(int donationID) {

        ImageView img = new ImageView(this);
        img.setImageResource(donationID);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 2, 5, 2);

        img.setLayoutParams(lp);

        return img;

    }

    private ImageView getUserPreferenceDonationView(String donation) {

        ImageView img = null;

        if (!donation.isEmpty()) {
            if (donation.equals(getResources().getString(
                    R.string.material_limpeza))) {

                img = createImageDonation(R.drawable.ic_limpeza_on);

            } else if (donation.equals(getResources().getString(
                    R.string.cesta_basica))) {

                img = createImageDonation(R.drawable.ic_cesta_on);

            } else if (donation.equals(getResources().getString(
                    R.string.higiene_pessoal))) {

                img = createImageDonation(R.drawable.ic_higiene_on);

            } else if (donation.equals(getResources().getString(
                    R.string.material_pedagogico))) {

                img = createImageDonation(R.drawable.ic_pedagogico_on);

            } else if (donation.equals(getResources().getString(
                    R.string.remedio))) {

                img = createImageDonation(R.drawable.ic_remedios_on);

            } else if (donation.equals(getResources().getString(
                    R.string.brinquedos))) {

                img = createImageDonation(R.drawable.ic_brinquedos_on);

            } else if (donation.equals(getResources()
                    .getString(R.string.outros))) {

                img = createImageDonation(R.drawable.ic_outros_on);

            } else if (donation.equals(getResources().getString(
                    R.string.recursos_financeiros))) {

                img = createImageDonation(R.drawable.ic_financeiro_on);

            } else if (donation.equals(getResources().getString(
                    R.string.toalha_banho))) {

                img = createImageDonation(R.drawable.ic_banho_on);

            } else if (donation.equals(getResources().getString(
                    R.string.toalha_mesa))) {

                img = createImageDonation(R.drawable.ic_mesa_on);

            } else if (donation.equals(getResources()
                    .getString(R.string.roupas))) {

                img = createImageDonation(R.drawable.ic_roupa_on);

            } else if (donation.equals(getResources()
                    .getString(R.string.lencol))) {
                img = createImageDonation(R.drawable.ic_lencol_on);

            }

        }

        return img;

    }

    private String getToken() {
        SharedPreferences sp = getSharedPreferences(
                DoeUtils.CURRENT_USER_SP, MODE_PRIVATE);

        return sp.getString(DoeUtils.TOKEN_SP, "");

    }

    private void createProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.waiting));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
    }

    private List<Donation> filterDonationUserByCurrentInstitution(User user) {

        List<Donation> result = new ArrayList<Donation>();

        for (Donation donation : user.getDonationList()) {
            if (donation.getInstitution_id() == mInstitution.getId()) {
                result.add(donation);
            }
        }

        return result;
    }

    @Override
    public void onSuccess(Object resultObj) {
        mProgressDialog.dismiss();

        countDonation++;
        txtCountNumber.setText(String.valueOf(countDonation));
        txtCounterName.setText(getResources().
                getQuantityString(R.plurals.user_donation_action, countDonation, countDonation));

        txtCountNumber.invalidate();
        txtCounterName.invalidate();
    }

    @Override
    public void onFailure(String msg) {
        mProgressDialog.dismiss();
    }
}
