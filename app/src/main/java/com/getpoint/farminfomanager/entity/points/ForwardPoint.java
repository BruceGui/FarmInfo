package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;

/**
 * Created by Station on 2016/8/4.
 */
public class ForwardPoint extends DangerPoint {

    private final short indicat = 8;

    public ForwardPoint(LatLong position) {
        super(position);
        this.pointType = PointItemType.FORWAEDPOINT;
    }

}
