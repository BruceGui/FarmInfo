package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;

/**
 * Created by Station on 2016/8/1.
 */
public class FramePoint extends PointInfo {

    public FramePoint(LatLong coord) {

        this.position = new LatLongAlt(coord.getLatitude(), coord.getLongitude(), 0);
        this.pointType = PointItemType.FRAMEPOINT;

    }

}
