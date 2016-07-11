package br.com.doe.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.doe.R;
import br.com.doe.model.Institution;

/**
 * Created by heitornascimento on 5/23/16.
 */
public class DistrictAdapter extends BaseAdapter {

    private static final String SPINNER_VIEW = "spinner_view";
    private List<String> mData;
    private LayoutInflater mLayoutInflater;

    public DistrictAdapter(Context ctx, List<String> data) {
        this.mData = data;
        this.mLayoutInflater =
                (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null ||
                !convertView.getTag().toString().equals(SPINNER_VIEW)){
            convertView =
                    mLayoutInflater.inflate(R.layout.spinner_item, parent, false);
            convertView.setTag(SPINNER_VIEW);
        }

        TextView neighborhood = (TextView)convertView.findViewById(R.id.spinner_item);
        neighborhood.setText(mData.get(position));

        return  convertView;
    }
}
