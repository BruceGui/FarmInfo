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
    protected int getIconResource() {
        return R.drawable.ic_danger_point;
    }
}
