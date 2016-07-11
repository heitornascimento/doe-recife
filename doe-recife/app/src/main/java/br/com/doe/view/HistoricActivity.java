package br.com.doe.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.doe.R;
import br.com.doe.core.HistoricAdapter;
import br.com.doe.core.HistoricList;
import br.com.doe.model.Institution;
import br.com.doe.model.User;

public class HistoricActivity extends ListActivity {

	private String mDonationType;
	private User mUser;
	private int mDonationIdImage;
	private ArrayList<HashMap<Integer, Integer>> historicInstitution;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historic);
		setDonationType(getIntent());
		loadActionBar();
	}

	private void setDonationType(Intent intent) {
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				mDonationType = bundle.getString(getResources().getString(
						R.string.parameter_donation_type));
				mUser = intent.getParcelableExtra(getResources().getString(
						R.string.user));
				mDonationIdImage = intent.getIntExtra(
						getResources().getString(R.string.donation_id), -1);

				HistoricList historicInstitutionExtra = (HistoricList) intent
						.getSerializableExtra("countList");

				if (historicInstitutionExtra != null) {

					historicInstitution = historicInstitutionExtra
							.getHistoricInstitution();
				}

				if (mUser != null) {
					loadListData(mUser);
				}
			}
		}
	}

	private void loadListData(User mUser) {

		HistoricAdapter adapter = new HistoricAdapter(getApplicationContext(),
				R.layout.item_list_institution, new ArrayList<Institution>(),
				mDonationType, historicInstitution);
		setListAdapter(adapter);

		List<Institution> instituions = mUser.getInstitutionList();

		for (Institution institution : instituions) {

			adapter.add(institution);

		}

		adapter.notifyDataSetChanged();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.historic, menu);
		return true;
	}

	private void loadActionBar() {
		Typeface type = Typeface.createFromAsset(getAssets(),
				"Mission Gothic Regular.otf");

		ActionBar actionbar = this.getActionBar();
		actionbar.setBackgroundDrawable(new ColorDrawable(0xF0F0F0));

		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);
		getActionBar().setHomeButtonEnabled(true);

		actionbar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbar_custom_view, null);

		TextView tv = ((TextView) v.findViewById(R.id.action_title));
		tv.setText(mDonationType);
		tv.setTextColor(Color.parseColor("#9b9b9b"));
		tv.setTypeface(type);

		ImageView imgActionbar = (ImageView) v.findViewById(R.id.img_donation);

		if (mDonationIdImage != -1)
			imgActionbar.setBackgroundResource(mDonationIdImage);

		actionbar.setCustomView(v);

	}

}
