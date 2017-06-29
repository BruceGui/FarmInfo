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
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.OnDownCity;
import com.getpoint.farminfomanager.weights.numberprocessbar.NumberProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Gui Zhou on 2016/10/31.
 */


//TODO 增加后台下载功能 notification 和 service

/**
 *   service 后台下载 NotificationManager 后台通知
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
        mDownloaded = new ArrayList<>();
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
        mDoingText.setVisibility(View.GONE);

        return v;
    }

    /**
     * 正在下载的任务 适配器
     */
    public class OfflineMapDownloadAdapter extends RecyclerView.Adapter<OfflineMapDownloadAdapter.DownloadViewHolder> {

        @Override
        public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DownloadViewHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.offline_map_down_list, parent, false));
        }

        @Override
        public void onBindViewHolder(DownloadViewHolder holder, int position) {
            holder.mCityName.setText(mOnDownload.get(position).r.cityName);
            holder.mDownRatio.setText(String.format(getResources().getString(R.string.down_ratio),
                    mOnDownload.get(position).getRatio()));
            holder.m.setVisibility(View.GONE);
            holder.mProgress.setProgress(mOnDownload.get(position).getRatio());
        }

        @Override
        public int getItemCount() {
            return mOnDownload.size();
        }

        class DownloadViewHolder extends ViewHolder {

            private TextView mCityName;
            private TextView mDownRatio;
            private Button m;
            private NumberProgressBar mProgress;

            private DownloadViewHolder(View v) {
                super(v);
                mCityName = (TextView) v.findViewById(R.id.id_down_city_name);
                mDownRatio = (TextView) v.findViewById(R.id.id_down_ratio);
                m = (Button) v.findViewById(R.id.id_delete_off_map);
                mProgress = (NumberProgressBar) v.findViewById(R.id.id_down_progress);
            }

        }
    }

    /**
     * 已经下载的 适配器
     */
    private class OfflineMapHaveAdapter extends RecyclerView.Adapter<OfflineMapHaveAdapter.HaveViewHolder> {

        @Override
        public HaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HaveViewHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.offline_map_down_list, parent, false));
        }

        @Override
        public void onBindViewHolder(HaveViewHolder holder, final int position) {
            holder.mCityName.setText(mDownloaded.get(position).cityName);

            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    offlineMap.remove(mDownloaded.get(position).cityID);
                    mDownloaded.remove(position);
                    mHaveAdapter.notifyDataSetChanged();
                }
            });

            holder.n.setVisibility(View.INVISIBLE);
            holder.m.setVisibility(View.GONE);
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
            private Button mDelete;
            private TextView m;
            private NumberProgressBar n;

            public HaveViewHolder(View v) {
                super(v);
                mCityName = (TextView) v.findViewById(R.id.id_down_city_name);
                mDelete = (Button) v.findViewById(R.id.id_delete_off_map);
                m = (TextView) v.findViewById(R.id.id_down_ratio);
                n = (NumberProgressBar) v.findViewById(R.id.id_down_progress);
            }

        }

    }

    public void addDownloadCity(MKOLSearchRecord c) {

        if(mOnDownload.isEmpty()) {
            mDoingText.setVisibility(View.VISIBLE);
        }

        if (!mOnDownload.contains(c)) {
            mOnDownload.add(new OnDownCity(c));
        }

        notifyDatasetChanged();
    }

    public void removeDownloadCity() {
        if (!this.mOnDownload.isEmpty()) {
            this.mOnDownload.remove(0);
        }
        notifyDatasetChanged();
    }

    public void updateProcess(MKOLUpdateElement u) {

        if (!mOnDownload.isEmpty()) {
            mOnDownload.get(0).setRatio(u.ratio);

            if (u.ratio == 100) {

                removeDownloadCity();
                updateOfflineMapHave();
                mHaveAdapter.notifyDataSetChanged();
            }
            notifyDatasetChanged();
        }

    }

    public void notifyDatasetChanged() {

        mDowningAdapter.notifyDataSetChanged();
        if (this.mOnDownload.isEmpty()) {
            mDoingText.setVisibility(View.GONE);
        }

    }

    /**
     *  更新已经下载的离线地图列表
     */
    public void updateOfflineMapHave() {

        /**
         *  获取所有 正在下载和已经下载的地图
         */
        mDownloaded = offlineMap.getAllUpdateInfo();

        /**
         *  显示下载完全的 地图
         */
         for(int i = 0; i < mDownloaded.size();) {

             if(mDownloaded.get(i).ratio != 100)
                 mDownloaded.remove(i);
             else
                 i ++;

         }

        mHaveAdapter.notifyDataSetChanged();

    }

    public void setMKOfflineMap(MKOfflineMap m) {
        this.offlineMap = m;
    }

}
