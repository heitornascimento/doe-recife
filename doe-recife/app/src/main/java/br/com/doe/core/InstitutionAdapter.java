package br.com.doe.core;

import java.util.ArrayList;

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
 * @author heitornsouza Copyright 2013. All rights reserved
 */
public class InstitutionAdapter extends ArrayAdapter<Institution> {

	private Context ctx;
	private ArrayList<Institution> data = new ArrayList<Institution>();
	private static final int DISTRICT = 1;

	public InstitutionAdapter(Context context, int resource,
			ArrayList<Institution> objects) {
		super(context, resource, objects);
		this.ctx = context;
		this.data = objects;
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
		TextView txtIndex;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_list_institution,
					parent, false);

			viewHolder = new ViewHolder();
			viewHolder.txtDistrict = (TextView) convertView
					.findViewById(R.id.institution_district);
			viewHolder.txtIndex = (TextView) convertView
					.findViewById(R.id.txt_counter);
			viewHolder.txtName = (TextView) convertView
					.findViewById(R.id.institution_name);

			// viewHolder.txtCountName = (TextView) convertView
			// .findViewById(R.id.institution_count_text);
			// viewHolder.txtCount = (TextView) convertView
			// .findViewById(R.id.institution_count);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Institution institution = data.get(position);

		if (institution != null) {

			Typeface type = Typeface.createFromAsset(this.ctx.getAssets(),
					"Mission Gothic Regular.otf");
			Typeface typeBold = Typeface.createFromAsset(this.ctx.getAssets(),
					"Mission Gothic Bold.otf");

			viewHolder.txtName.setText(institution.getRazao_social());
			viewHolder.txtName.setTypeface(type);
			viewHolder.txtName.setTextColor(Color.BLACK);

			viewHolder.txtDistrict.setText(getDistrict(institution));
			viewHolder.txtDistrict.setTypeface(type);

			// viewHolder.txtCountName.setTypeface(type);
			// viewHolder.txtCount.setTypeface(type);

			viewHolder.txtIndex.setTypeface(type);
			position++;
			if (position < 10) {
				viewHolder.txtIndex.setText("0" + String.valueOf(position));
			} else {
				viewHolder.txtIndex.setText(String.valueOf(position));
			}

		}

		return convertView;
	}

}
