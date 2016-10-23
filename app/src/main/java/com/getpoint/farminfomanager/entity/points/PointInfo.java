package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;

/**
 * Created by Station on 2016/8/1.
 */
public class PointInfo {

    /**
     *
     */
    protected LatLongAlt position;
    protected PointItemType pointType;
    protected short pointNum;
    protected float flyheight;

    public PointInfo(LatLong coord, float altitude) {
        this.position = new LatLongAlt(coord.getLatitude(), coord.getLongitude(), altitude);
    }

    public PointInfo() {}

    //public PointInfo(PointItemType pointType) {
    //    this.pointType = pointType;
    //}

    public PointItemType getPointType() {
        return this.pointType;
    }

    public void setPointType(PointItemType pt) {
        this.pointType = pt;
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

    public String toString() {
        if (pointType.equals(PointItemType.FRAMEPOINT)) {
            return position.getLongitude() + " "
                    + position.getLatitude() + " "
                    + position.getAltitude();
        } else {
            return position.getLongitude() + " "
                    + position.getLatitude() + " "
                    + position.getAltitude() + " "
                    + pointNum + " "
                    + flyheight;
        }
    }
}
