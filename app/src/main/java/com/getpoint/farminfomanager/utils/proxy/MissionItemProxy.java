package com.getpoint.farminfomanager.utils.proxy;

/**
 * Created by Station on 2016/8/3.
 */

import com.getpoint.farminfomanager.entity.markers.PointMarker;
import com.getpoint.farminfomanager.entity.points.PointInfo;

/**
 *  用来处理一个任务中点的信息
 */

public class MissionItemProxy {

    /**
     *  这个点的信息、所属任务和地图标点信息
     */
    private final PointInfo mPointInfo;

    private final MissionProxy mMission;

    private PointMarker mMarker;

    public MissionItemProxy(MissionProxy mission, PointInfo pointInfo) {
        mMission = mission;
        mPointInfo = pointInfo;
        mMarker = PointMarker.newInstance(this);
    }

    public PointInfo getPointInfo() {
        return this.mPointInfo;
    }

    public MissionProxy getMission() {
        return this.mMission;
    }

    public PointMarker getMarker() {
        return this.mMarker;
    }

    public void setMarker(PointMarker marker) {
        this.mMarker = marker;
    }
}
