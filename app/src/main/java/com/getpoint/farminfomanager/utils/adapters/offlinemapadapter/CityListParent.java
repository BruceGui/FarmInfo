package com.getpoint.farminfomanager.utils.adapters.offlinemapadapter;

import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityListParent implements Parent<CityDetail> {

    private CityDetail city;

    public CityListParent(CityDetail c) {
        this.city = c;
    }


    public CityDetail getCity() {
        return this.city;
    }

    @Override
    public List<CityDetail> getChildList() {
        return this.city.getChildCities();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
