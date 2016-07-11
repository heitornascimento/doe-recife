/**
 * 
 */
package br.com.doe.core;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.doe.R;
import br.com.doe.model.Institution;

/**
 * @author heitornsouza
 * 
 */
public class HistoricAdapter extends ArrayAdapter<Institution> {

	private Context ctx;
	private ArrayList<Institution> data = new ArrayList<Institution>();
	private static final int DISTRICT = 1;
	private String mDonationType;
	private ArrayList<HashMap<Integer, Integer>> historicInstitution;

	public HistoricAdapter(Context context, int resource,
			ArrayList<Institution> objects, String donationType,
			ArrayList<HashMap<Integer, Integer>> historicInstitution) {
		super(context, resource, objects);

		this.mDonationType = donationType;
		this.ctx = context;
		this.data = objects;
		this.historicInstitution = historicInstitution;
	}

	private String getDistrict(Institution institution) {
		String address = institution.getAddress();
		String district = "";

		String[] addressArray = address.split("-");
		if (addressArray.length > 0 && addressArray[DISTRICT] != null) {
			district = addressArray[DISTRICT];
		}

		return district;
	}

	static class ViewHolder {
		TextView txtName;
		TextView txtDistrict;
		TextView txtCount;
		TextView txtCountName;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_historic, parent,
					false);

			viewHolder = new ViewHolder();
			viewHolder.txtDistrict = (TextView) convertView
					.findViewById(R.id.institution_district);
			viewHolder.txtName = (TextView) convertView
					.findViewById(R.id.institution_name);

			viewHolder.txtCountName = (TextView) convertView
					.findViewById(R.id.institution_count_text);
			viewHolder.txtCount = (TextView) convertView
					.findViewById(R.id.institution_count);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Institution institution = data.get(position);

		if (institution != null) {

			Typeface type = Typeface.createFromAsset(this.ctx.getAssets(),
					"Mission Gothic Regular.otf");

			viewHolder.txtName.setText(institution.getRazao_social());
			viewHolder.txtName.setTypeface(type);
			viewHolder.txtName.setTextColor(Color.BLACK);

			viewHolder.txtDistrict.setText(getDistrict(institution));
			viewHolder.txtDistrict.setTypeface(type);

			viewHolder.txtCountName.setTypeface(type);
			viewHolder.txtCount.setTypeface(type);

			Integer totalCount = 0;

			for (HashMap<Integer, Integer> countInst : historicInstitution) {

				if (countInst != null) {
					Integer amount = countInst.get(institution.getId());
					if (amount != null) {
						totalCount += amount;

						if (totalCount > 1) {
							viewHolder.txtCountName.setText("Doações");
						} else {
							viewHolder.txtCountName.setText("Doação");
						}

						viewHolder.txtCount.setText(totalCount + "");
					}

				}

			}

		}

		return convertView;
	}

}
