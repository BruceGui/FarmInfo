package com.getpoint.farminfomanager.utils.adapters.offlinemapadapter;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListParent implements Parent<MKOLSearchRecord> {

    private MKOLSearchRecord city;

    public CityListParent(MKOLSearchRecord c) {
        this.city = c;
    }


    public MKOLSearchRecord getCity() {
        return this.city;
    }

    @Override
    public List<MKOLSearchRecord> getChildList() {
        return this.city.childCities;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
