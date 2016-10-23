package com.getpoint.farminfomanager.entity.markers;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.utils.MarkerWithText;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;

/**
 * Created by Station on 2016/8/3.
 */
public abstract class PointMarker {

    private final PointInfo mPointInfo;
    private MissionItemProxy mMIP;
    protected Boolean isSelected = false;
    protected int markerNum;

    public static PointMarker newInstance(PointInfo origin, MissionItemProxy m) {

        PointMarker pointMarker;
        switch (origin.getPointType()) {
            case FRAMEPOINT:
                pointMarker = new FramePointMarker(origin, m);
                break;
            default:
                pointMarker = new DangerPointMarker(origin, m);
                break;
        }

        return pointMarker;
    }

    public PointMarker(PointInfo p, MissionItemProxy m) {
        mPointInfo = p;
        mMIP = m;
    }

    public MissionItemProxy getMissionProxy() {
        return mMIP;
    }

    public PointInfo getPointInfo() {
        return mPointInfo;
    }

    public float getAnchorU() {
        return 0.5f;
    }

    public float getAnchorV() {
        return 0.5f;
    }

    public boolean isDraggable() {
        return false;
    }

    public boolean isVisible() {
        return true;
    }

    public LatLong getPosition() {
        return mPointInfo.getPosition().getLatLong();
    }

    public void setPosition(LatLong coord) {
        LatLong coordinate = mPointInfo.getPosition().getLatLong();
        coordinate.setLatitude(coord.getLatitude());
        coordinate.setLongitude(coord.getLongitude());
    }

    public void setState(Boolean state) {
        this.isSelected = state;
    }



    public void setMarkerNum(int markerNum) {
        this.markerNum = markerNum;
    }

    public int getMarkerNum() {
        return this.markerNum;
    }

    protected abstract Bitmap getIcon(Resources res);

    protected abstract int getIconNormal();

    protected abstract int getIconSelected();

}
