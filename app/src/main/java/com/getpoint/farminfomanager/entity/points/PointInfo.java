package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;

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

    public String toString() {
        if (pointType.equals(PointItemType.FRAMEPOINT)) {
            return position.getLatitude() + " "
                    + position.getLongitude() + " "
                    + position.getAltitude();
        } else {
            return position.getLatitude() + " "
                    + position.getLongitude() + " "
                    + position.getAltitude() + " "
                    + pointNum + " "
                    + flyheight;
        }
    }

    /**
     *
     *//*
    public Object clone() {

        PointInfo o = null;
        try {
            o = (PointInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        o.position = (LatLongAlt)position.clone();

        return o;

    }*/
}
