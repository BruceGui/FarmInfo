package com.getpoint.farminfomanager.entity;

/**
 * Created by Station on 2016/8/1.
 */
public class PointInfo {

    private double latitude;
    private double longitude;
    private float altitude;

    private short pointNum;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public short getPointNum() {
        return pointNum;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public void setPointNum(short pointNum) {
        this.pointNum = pointNum;
    }
}
