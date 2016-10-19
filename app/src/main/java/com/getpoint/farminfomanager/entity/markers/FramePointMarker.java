package com.getpoint.farminfomanager.entity.markers;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;

/**
 * Created by Station on 2016/8/3.
 */
public class FramePointMarker extends PointMarker {

    public FramePointMarker(MissionItemProxy origin) {
        super(origin);
    }

    @Override
    protected int getIconNormal() {
        return R.drawable.ic_frame_point;
    }

    @Override
    protected int getIconSelected() {
        return R.drawable.ic_frame_point_sel;
    }
}
