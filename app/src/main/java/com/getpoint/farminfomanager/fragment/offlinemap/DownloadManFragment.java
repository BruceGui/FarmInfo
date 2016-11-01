package com.getpoint.farminfomanager.fragment.offlinemap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.weights.numberprocessbar.NumberProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class DownloadManFragment extends Fragment {

    private MKOfflineMap offlineMap = null;

    private List<CityDetail> mOnDownload;
    private List<CityDetail> mDownloaded;

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

        mOnDownload = new ArrayList<>();
        mDownloaded = new ArrayList<>();
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

        return v;
    }

    /**
     *  正在下载的任务 适配器
     */
    public class OfflineMapDownloadAdapter extends RecyclerView.Adapter<OfflineMapDownloadAdapter.DownloadViewHolder> {

        @Override
        public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DownloadViewHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.offline_map_on_downloading, parent, false));
        }

        @Override
        public void onBindViewHolder(DownloadViewHolder holder, int position) {
            holder.mCityName.setText(mOnDownload.get(position).getCityName());
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
     *  已经下载的 适配器
     */
    public class OfflineMapHaveAdapter extends RecyclerView.Adapter<OfflineMapHaveAdapter.HaveViewHolder> {

        @Override
        public HaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HaveViewHolder(LayoutInflater.from(getContext())
                .inflate(R.layout.offline_map_downloaded, parent, false));
        }

        @Override
        public void onBindViewHolder(HaveViewHolder holder, int position) {
            holder.mCityName.setText(mDownloaded.get(position).getCityName());
        }

        @Override
        public int getItemCount() {
            return mDownloaded.size();
        }

        class HaveViewHolder extends ViewHolder {

            private TextView mCityName;

            public HaveViewHolder(View v) {
                super(v);
                mCityName = (TextView) v.findViewById(R.id.id_down_city_name);
            }

        }

    }

    public void addDownloadCity(CityDetail c) {
        this.mOnDownload.add(c);
        notifyDatasetChanged();
    }

    public void updateProcess(MKOLUpdateElement u) {
        mOnDownload.get(0).setRatio(u.ratio);
        if(u.ratio == 100) {
           // mDownloaded.add(mOnDownload.get(0));
            removeDownloadCity();
        }
        notifyDatasetChanged();
    }

    public void notifyDatasetChanged() {
        mDowningAdapter.notifyDataSetChanged();
    }

    public void removeDownloadCity() {
        if (!this.mOnDownload.isEmpty()) {
            this.mOnDownload.remove(0);
        }
        notifyDatasetChanged();
    }

    public void updateOfflineMapHave(CityDetail c) {
        mDownloaded.add(c);
        mHaveAdapter.notifyDataSetChanged();
    }

    public void setMKOfflineMap(MKOfflineMap m) {
        this.offlineMap = m;
    }

    public interface OnOfflineMapDownloadListener {

        void startDownload(CityDetail city);

    }
}
