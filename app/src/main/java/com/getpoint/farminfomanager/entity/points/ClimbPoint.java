package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;

/**
 * Created by Station on 2016/8/4.
 */
public class ClimbPoint extends DangerPoint {

    private final short indicatNum = 7;

    public ClimbPoint(LatLong position) {
        super(position);
        this.pointType = PointItemType.CLIMBPOINT;
    }


}
