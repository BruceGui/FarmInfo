package com.getpoint.farminfomanager.utils.adapters.pointdetailadapter;

import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/26.
 */

public class PointContainer implements Parent<MissionItemProxy> {

    private String pointType;
    private List<MissionItemProxy> itemProxies;

    public PointContainer(String pointType, List<MissionItemProxy> itemProxies) {
        this.pointType = pointType;
        this.itemProxies = itemProxies;
    }

    public String getPointType() {
        return pointType;
    }

    @Override
    public List<MissionItemProxy> getChildList() {
        return itemProxies;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
