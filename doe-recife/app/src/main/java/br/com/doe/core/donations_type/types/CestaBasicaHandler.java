/**
 * 
 */
package br.com.doe.core.donations_type.types;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import br.com.doe.R;
import br.com.doe.core.HistoricList;
import br.com.doe.model.User;
import br.com.doe.view.HistoricActivity;

/**
 * @author heitornsouza
 * 
 */
public class CestaBasicaHandler extends DonationHandler {

	private Context ctx;
	private User mUser;

	/**
	 * @param id
	 * @param context
	 */
	public CestaBasicaHandler(int id, Context context) {
		super(id, context, context.getResources().getString(
				R.string.cesta_basica));
		this.ctx = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.doe.core.donations_type.DonationHandler#handle(java.lang.String,
	 * android.content.Context)
	 */
	@Override
	public void handle(String donationType, Context ctx, User user) {

		if (donationType
				.equals(getResources().getString(R.string.cesta_basica))) {
			mUser = user;
			buildView(this.imageID, ctx);
		} else {
			passTheResponsability(donationType, ctx, user);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(ctx.getApplicationContext(),
				HistoricActivity.class);
		intent.putExtra(
				getResources().getString(R.string.parameter_donation_type),
				getResources().getString(R.string.cesta_basica));
		intent.putExtra(
				getResources().getString(R.string.user),
				createFilterUser(mUser,
						getResources().getString(R.string.cesta_basica)));
		intent.putExtra(getResources().getString(R.string.donation_id),
				R.drawable.ic_cesta_blue);
		HistoricList hl = new HistoricList(this.getHistoricInstitution());
		intent.putExtra("countList", hl);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);

	}

}
