package com.getpoint.farminfomanager.utils.adapters.pointdetailadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/26.
 */

public class PointContainerAdapter extends ExpandableRecyclerAdapter<PointContainer, MissionItemProxy, PointContainerViewHolder, PointInfoViewHolder> {

    private LayoutInflater mInflater;

    public PointContainerAdapter(Context context, @NonNull List<PointContainer> p) {
        super(p);
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PointContainerViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {

        return new PointContainerViewHolder(mInflater.inflate(
                R.layout.point_type_header, parentViewGroup, false
        ));

    }

    @NonNull
    @Override
    public PointInfoViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        return new PointInfoViewHolder(mInflater.inflate(
                R.layout.point_info_adapter, childViewGroup, false
        ));
    }

    @Override
    public void onBindParentViewHolder(@NonNull PointContainerViewHolder parentViewHolder, int parentPosition, @NonNull PointContainer parent) {
        parentViewHolder.bind(parent.getPointType());
    }

    @Override
    public void onBindChildViewHolder(@NonNull PointInfoViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull MissionItemProxy child) {
        childViewHolder.bind(child.getPointInfo());
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
