package com.getpoint.farminfomanager.entity.markers;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.utils.MarkerWithText;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;

/**
 * Created by Gui Zhou on 2016/10/24.
 */

public class StationPointMarker extends PointMarker {

    public StationPointMarker(PointInfo origin, MissionItemProxy m) {
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
        return R.drawable.ic_base_station;
    }

    @Override
    protected int getIconSelected() {
        return R.drawable.ic_base_station;
    }

}
