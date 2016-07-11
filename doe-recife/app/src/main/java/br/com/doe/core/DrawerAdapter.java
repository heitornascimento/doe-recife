package br.com.doe.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import br.com.doe.R;
import br.com.doe.view.DrawerItem;

/**
 * Created by heitornascimento on 4/20/16.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    List<DrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context ctx;

    public DrawerAdapter(Context ctx, List<DrawerItem> data) {
        this.ctx = ctx;
        this.data = data;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.drawer_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DrawerItem di = data.get(position);
        holder.type.setText(di.getType());
        holder.count.setText(di.getCount());
        Drawable iconDrawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            iconDrawable = ctx.getResources().getDrawable(di.getIcon(), null);
        } else {
            iconDrawable = ctx.getResources().getDrawable(di.getIcon());
        }
        holder.icon.setImageDrawable(iconDrawable);
    }


    @Override
    public int getItemCount() {

        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView type;
        TextView count;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.type);
            count = (TextView) itemView.findViewById(R.id.count);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
