package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;

/**
 * Created by Station on 2016/8/1.
 */
public class DangerPoint extends PointInfo {

    public DangerPoint(LatLong coord) {
        //pointType = PointItemType.DANGERPOINT;
        this.position = new LatLongAlt(coord.getLatitude(), coord.getLongitude(), 0);
    }
}
