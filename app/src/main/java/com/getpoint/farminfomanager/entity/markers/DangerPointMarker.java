package com.getpoint.farminfomanager.entity.markers;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;

/**
 * Created by Station on 2016/8/3.
 */
public class DangerPointMarker extends PointMarker {

    public DangerPointMarker(MissionItemProxy origin) {
        super(origin);
    }

    @Override
    protected int getIconNormal() {
        return R.drawable.ic_danger_point;
    }

    @Override
    protected int getIconSelected() {
        return R.drawable.ic_danger_point_sel;
    }
}
