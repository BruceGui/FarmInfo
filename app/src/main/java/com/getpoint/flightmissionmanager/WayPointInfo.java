package com.getpoint.flightmissionmanager;

import android.util.Log;

/**
 * Created by lenovo on 2016/4/28.
 */
public class WayPointInfo {
    private int mRouteIndex;
    private int mWayPointIndex;
    private double mLon;
    private  double mLat;
    private int mHeight;
    private int mVdx1;
    private int mVdx2;
    private int mVdx3;


    WayPointInfo(    int routeIndex,
                     int wayPointIndex,
            double lon,
            double lat,
            int height,
            int vdx1,
            int vdx2,
            int vdx3) {
        mRouteIndex = routeIndex;
        mWayPointIndex = wayPointIndex;
        mLat = lat;
        mLon = lon;
        mHeight = height;
        mVdx1 = vdx1;
        mVdx2  = vdx2;
        mVdx3 = vdx3;
    }

    WayPointInfo(String infoStr) {
        String[] strs = infoStr.split("\\s+");
        mRouteIndex = Integer.parseInt(strs[0]);
        mWayPointIndex = Integer.parseInt(strs[1]);
        mLon = Double.parseDouble(strs[2]);
        mLat = Double.parseDouble(strs[3]);
        mHeight = Integer.parseInt(strs[4]);
        mVdx1 = Integer.parseInt(strs[5]);
        mVdx2 = Integer.parseInt(strs[6]);
        mVdx3 = Integer.parseInt(strs[7]);
    }


    public int getRouteIndex() {
        return mRouteIndex;
    }

    public void setRouteIndex(int routeIndex) {
        mRouteIndex = routeIndex;
    }

    public int getWayPointIndex() {
        return mWayPointIndex;
    }

    public void setWayPointIndex(int wayPointIndex) {
        mWayPointIndex = wayPointIndex;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getVdx1() {
        return mVdx1;
    }

    public void setVdx1(int vdx1) {
        mVdx1 = vdx1;
    }

    public int getVdx2() {
        return mVdx2;
    }

    public void setVdx2(int vdx2) {
        mVdx2 = vdx2;
    }

    public int getVdx3() {
        return mVdx3;
    }

    public void setVdx3(int vdx3) {
        mVdx3 = vdx3;
    }

    @Override
    public String toString() {
        return new String("" + mRouteIndex + "    "
                               + mWayPointIndex + "    "
                               + mLon +  "    "
                               + mLat  + "    "
                               + mHeight + "    "
                               + mVdx1 + "    "
                               + mVdx2 + "    "
                               + mVdx3) ;
    }
}
