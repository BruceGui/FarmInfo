package com.getpoint.farminfomanager.entity.markers;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.utils.MarkerWithText;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;

/**
 * Created by Station on 2016/8/3.
 */
public abstract class PointMarker {

    private final MissionItemProxy mMarkerOrigin;
    private Boolean isSelected = false;
    private int markerNum;

    public static PointMarker newInstance(MissionItemProxy origin) {

        PointMarker pointMarker;
        switch (origin.getPointInfo().getPointType()) {
            case FRAMEPOINT:
                pointMarker = new FramePointMarker(origin);
                break;
            default:
                pointMarker = new DangerPointMarker(origin);
                break;
        }

        return pointMarker;
    }

    public PointMarker(MissionItemProxy origin) {
        mMarkerOrigin = origin;
    }

    public MissionItemProxy getmMarkerOrigin() {
        return this.mMarkerOrigin;
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
        return mMarkerOrigin.getPointInfo().getPosition().getLatLong();
    }

    public void setPosition(LatLong coord) {
        LatLong coordinate = mMarkerOrigin.getPointInfo().getPosition().getLatLong();
        coordinate.setLatitude(coord.getLatitude());
        coordinate.setLongitude(coord.getLongitude());
    }

    public void setState(Boolean state) {
        this.isSelected = state;
    }

    public Bitmap getIcon(Resources res) {

        if (isSelected) {
            return MarkerWithText.getMarkerWithTextAndDetail(getIconSelected(),
                    Integer.toString(markerNum), null, res);
        } else {
            return MarkerWithText.getMarkerWithTextAndDetail(getIconNormal(),
                    Integer.toString(markerNum), null, res);
        }
    }

    public void setMarkerNum(int markerNum) {
        this.markerNum = markerNum;
    }

    public int getMarkerNum() {
        return this.markerNum;
    }

    protected abstract int getIconNormal();

    protected abstract int getIconSelected();

}
