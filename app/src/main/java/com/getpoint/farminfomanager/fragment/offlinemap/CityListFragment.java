package com.getpoint.farminfomanager.fragment.offlinemap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.getpoint.farminfomanager.R;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListFragment extends Fragment {

    private static final String TAG = "CityListFragment";

    private MKOfflineMap offlineMap = null;
    private SearchView mCitySearch;

    public static CityListFragment newInstance() {

        CityListFragment instance = new CityListFragment();

        return instance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.city_list_fragment, container, false);

        mCitySearch = (SearchView) v.findViewById(R.id.id_search_city);
        mCitySearch.setIconifiedByDefault(false);

        return v;
    }
}
