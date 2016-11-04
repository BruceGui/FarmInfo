package com.getpoint.farminfomanager.utils.adapters.offlinemapadapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.ParentViewHolder;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListParentViewHolder extends ParentViewHolder {

    private static final String TAG = "CityListParent";

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private TextView mProvince;
    private TextView mFileSize;
    private ImageView mExpandImgBtn;

    public CityListParentViewHolder(@NonNull View itemView) {
        super(itemView);
        mProvince = (TextView) itemView.findViewById(R.id.id_province_name);
        mFileSize = (TextView) itemView.findViewById(R.id.id_city_map_size);
        mExpandImgBtn = (ImageView) itemView.findViewById(R.id.id_pro_expand_img_vi);
    }

    public void bind(@NonNull MKOLSearchRecord c) {
        mProvince.setText(c.cityName);
        //if(c.getCityType() != 1) {
        //    Log.i(TAG, "City Type: " + c.getCityType());
        //    mFileSize.setText(c.getFileSize());
        //    mExpandImgBtn.setBackgroundResource(R.drawable.ic_file_download_black_24dp);
        //}
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                mExpandImgBtn.setImageResource(R.drawable.ic_expand_less_black_24dp);
            } else {
                mExpandImgBtn.setImageResource(R.drawable.ic_expand_more_black_24dp);
            }
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            RotateAnimation rotateAnimation;

            if (expanded) {
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else {
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
        }
    }
}
