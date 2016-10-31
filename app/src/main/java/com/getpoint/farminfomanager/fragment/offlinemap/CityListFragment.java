package com.getpoint.farminfomanager.fragment.offlinemap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.utils.adapters.offlinemapadapter.CityListAdapter;
import com.getpoint.farminfomanager.utils.adapters.offlinemapadapter.CityListParent;

import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListFragment extends Fragment {

    private static final String TAG = "CityListFragment";

    private MKOfflineMap offlineMap = null;
    private SearchView mCitySearch;
    private RecyclerView cityListrv;

    private List<CityListParent> allCities;
    CityListAdapter mAdapter;

    public static CityListFragment newInstance() {

        CityListFragment instance = new CityListFragment();
        return instance;

    }

    public void setAllCities(List<CityListParent> c) {
        this.allCities = c;
    }

    public void setmAdapter(CityListAdapter a) {
        this.mAdapter = a;
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

        cityListrv = (RecyclerView) v.findViewById(R.id.city_list_rv);
        cityListrv.setAdapter(mAdapter);
        cityListrv.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }
}
