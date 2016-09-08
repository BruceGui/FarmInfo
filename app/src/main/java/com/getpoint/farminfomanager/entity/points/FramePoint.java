package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;

/**
 * Created by Station on 2016/8/1.
 */
public class FramePoint extends PointInfo {

    public FramePoint(LatLong coord, int altitude) {

        this.position = new LatLongAlt(coord.getLatitude(), coord.getLongitude(), altitude);
        this.pointType = PointItemType.FRAMEPOINT;

    }

}
