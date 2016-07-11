package br.com.doe.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.doe.R;
import br.com.doe.core.DrawerAdapter;
import br.com.doe.model.User;
import br.com.doe.utils.DoeUtils;


public class FragmentDrawer extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ActionBarDrawerToggle mDrawerToogle;
    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mDrawerAdapter;
    private View mContainerView;
    private List<DrawerItem> mData;
    private DrawerListener mListener;

    private TextView mTextUsername;
    private TextView mTextUserEmail;
    private TextView mTextUserDonationCount;
    private TextView mCountTextual;
    private TextView mLogout;
    private User mUser;

    public FragmentDrawer() {
        // Required empty public constructor
    }

    public void setDrawerListener(DrawerListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        mTextUsername = (TextView) layout.findViewById(R.id.user_name);
        mTextUserEmail = (TextView) layout.findViewById(R.id.user_email);
        mTextUserDonationCount = (TextView) layout.findViewById(R.id.countDonation);
        mCountTextual = (TextView) layout.findViewById(R.id.countTextual);
        mLogout = (TextView) layout.findViewById(R.id.logout);
        mLogout.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDrawerAdapter = new DrawerAdapter(getActivity(), mData);
        mRecyclerView.setAdapter(mDrawerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadUserInfo(mUser);
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    private void loadUserInfo(User user) {
        mTextUsername.setText(user.getName());
        mTextUserEmail.setText(user.getEmail());
        mTextUserDonationCount.setText(String.valueOf(user.getDonationList().size()));
        mCountTextual.setText(getResources().
                getQuantityString(R.plurals.donation_history,
                        user.getDonationList().size(), user.getDonationList().size()));
    }

    public void setData(List<DrawerItem> data) {
        this.mData = data;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout) {
            mListener.onLogout();
        }
    }

    public interface DrawerListener {
        public void onDrawerItemSelected(View view, int position);
        public void onLogout();
    }

}
