package br.com.doe.core;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.doe.R;
import br.com.doe.model.Institution;

/**
 * Created by heitornascimento on 4/26/16.
 */
public class InstitutionRecycleAdapter extends RecyclerView.Adapter<InstitutionRecycleAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {


    private ArrayList<Institution> mData;
    private ViewHolder mViewHolder;


    public InstitutionRecycleAdapter(ArrayList<Institution> list) {
        mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_list_institution, parent, false);
        mViewHolder = new ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Institution institution = mData.get(position);

        if(institution != null){

            holder.txtName.setText(institution.getRazao_social());
            holder.txtDistrict.setText(getDistrict(institution));
            position++;
            if (position < 10) {
                holder.txtIndex.setText("0" + String.valueOf(position));
            } else {
                holder.txtIndex.setText(String.valueOf(position));
            }

        }

    }

    public void setData(ArrayList<Institution> data) {
        mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());


    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtIndex;
        private TextView txtName;
        private TextView txtDistrict;

        public ViewHolder(View itemView) {
            super(itemView);
            txtIndex = (TextView) itemView.findViewById(R.id.txt_counter);
            txtName = (TextView) itemView.findViewById(R.id.institution_name);
            txtDistrict = (TextView) itemView.findViewById(R.id.institution_district);


        }
    }

    private String getDistrict(Institution institution) {
        String address = institution.getAddress();
        String district = "";

        String[] addressArray = address.split("-");
        if (addressArray.length > 0 && addressArray[1] != null) {
            district = addressArray[1];
        }

        return district;
    }

    /* Interface item click */

    public interface OnInstitutionClick{
        public void onItemClick(View v, int position);
    }
}
