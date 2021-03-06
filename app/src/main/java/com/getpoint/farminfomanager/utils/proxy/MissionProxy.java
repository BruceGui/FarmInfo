package com.getpoint.farminfomanager.utils.proxy;

import android.util.Log;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.markers.DangerPointMarker;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;
import com.getpoint.farminfomanager.utils.file.IO.MissionWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/3.
 */

/**
 * 保存这个农田所有边界点和障碍点的信息
 */

public class MissionProxy {

    private static final String TAG = "MissionProxy";

    /**
     * 存储当前任务的所有点集合
     */
    private List<MissionItemProxy> missionItemProxies = new ArrayList<>();

    private List<MissionItemProxy> boundaryItemProxies = new ArrayList<>();

    private List<MissionItemProxy> dangerItemProxies = new ArrayList<>();

    private List<MissionItemProxy> baseStationProxies = new ArrayList<>();

    public void addItem(MissionItemProxy itemToAdd) {

        switch (itemToAdd.getPointInfo().getPointType()) {
            case FRAMEPOINT:
                boundaryItemProxies.add(itemToAdd);
                break;
            case DANGERPOINT:
                dangerItemProxies.add(itemToAdd);
                break;
            case STATIONPOINT:
                baseStationProxies.add(itemToAdd);
                break;
            default:
                missionItemProxies.add(itemToAdd);
                break;
        }
    }

    public List<MissionItemProxy> getItem() {
        return this.missionItemProxies;
    }

    public List<MissionItemProxy> getBoundaryItemProxies() {
        return this.boundaryItemProxies;
    }

    public List<MissionItemProxy> getDangerItemProxies() {
        return this.dangerItemProxies;
    }

    /**
     *   返回每一个小障碍点的集合, 用于显示点细节
     */
    public List<MissionItemProxy> getAllDangerPoints() {

        List<MissionItemProxy> allDangerNums = new ArrayList<>();


        for(int i = 0; i < dangerItemProxies.size(); i ++) {
            List<PointInfo> ips = ((DangerPoint)dangerItemProxies.get(i)
                    .getPointInfo()).getInnerPoints();

            for(int j = 0; j < ips.size(); j ++) {

                PointInfo ip = new PointInfo(ips.get(j).getPosition().getLatLong(),
                        ips.get(j).getPosition().getAltitude());
                ip.setPointType(PointItemType.DANGERPOINT);
                ip.setPointOrd(String.valueOf(DangerPointMarker.IND[i])+(j+1));

                allDangerNums.add(new MissionItemProxy(this, ip));

            }

        }


        return allDangerNums;

    }


    public List<MissionItemProxy> getBaseStationProxies() {
        return this.baseStationProxies;
    }
    /**
     * 返回当前边界点总数
     *
     * @return
     */
    public int getCurrentFrameNumber() {
        return this.boundaryItemProxies.size() + 1;
    }

    public int getCurrentBaseNumber() {
        return this.baseStationProxies.size() + 1;
    }

    public int getCurrentDangerNumber() {

        if(dangerItemProxies.isEmpty()) {
            return 0;
        }

        return this.dangerItemProxies.size() - 1;
    }

    public int getActualDangerNumber() {

        int actNum = 0;

        for(MissionItemProxy m : dangerItemProxies) {

            if(!((DangerPoint)m.getPointInfo()).getInnerPoints().isEmpty())
                actNum ++;

        }

        return actNum;

    }

    /**
     * 返回给定点的序号
     *
     * @param item 给定的点
     * @return 序号
     */
    public int getOrder(MissionItemProxy item) {

        switch (item.getPointInfo().getPointType()) {
            case FRAMEPOINT:
                return boundaryItemProxies.indexOf(item) + 1;
            case DANGERPOINT:
                return dangerItemProxies.indexOf(item) + 1;
            case STATIONPOINT:
                return baseStationProxies.indexOf(item) + 1;
            default:
                return missionItemProxies.indexOf(item) + 1;
        }

    }

    /**
     * 清除所有的航标点
     */
    public void missionClear() {

        this.boundaryItemProxies.clear();
        this.dangerItemProxies.clear();
        this.baseStationProxies.clear();

    }

    public boolean writeMissionToFile(String filepath, String filename) {
        return MissionWriter.write(this, filepath, filename);
    }

    public boolean readMissionFromFile(String filepath) {

        this.missionClear();
        MissionProxy missionProxy = MissionWriter.read(filepath);
        if (missionProxy != null) {

            this.boundaryItemProxies = missionProxy.getBoundaryItemProxies();
            this.dangerItemProxies   = missionProxy.getDangerItemProxies();
            this.baseStationProxies = missionProxy.getBaseStationProxies();
            return true;
        }

        return false;

    }
}
