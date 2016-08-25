package com.getpoint.farminfomanager.utils.proxy;

import com.getpoint.farminfomanager.utils.file.IO.MissionWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/3.
 */

/**
 *  保存这个农田所有边界点和障碍点的信息
 */

public class MissionProxy {

    /**
     *  存储当前任务的所有点集合
     */
    private final List<MissionItemProxy> missionItemProxies = new ArrayList<>();

    private final List<MissionItemProxy> boundaryItemProxies = new ArrayList<>();

    private final List<MissionItemProxy> bypassItemProxies = new ArrayList<>();

    private final List<MissionItemProxy> climbItemProxies = new ArrayList<>();

    private final List<MissionItemProxy> forwardItemProies = new ArrayList<>();

    public void addItem(MissionItemProxy itemToAdd) {

        switch (itemToAdd.getPointInfo().getPointType()) {
            case FRAMEPOINT:
                boundaryItemProxies.add(itemToAdd);
                break;
            case BYPASSPOINT:
                bypassItemProxies.add(itemToAdd);
                break;
            case CLIMBPOINT:
                climbItemProxies.add(itemToAdd);
                break;
            case FORWAEDPOINT:
                forwardItemProies.add(itemToAdd);
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

    public List<MissionItemProxy> getBypassItemProxies() {
        return this.bypassItemProxies;
    }

    public List<MissionItemProxy> getClimbItemProxies() {
        return this.climbItemProxies;
    }

    public List<MissionItemProxy> getForwardItemProies() {
        return this.forwardItemProies;
    }

    /**
     *   返回当前边界点总数
     * @return
     */
    public int getCurrentFrameNumber() {
        return this.boundaryItemProxies.size() + 1;
    }

    public int getCurrentBypassNumber() {
        return this.bypassItemProxies.size() + 1;
    }

    public int getCurrentClimbNumber() {
        return this.climbItemProxies.size() + 1;
    }

    public int getCurrentForwardNumber() {
        return this.forwardItemProies.size() + 1;
    }


    /**
     *  返回给定点的序号
     * @param item 给定的点
     * @return 序号
     */
    public int getOrder(MissionItemProxy item) {

        switch (item.getPointInfo().getPointType()) {
            case FRAMEPOINT:
                return boundaryItemProxies.indexOf(item) + 1;
            case BYPASSPOINT:
                return bypassItemProxies.indexOf(item) + 1;
            case CLIMBPOINT:
                return climbItemProxies.indexOf(item) + 1;
            case FORWAEDPOINT:
                return forwardItemProies.indexOf(item) +1;
            default:
                return missionItemProxies.indexOf(item) + 1;
        }

    }

    /**
     *  清除所有的航标点
     */
    public void missionClear() {

        this.boundaryItemProxies.clear();
        this.bypassItemProxies.clear();
        this.climbItemProxies.clear();
        this.forwardItemProies.clear();

    }

    public boolean writeMissionToFile(String filename) {
        return MissionWriter.write(this, filename);
    }
}
