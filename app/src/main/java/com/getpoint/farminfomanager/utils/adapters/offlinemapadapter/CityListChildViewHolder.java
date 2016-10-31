package com.getpoint.farminfomanager.utils.adapters.offlinemapadapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.ChildViewHolder;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListChildViewHolder extends ChildViewHolder {

    private TextView mCityName;
    private TextView mCityMapSize;
    private ImageButton mDownload;

    public CityListChildViewHolder(@NonNull View itemView) {

        super(itemView);

        mCityName = (TextView) itemView.findViewById(R.id.id_city_name);
        mCityMapSize = (TextView) itemView.findViewById(R.id.id_city_map_size);
        mDownload = (ImageButton) itemView.findViewById(R.id.id_start_download);

    }

    public void bind(String cityName) {

        mCityName.setText(cityName);

    }

}
