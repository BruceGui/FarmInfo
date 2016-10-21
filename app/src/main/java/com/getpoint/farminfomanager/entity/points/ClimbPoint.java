package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/4.
 */
public class ClimbPoint extends DangerPoint {

    public static final short INDICATE_NUM = 7;

    protected List<DangerPoint> points = new ArrayList<>();

    public ClimbPoint() {
        //super(position);
        //this.pointType = PointItemType.CLIMBPOINT;
    }

    public void addInnerPoint(DangerPoint point) {
        points.add(point);
    }

    public void setPoints(List<DangerPoint> points) {
        this.points = points;
    }

    public List<DangerPoint> getInnerPoint() {
        return points;
    }

    public void clearPoint() {
        this.points.clear();
    }

}
