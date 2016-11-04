package com.getpoint.farminfomanager.entity.markers;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.utils.MarkerWithText;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;

/**
 * Created by Station on 2016/8/3.
 */
public class FramePointMarker extends PointMarker {

    public FramePointMarker(PointInfo origin, MissionItemProxy m) {
        super(origin, m);
    }

    public Bitmap getIcon(Resources res) {

        if (isSelected) {
            return MarkerWithText.getMarkerWithTextAndDetail(getIconSelected(),
                    Integer.toString(markerNum), null, res);
        } else {
            return MarkerWithText.getMarkerWithTextAndDetail(getIconNormal(),
                    Integer.toString(markerNum), null, res);
        }
    }

    @Override
    protected int getIconNormal() {
        return R.drawable.ic_frame_point;
    }

    @Override
    protected int getIconSelected() {
        return R.drawable.ic_frame_point;
    }
}
