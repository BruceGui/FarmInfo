package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/4.
 */
public class BypassPoint extends DangerPoint implements Cloneable{

    public static final short INDICATE_NUM = 6;

    protected List<DangerPoint> points = new ArrayList<>();

    public BypassPoint() {
        //super(position);
        //this.pointType = PointItemType.BYPASSPOINT;
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

    /*public Object clone() {

        BypassPoint o = null;

        o = (BypassPoint)super.clone();

        for(DangerPoint dp : points) {
            o.points.add((DangerPoint) dp.clone());
        }

        return o;

    }*/

}
