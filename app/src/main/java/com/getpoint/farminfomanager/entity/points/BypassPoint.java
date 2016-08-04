package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/4.
 */
public class BypassPoint extends DangerPoint {

    private List<DangerPoint> points = new ArrayList<>();

    public BypassPoint(LatLong position) {
        super(position);
        this.pointType = PointItemType.BYPASSPOINT;
    }

}
