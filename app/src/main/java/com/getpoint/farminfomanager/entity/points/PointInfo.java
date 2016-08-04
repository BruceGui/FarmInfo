package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;

/**
 * Created by Station on 2016/8/1.
 */
public class PointInfo {

    protected LatLongAlt position;

    protected short pointNum;

    protected PointItemType pointType;

    public PointInfo() {

    }

    //public PointInfo(PointItemType pointType) {
    //    this.pointType = pointType;
    //}

    public PointItemType getPointType() {
        return this.pointType;
    }

    public void setPosition(double latitude, double longitude, float altitude) {
        position = new LatLongAlt(latitude, longitude, altitude);
    }

    public LatLongAlt getPosition() {
        return position;
    }

    public void setPointNum(short pointNum) {
        this.pointNum = pointNum;
    }
}
