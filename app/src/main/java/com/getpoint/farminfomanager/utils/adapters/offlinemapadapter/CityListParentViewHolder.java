package com.getpoint.farminfomanager.utils.adapters.offlinemapadapter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.ParentViewHolder;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListParentViewHolder extends ParentViewHolder {

    private static final String TAG = "CityListParent";

    private TextView mProvince;
    private TextView mFileSize;
    private ImageButton mExpandImgBtn;

    public CityListParentViewHolder(@NonNull View itemView) {
        super(itemView);
        mProvince = (TextView) itemView.findViewById(R.id.id_province_name);
        mFileSize = (TextView) itemView.findViewById(R.id.id_city_map_size);
        mExpandImgBtn = (ImageButton) itemView.findViewById(R.id.id_pro_expand_img_btn);
    }

    public void bind(@NonNull CityDetail c) {
        mProvince.setText(c.getCityName());
        //if(c.getCityType() != 1) {
        //    Log.i(TAG, "City Type: " + c.getCityType());
        //    mFileSize.setText(c.getFileSize());
        //    mExpandImgBtn.setBackgroundResource(R.drawable.ic_file_download_black_24dp);
        //}
    }
}
