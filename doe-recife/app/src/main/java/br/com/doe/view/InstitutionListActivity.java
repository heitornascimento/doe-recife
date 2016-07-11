package br.com.doe.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import br.com.doe.R;
import br.com.doe.core.DistrictAdapter;
import br.com.doe.core.InstitutionRecycleAdapter;
import br.com.doe.model.Institution;
import br.com.doe.model.User;
import br.com.doe.presenter.InstitutionsPresenter;
import br.com.doe.utils.DoeUtils;
import br.com.doe.utils.TaskEnum;

public class InstitutionListActivity extends BaseActivity implements
        InstitutionsPresenter.Presentable, InstitutionsPresenter.Addressable,
        OnMapReadyCallback, View.OnClickListener,
        ClusterManager.OnClusterClickListener<Institution>, ClusterManager.OnClusterInfoWindowClickListener<Institution>,
        ClusterManager.OnClusterItemClickListener<Institution>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Institution>, AdapterView.OnItemSelectedListener, RecyclerViewClickListener.OnItemClickListener {


    private MapFragment mMap;
    private GoogleMap mGoogleMap;
    private ClusterManager<Institution> mClusterManager;

    private ImageView imgFullScreen = null;
    private FrameLayout flList = null;
    private FrameLayout flMap;
    private Spinner mSpinner;

    private RecyclerView mRecyclerView;
    private InstitutionRecycleAdapter mAdapter;

    private ArrayList<Institution> mData = new ArrayList<Institution>();
    private List<String> mDistricts = new ArrayList<String>();

    private String mDonationType;
    private int mImageId;
    private User mUser;

    private ProgressDialog mDialog;
    private InstitutionsPresenter mPresenter;
    private Toolbar mToolBar;

    private final static String DOWNLOAD_STATUS = "download_status";
    private final static String USER_PARAM = "user_param";
    private final static String DONATION_PARAM = "donation_param";
    private TaskEnum mTaskStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.institution_list);
        mMap = (MapFragment) getFragmentManager().findFragmentById(R.id.institutionsMap);
        mMap.getMapAsync(this);

        mPresenter = new InstitutionsPresenter(this, getApplicationContext());

        imgFullScreen = (ImageView) findViewById(R.id.img_full_screen);
        imgFullScreen.setOnClickListener(this);
        flList = (FrameLayout) findViewById(R.id.fl_list);
        flMap = (FrameLayout) findViewById(R.id.fl_map);

        mRecyclerView = (RecyclerView) findViewById(R.id.institution_list);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this, this));
        //improve performance
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InstitutionRecycleAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);

        mSpinner = (Spinner) findViewById(R.id.districts);
        configToolbar();

        mUser = getIntent().getParcelableExtra(getResources().getString(R.string.user));
        mDonationType = getIntent().getStringExtra(getResources().getString(
                R.string.donation));

        mTaskStatus = TaskEnum.START;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.setForeground(true);
        if (mTaskStatus != TaskEnum.DONE) {
            loadData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.setForeground(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable(DOWNLOAD_STATUS, mTaskStatus);
        outState.putParcelable(USER_PARAM, mUser);
        outState.putString(USER_PARAM, mDonationType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            TaskEnum taskStatus = (TaskEnum)
                    savedInstanceState.getSerializable(DOWNLOAD_STATUS);
            if (taskStatus != null) {
                mTaskStatus = taskStatus;
            }
            User user = savedInstanceState.getParcelable(USER_PARAM);
            if (user != null) {
                mUser = user;
            }

            String donationType = savedInstanceState.getString(DONATION_PARAM);
            if (donationType != null && !donationType.equals("")) {
                mDonationType = donationType;
            }
        }
    }

    /**
     * Setup basic properties of the toolbar.
     */
    private void configToolbar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBar.getMenu().clear();
        getSupportActionBar().setTitle(getResources().getString(R.string.institutions_tool_bar));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Load the institution by the Presenter.
     */
    private void loadData() {

        if (checkInternetConnection()) {
            //Set progress
            mDialog = new ProgressDialog(this);
            mDialog.setIndeterminate(true);
            mDialog.setCancelable(false);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setMessage(getResources().getString(R.string.loading_institutions));
            mDialog.show();
            //Load all institution
            mPresenter.loadInstitutions(mDonationType, getToken());
        }
        mTaskStatus = TaskEnum.START;
    }


    @Override
    public void onSuccess(Object resultObj) {

        mData = (ArrayList<Institution>) resultObj;
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
        mDialog.dismiss();

        if (mGoogleMap == null) {
            //TODO
            //launch error
        } else {

            mClusterManager = new ClusterManager<Institution>(this, mGoogleMap);
            mGoogleMap.setOnCameraChangeListener(mClusterManager);
            mGoogleMap.setOnMarkerClickListener(mClusterManager);
            mGoogleMap.setOnInfoWindowClickListener(mClusterManager);
            mClusterManager.setOnClusterClickListener(this);
            mClusterManager.setOnClusterInfoWindowClickListener(this);
            mClusterManager.setOnClusterItemClickListener(this);
            mClusterManager.setOnClusterItemInfoWindowClickListener(this);
            loadMapConfig();
        }

        mTaskStatus = TaskEnum.DONE;
    }

    @Override
    public void onFailure(String msg) {
        mTaskStatus = TaskEnum.INCOMPLETE;
        mDialog.dismiss();
        Snackbar snackbar = Snackbar.
                make(findViewById(R.id.cl),
                        msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.clear();
    }

    /**
     * Add the data into ClusterManager list.
     *
     * @param clusterManager
     */
    private void addInstitutionToCluster(ClusterManager<Institution> clusterManager) {
        for (Institution inst : mData) {
            clusterManager.addItem(inst);
        }
    }

    @Override
    public void onClick(View v) {

        if (checkInternetConnection()) {
            if (v.getId() == R.id.img_full_screen) {
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    showMapFullscreen(true);
                } else {
                    showMapFullscreen(false);
                }
            }
        }
    }

    /**
     * Calculates the average point to be able to center the map.
     *
     * @return
     */
    private LatLng getAvgLatLgn() {
        LatLng latLng = null;
        Double lat = (double) 0;
        Double lng = (double) 0;
        for (Institution item : mData) {
            lat += Double.parseDouble(item.getLat());
            lng += Double.parseDouble(item.getLgn());
        }
        latLng = new LatLng((lat / mData.size()) - (mData.size() > 1 ? 0.01 : 0.0),
                lng / mData.size());

        return latLng;
    }

    /**
     * Enable fullscreen map.
     */
    private void showMapFullscreen(boolean isShow) {

        PercentRelativeLayout.LayoutParams params =
                (PercentRelativeLayout.LayoutParams) flMap.getLayoutParams();

        PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo
                = params.getPercentLayoutInfo();

        if (isShow) {
            mRecyclerView.setVisibility(View.GONE);
            if (params != null) {
                percentLayoutInfo.heightPercent = 1.0f;
            }
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            if (params != null) {
                percentLayoutInfo.heightPercent = 0.30f;
            }
        }
        flMap.requestLayout();
    }


    private String getToken() {
        SharedPreferences sp = getSharedPreferences(DoeUtils.CURRENT_USER_SP, MODE_PRIVATE);
        return sp.getString(DoeUtils.TOKEN_SP, "");
    }

    @Override
    public void onReceiveDistrictList(List<String> districts) {

        mDistricts = districts;

        if (mSpinner != null) {
            DistrictAdapter districtAdapter =
                    new DistrictAdapter(this, districts);
            mSpinner.setAdapter(districtAdapter);
            mSpinner.setOnItemSelectedListener(this);
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Institution> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Institution> cluster) {

    }

    @Override
    public boolean onClusterItemClick(Institution institution) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Institution institution) {
        Log.i(DEBUG, "onClusterItemInfoWindowClick");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.institution_list, menu);
        return false;
    }

    /**
     * When an item from spinner is selected, reload the recycler view data.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selected = mDistricts.get(position);

        if (checkInternetConnection() && selected != null && !selected.equals("")) {
            mData = mPresenter.loadInstitutionPerDistrict(selected);
            mAdapter.setData(mData);
            mAdapter.notifyDataSetChanged();
            mGoogleMap.clear();
            mClusterManager.clearItems();
            loadMapConfig();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Set the configuration of the map according to the data.
     */
    private void loadMapConfig() {

        addInstitutionToCluster(mClusterManager);
        mClusterManager.setRenderer(new InstitutionRender(this, mGoogleMap, mClusterManager));
        mClusterManager.cluster();
        LatLng latLng = getAvgLatLgn();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

    }

    /**
     * Handle click of RecyclerView
     *
     * @param v
     * @param position
     */
    @Override
    public void onItemClick(View v, int position) {

        if (checkInternetConnection()) {
            Institution institution = mData.get(position);
            callInstitutionDetail(institution);
        }
    }

    /**
     * Show the institution details
     *
     * @param institution
     */
    private void callInstitutionDetail(@NonNull Institution institution) {

        Intent intent = new Intent(this, InstitutionActivity.class);
        intent.putExtra(getResources().getString(R.string.institution),
                institution);
        intent.putExtra(
                getResources().getString(R.string.user_preference_donaton),
                mDonationType);
        intent.putExtra(getResources().getString(R.string.user), mUser);
        startActivity(intent);
    }
}
