package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/1.
 */
public class DangerPoint extends PointInfo implements Cloneable{

    public DangerPoint() {}

    public DangerPoint(LatLong coord, float altitude) {
        //pointType = PointItemType.DANGERPOINT;
        this.position = new LatLongAlt(coord.getLatitude(), coord.getLongitude(), altitude);
    }

    public void setFlyheight(float flyheight) {
        this.flyheight = flyheight;
    }

    public void setPointType(PointItemType type) {
        this.pointType = type;
    }

}
