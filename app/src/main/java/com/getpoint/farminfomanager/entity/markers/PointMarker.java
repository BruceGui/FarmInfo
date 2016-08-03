package com.getpoint.farminfomanager.entity.markers;

/**
 * Created by Station on 2016/8/3.
 */
public abstract class PointMarker {

    public float getAnchorU() {
        return 0.5f;
    }

    public float getAnchorV() {
        return 0.5f;
    }

    protected abstract int getIconResource();

}
