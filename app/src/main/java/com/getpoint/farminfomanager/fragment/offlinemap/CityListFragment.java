package com.getpoint.farminfomanager.fragment.offlinemap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.utils.adapters.offlinemapadapter.CityListAdapter;
import com.getpoint.farminfomanager.utils.adapters.offlinemapadapter.CityListParent;

import static com.getpoint.farminfomanager.entity.offlinemap.CityDetail.formatDataSize;

import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListFragment extends Fragment {

    private static final String TAG = "CityListFragment";

    private MKOfflineMap offlineMap = null;
    private SearchView mCitySearch;
    private RecyclerView cityListrv;

    private List<MKOLSearchRecord> searchRes;
    private List<CityListParent> allCities;
    private CityListAdapter mAdapter;
    private SearchResAdapter searchResAdapter;

    private DownloadManFragment.OnOfflineMapDownloadListener listener;

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

    public void setDownloadListener(DownloadManFragment.OnOfflineMapDownloadListener l) {
        this.listener = l;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.city_list_fragment, container, false);

        searchResAdapter = new SearchResAdapter();

        mCitySearch = (SearchView) v.findViewById(R.id.id_search_city);
        mCitySearch.setIconifiedByDefault(false);
        mCitySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(offlineMap != null) {
                    searchRes = offlineMap.searchCity(query);
                    cityListrv.setAdapter(searchResAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.equals("")) {
                    cityListrv.setAdapter(mAdapter);
                } else {
                    searchRes = offlineMap.searchCity(newText);
                    cityListrv.setAdapter(searchResAdapter);
                }
                return false;
            }
        });


        cityListrv = (RecyclerView) v.findViewById(R.id.city_list_rv);
        cityListrv.setAdapter(mAdapter);
        cityListrv.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    public class SearchResAdapter extends RecyclerView.Adapter<SearchResAdapter.SearchResViewHolder> {


        @Override
        public SearchResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SearchResViewHolder(LayoutInflater.from(getContext())
                .inflate(R.layout.city_list_child_view_holder, parent, false));
        }

        @Override
        public void onBindViewHolder(SearchResViewHolder holder, final int position) {

            final MKOLSearchRecord r = searchRes.get(position);

            holder.mCityName.setText(r.cityName);
            holder.mCityMapSize.setText(formatDataSize(r.size));
            holder.mDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CityDetail c = new CityDetail(r.cityID,r.cityName,r.size,r.cityType);
                    if(listener != null) {
                        listener.startDownload(c);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return searchRes.size();
        }

        class SearchResViewHolder extends ViewHolder {

            private TextView mCityName;
            private TextView mCityMapSize;
            private ImageButton mDownload;

            public SearchResViewHolder(View v) {
                super(v);
                mCityName = (TextView) v.findViewById(R.id.id_city_name);
                mCityMapSize = (TextView) v.findViewById(R.id.id_city_map_size);
                mDownload = (ImageButton) v.findViewById(R.id.id_start_download);
            }
        }

    }

    public void setMKOfflineMap(MKOfflineMap m) {
        this.offlineMap = m;
    }
}
