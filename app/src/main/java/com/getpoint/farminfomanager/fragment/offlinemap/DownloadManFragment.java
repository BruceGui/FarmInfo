package com.getpoint.farminfomanager.fragment.offlinemap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.getpoint.farminfomanager.R;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class DownloadManFragment extends Fragment {

    private MKOfflineMap offlineMap = null;

    public static DownloadManFragment newInstance() {

        DownloadManFragment instance = new DownloadManFragment();

        return instance;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.download_manager_fragment, container, false);

        return v;
    }

    public void setMKOfflineMap(MKOfflineMap m) {
        this.offlineMap = m;
    }
}
