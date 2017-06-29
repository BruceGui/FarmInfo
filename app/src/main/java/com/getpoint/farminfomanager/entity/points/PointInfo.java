package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;

import java.util.Locale;

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
    protected String pointOrd;   // 障碍点的顺序，用来显示点细节时，显示点的顺序

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

    public void setPointOrd(String po) {
        this.pointOrd = po;
    }

    public void setFlyheight(float flyheight) {
        this.flyheight = flyheight;
    }

    public String getPointOrd() {
        return this.pointOrd;
    }

    public String toString() {
        if (pointType.equals(PointItemType.FRAMEPOINT)) {
            return  String.format(Locale.getDefault(), "%.8f", position.getLongitude()) + " "
                    + String.format(Locale.getDefault(), "%.8f", position.getLatitude()) + " "
                    + String.format(Locale.getDefault(), "%.2f", position.getAltitude());
        } else {
            return String.format(Locale.getDefault(), "%.8f", position.getLongitude()) + " "
                    + String.format(Locale.getDefault(), "%.8f", position.getLatitude()) + " "
                    + String.format(Locale.getDefault(), "%.2f", position.getAltitude()) + " "
                    + pointNum + " "
                    + flyheight;
        }
    }
}
