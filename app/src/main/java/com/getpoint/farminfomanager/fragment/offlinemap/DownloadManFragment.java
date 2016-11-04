package com.getpoint.farminfomanager.fragment.offlinemap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.entity.offlinemap.OnDownCity;
import com.getpoint.farminfomanager.weights.numberprocessbar.NumberProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class DownloadManFragment extends Fragment {

    private static final String TAG = "DownloadMan";

    private MKOfflineMap offlineMap = null;

    private List<OnDownCity> mOnDownload;
    private List<MKOLUpdateElement> mDownloaded;

    private TextView mDoingText;

    private RecyclerView mDownloadrv;
    private RecyclerView mDownloadedrv;

    private OfflineMapDownloadAdapter mDowningAdapter;
    private OfflineMapHaveAdapter mHaveAdapter;

    public static DownloadManFragment newInstance() {

        DownloadManFragment instance = new DownloadManFragment();

        return instance;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (offlineMap != null) {
            mDownloaded = offlineMap.getAllUpdateInfo();
        }
        mOnDownload = new ArrayList<>();
    }

    public void setOnDowning(List<OnDownCity> c) {
        this.mOnDownload = c;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.download_manager_fragment, container, false);

        mDownloadrv = (RecyclerView) v.findViewById(R.id.id_downloading_rv);
        mDowningAdapter = new OfflineMapDownloadAdapter();
        mDownloadrv.setAdapter(mDowningAdapter);
        mDownloadrv.setLayoutManager(new LinearLayoutManager(getContext()));

        mDownloadedrv = (RecyclerView) v.findViewById(R.id.id_downloaded_rv);
        mHaveAdapter = new OfflineMapHaveAdapter();
        mDownloadedrv.setAdapter(mHaveAdapter);
        mDownloadedrv.setLayoutManager(new LinearLayoutManager(getContext()));

        mDoingText = (TextView) v.findViewById(R.id.id_offline_map_doing_text);
        mDoingText.setVisibility(View.INVISIBLE);

        return v;
    }

    /**
     * 正在下载的任务 适配器
     */
    public class OfflineMapDownloadAdapter extends RecyclerView.Adapter<OfflineMapDownloadAdapter.DownloadViewHolder> {

        @Override
        public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DownloadViewHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.offline_map_on_downloading, parent, false));
        }

        @Override
        public void onBindViewHolder(DownloadViewHolder holder, int position) {
            holder.mCityName.setText(mOnDownload.get(position).r.cityName);
            holder.mProgress.setProgress(mOnDownload.get(position).getRatio());
        }

        @Override
        public int getItemCount() {
            return mOnDownload.size();
        }

        class DownloadViewHolder extends ViewHolder {

            private TextView mCityName;
            private NumberProgressBar mProgress;

            public DownloadViewHolder(View v) {
                super(v);
                mCityName = (TextView) v.findViewById(R.id.id_down_city_name);
                mProgress = (NumberProgressBar) v.findViewById(R.id.id_down_progress);
            }

        }
    }

    /**
     * 已经下载的 适配器
     */
    public class OfflineMapHaveAdapter extends RecyclerView.Adapter<OfflineMapHaveAdapter.HaveViewHolder> {

        @Override
        public HaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HaveViewHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.offline_map_downloaded, parent, false));
        }

        @Override
        public void onBindViewHolder(HaveViewHolder holder, int position) {
            holder.mCityName.setText(mDownloaded.get(position).cityName);
        }

        @Override
        public int getItemCount() {
            if(mDownloaded != null) {
                return mDownloaded.size();
            }

            return 0;
        }

        class HaveViewHolder extends ViewHolder {

            private TextView mCityName;

            public HaveViewHolder(View v) {
                super(v);
                mCityName = (TextView) v.findViewById(R.id.id_down_city_name);
            }

        }

    }

    public void addDownloadCity(MKOLSearchRecord c) {
        mDoingText.setVisibility(View.VISIBLE);
        if (!mOnDownload.contains(c)) {
            mOnDownload.add(new OnDownCity(c));
        }
        notifyDatasetChanged();
    }

    public void updateProcess(MKOLUpdateElement u) {
        if (!mOnDownload.isEmpty()) {
            mOnDownload.get(0).setRatio(u.ratio);

            if (u.ratio == 100) {
                // mDownloaded.add(mOnDownload.get(0));
                removeDownloadCity();
                updateOfflineMapHave(u);
                mHaveAdapter.notifyDataSetChanged();
            }
            notifyDatasetChanged();
        }
    }

    public void notifyDatasetChanged() {
        mDowningAdapter.notifyDataSetChanged();
        if (this.mOnDownload.isEmpty()) {
            mDoingText.setVisibility(View.INVISIBLE);
        }
    }

    public void removeDownloadCity() {
        if (!this.mOnDownload.isEmpty()) {
            this.mOnDownload.remove(0);
        }
        notifyDatasetChanged();
    }

    public void updateOfflineMapHave(MKOLUpdateElement c) {
        //if(mDownloaded == null) {
            mDownloaded = offlineMap.getAllUpdateInfo();
        //}
        mHaveAdapter.notifyDataSetChanged();
    }

    public void setMKOfflineMap(MKOfflineMap m) {
        this.offlineMap = m;
    }

}
