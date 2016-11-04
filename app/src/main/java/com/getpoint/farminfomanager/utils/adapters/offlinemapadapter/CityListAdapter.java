package com.getpoint.farminfomanager.utils.adapters.offlinemapadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.utils.MKOfflineMapDownloadListener;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListAdapter extends ExpandableRecyclerAdapter<CityListParent, MKOLSearchRecord, CityListParentViewHolder, CityListChildViewHolder> {

    private LayoutInflater mLayoutInflater;
    private MKOfflineMapDownloadListener listener;

    public CityListAdapter(@NonNull Context c, List<CityListParent> p,
                           MKOfflineMapDownloadListener listener) {
        super(p);
        mLayoutInflater = LayoutInflater.from(c);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CityListChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        return new CityListChildViewHolder(mLayoutInflater
                .inflate(R.layout.city_list_child_view_holder, childViewGroup, false), listener);
    }

    @NonNull
    @Override
    public CityListParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        return new CityListParentViewHolder(mLayoutInflater
                .inflate(R.layout.city_list_parent_view_holder, parentViewGroup, false));
    }

    @Override
    public void onBindParentViewHolder(@NonNull CityListParentViewHolder parentViewHolder, int parentPosition, @NonNull CityListParent parent) {
        parentViewHolder.bind(parent.getCity());
    }

    @Override
    public void onBindChildViewHolder(@NonNull CityListChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull MKOLSearchRecord child) {
        childViewHolder.bind(child);
    }

    @Override
    public int getParentViewType(int parentPosition) {
        return super.getParentViewType(parentPosition);
    }

    @Override
    public int getChildViewType(int parentPosition, int childPosition) {
        return super.getChildViewType(parentPosition, childPosition);
    }

    @Override
    public boolean isParentViewType(int viewType) {
        return super.isParentViewType(viewType);
    }
}
