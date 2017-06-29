package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.points.enumc.DangerPointType;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;

import java.util.List;

/**
 * Created by Station on 2016/8/1.
 */

/**
 *  障碍点总共分为 三类：绕飞点、直飞点、爬升点
 */

public class DangerPoint extends PointInfo  {

    private List<PointInfo> innerPoints;
    private DangerPointType dPType;

    public DangerPoint(List<PointInfo> ip) {
        this.innerPoints = ip;
        pointType = PointItemType.DANGERPOINT;
    }

    public List<PointInfo> getInnerPoints() {
        return innerPoints;
    }

    public void setdPType(DangerPointType d) {
        dPType = d;
    }

    public DangerPointType getdPType() {
        return dPType;
    }

    public void setInnerPoints(List<PointInfo> innerPoints) {
        this.innerPoints = innerPoints;
    }

    public void setPointType(PointItemType type) {
        this.pointType = type;
    }

}
