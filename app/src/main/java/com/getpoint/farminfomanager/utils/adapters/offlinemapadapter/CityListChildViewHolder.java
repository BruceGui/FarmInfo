package com.getpoint.farminfomanager.utils.adapters.offlinemapadapter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.utils.MKOfflineMapDownloadListener;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.ChildViewHolder;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListChildViewHolder extends ChildViewHolder {

    private static final String TAG = "CityList";

    private TextView mCityName;
    private TextView mCityMapSize;
    private ImageButton mDownload;

    private MKOfflineMapDownloadListener listener;

    public CityListChildViewHolder(@NonNull View itemView,
                                   final MKOfflineMapDownloadListener listener) {

        super(itemView);

        mCityName = (TextView) itemView.findViewById(R.id.id_city_name);
        mCityMapSize = (TextView) itemView.findViewById(R.id.id_city_map_size);
        this.listener = listener;
        mDownload = (ImageButton) itemView.findViewById(R.id.id_start_download);


    }

    public void bind(@NonNull final CityDetail city) {

        mCityName.setText(city.getCityName());
        mCityMapSize.setText(city.getFileSize());

        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Listener");
                listener.startDownload(city);
            }
        });

    }

}
