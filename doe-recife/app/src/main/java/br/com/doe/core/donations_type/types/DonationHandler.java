/**
 * 
 */
package br.com.doe.core.donations_type.types;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.doe.model.Donation;
import br.com.doe.model.Institution;
import br.com.doe.model.User;

/**
 * @author heitornsouza
 * 
 */
public abstract class DonationHandler extends View implements OnClickListener {

	private int mCounter = 0;
	private DonationHandler mSuccessor;
	private boolean hasInitialize;
	protected int imageID;

	private LinearLayout donationView;
	private String category;
	private ArrayList<HashMap<Integer, Integer>> historicInstitution = new ArrayList<HashMap<Integer, Integer>>();

	/**
	 * @param context
	 */
	public DonationHandler(int id, Context context, String category) {
		super(context);
		this.imageID = id;
		this.category = category;
	}

	/**
	 * @return the historicInstitution
	 */
	protected ArrayList<HashMap<Integer, Integer>> getHistoricInstitution() {
		return historicInstitution;
	}

	/**
	 * @param historicInstitution
	 *            the historicInstitution to set
	 */
	protected void setHistoricInstitution(
			ArrayList<HashMap<Integer, Integer>> historicInstitution) {
		this.historicInstitution = historicInstitution;
	}

	/**
	 * @return the hasInitialize
	 */
	public boolean isHasInitialize() {
		return hasInitialize;
	}

	/**
	 * @param hasInitialize
	 *            the hasInitialize to set
	 */
	public void setHasInitialize(boolean hasInitialize) {
		this.hasInitialize = hasInitialize;
	}

	/**
	 * @return the counter
	 */
	public int getCounter() {
		return mCounter;
	}

	/**
	 * @param counter
	 *            the counter to set
	 */
	public void setCounter(int mCounter) {
		this.mCounter = mCounter;
	}

	/**
	 * @return the mSuccessor
	 */
	public DonationHandler getSuccessor() {
		return mSuccessor;
	}

	/**
	 * @param mSuccessor
	 *            the mSuccessor to set
	 */
	public void setSuccessor(DonationHandler mSuccessor) {
		this.mSuccessor = mSuccessor;
	}

	protected User createFilterUser(User user, String type) {

		User filterUser = new User();
		int countDonation = 0;

		for (Donation donation : user.getDonationList()) {

			HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
			int id = -1;

			for (Institution institution : user.getInstitutionList()) {

				if (donation.getInstitution_id() == institution.getId()
						&& donation.getDonation_description().contains(type)) {

					if (id != donation.getId()) {
						id = donation.getId();
						countDonation++;
						hash.put(institution.getId(), countDonation);

					}
					if (!filterUser.getInstitutionList().contains(institution)) {
						filterUser.setInstitutionList(institution);
					}
				}
			}
			countDonation = 0;
			if (!hash.isEmpty())
				this.getHistoricInstitution().add(hash);

		}

		return filterUser;

	}

	public void createViewDonation(Context ctx) {

		donationView = new LinearLayout(ctx);
		donationView.setOrientation(LinearLayout.HORIZONTAL);

		ImageView img = new ImageView(ctx);
		img.setImageResource(imageID);

		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp.setMargins(7, 5, 0, 0);
		donationView.setLayoutParams(rl);

		img.setLayoutParams(rl);
		img.setPadding(8, 15, 8, 0);

		Typeface type = Typeface.createFromAsset(ctx.getAssets(),
				"Mission Gothic Bold.otf");

		TextView tvCategory = new TextView(ctx);
		tvCategory.setText(category);
		tvCategory.setPadding(8, 8, 8, 8);
		tvCategory.setTextSize(16);
		tvCategory.setTypeface(type);
		rl = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rl.addRule(RelativeLayout.RIGHT_OF, img.getId());
		tvCategory.setLayoutParams(rl);

		rl = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		TextView tv = new TextView(ctx);
		tv.setText(String.valueOf(mCounter));
		tv.setPadding(8, 8, 8, 8);
		tv.setTypeface(type);
		tv.setTextSize(16);
		tv.setTextColor(Color.parseColor("#00AEEF"));
		tv.setLayoutParams(rl);

		donationView.setOnClickListener(this);

		donationView.addView(img);
		donationView.addView(tvCategory);
		donationView.addView(tv);

	}

	/**
	 * Responsible to handle the DonationHandler or pass to another.
	 **/
	public abstract void handle(String donationType, Context ctx, User user);

	protected void buildView(int imageID, Context ctx) {
		int count = getCounter();
		count++;
		setCounter(count);
		setHasInitialize(true);
		createViewDonation(ctx);
	}

	protected void passTheResponsability(String donationType, Context ctx,
			User user) {
		if (getSuccessor() != null) {
			getSuccessor().handle(donationType, ctx, user);
		} else {
			throw new RuntimeException("Donation Type NOT FOUND");

		}
	}

	public View getDonationView() {
		return this.donationView;
	}

}
