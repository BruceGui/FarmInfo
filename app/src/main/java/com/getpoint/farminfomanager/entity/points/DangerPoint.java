package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/1.
 */
public class DangerPoint extends PointInfo {

    protected short relativeAltitude;
    protected List<DangerPoint> points = new ArrayList<>();

    public DangerPoint(LatLong coord) {
        //pointType = PointItemType.DANGERPOINT;
        this.position = new LatLongAlt(coord.getLatitude(), coord.getLongitude(), 0);
    }

    public void clearPoint() {
        this.points.clear();
    }
}
