package br.com.doe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import br.com.doe.R;
import br.com.doe.model.Institution;

/**
 * Created by heitornascimento on 5/8/16.
 * <p/>
 * Responsible...
 */
public class InstitutionRender extends DefaultClusterRenderer<Institution> {

    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;
    private final ImageView mImageView;
    private final ImageView mClusterImageView;
    private final int mDimension;
    private final TextView mIndex;

    private Context mContext;

    public InstitutionRender(Context context, GoogleMap map, ClusterManager<Institution> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;

        mIconGenerator = new IconGenerator(context.getApplicationContext());
        mClusterIconGenerator = new IconGenerator(context.getApplicationContext());
        View multiProfile = ((InstitutionListActivity) context).getLayoutInflater().inflate(R.layout.inst_map, null);
        mClusterIconGenerator.setContentView(multiProfile);
        mClusterImageView = (ImageView) multiProfile.findViewById(R.id.imageView);
        mIndex = (TextView) multiProfile.findViewById(R.id.index);

        mImageView = new ImageView(context);
        mDimension = (int) context.getResources().getDimension(R.dimen.custom_map_marker_image);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        //mIndex = ()
        int padding = (int) context.getResources().getDimension(R.dimen.custom_map_marker_padding);
        mImageView.setPadding(padding, padding, padding, padding);
        mIconGenerator.setContentView(mImageView);

    }


    @Override
    protected void onBeforeClusterItemRendered(Institution item, MarkerOptions markerOptions) {

        Bitmap bmp = buildMapCanvas(item);
        mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_marker_map));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bmp)).title(item.getRazao_social());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Institution> cluster, MarkerOptions markerOptions) {
        //Everytime a map is rendered, this method is called.

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(160, 160, conf);
        Canvas canvas = new Canvas(bmp);

        String count = String.valueOf(cluster.getSize());

        Paint color = new Paint();
        color.setStyle(Paint.Style.FILL);
        color.setColor(Color.BLACK);
        canvas.drawBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_cluster_map), 0, 0, color);

        Paint colorText = new Paint();
        colorText.setStyle(Paint.Style.FILL);
        colorText.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.map_text_size));
        colorText.setColor(Color.BLACK);

        Bitmap orig = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_cluster_map);

        if(cluster.getSize() < 10){
            count = "0"+cluster.getSize();
        }

        canvas.drawText(count, (orig.getWidth() / 3) - 5, orig.getHeight() / 2, colorText);

        //The size will be either 4 or less.
        List<Drawable> instMarkers = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
        int width = mDimension;
        int height = mDimension;

        for (Institution institution : cluster.getItems()) {

            //if (instMarkers.size() == 4) break;
            //Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_banho_white);
            //drawable.setBounds(0, 0, width, height);
            //instMarkers.add(drawable);
        }

        // MultiDrawable multiDrawable = new MultiDrawable(instMarkers);
        // multiDrawable.setBounds(0, 0, width, height);
        // mClusterImageView.setImageDrawable(multiDrawable);
        Log.i("doe", "before = "+cluster.getSize());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bmp));

    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<Institution> cluster) {
        return cluster.getSize() > 1;
    }

    /**
     * Custom Map Marker along with institution index.
     * @param item
     * @return
     */
    private Bitmap buildMapCanvas(Institution item) {

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(160, 160, conf);
        Canvas canvas = new Canvas(bmp);
        String indexStr = String.valueOf(item.index);

        Paint colorText = new Paint();
        colorText.setStyle(Paint.Style.FILL);
        colorText.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.map_text_size));
        colorText.setColor(Color.BLACK);

        Paint color = new Paint();
        color.setStyle(Paint.Style.FILL);
        color.setColor(Color.BLACK);

        canvas.drawBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_tagmap_default), 0, 0, color);
        Bitmap orig = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_tagmap_default);
        if(item.index < 10){
            indexStr = "0"+item.index;
        }
        canvas.drawText(indexStr, (orig.getWidth() / 3) - 5, orig.getHeight() / 2, colorText);
        return bmp;
    }

    private Bitmap buildClusterMarker(){
        return null;
    }


}
