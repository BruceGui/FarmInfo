package com.getpoint.farminfomanager.entity.offlinemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Gui Zhou on 2016/10/31.
 */

public class CityDetail {

    private int cityId;
    private int cityType;
    private int ratio;
    private String cityName;
    private boolean isAdded;

    private String filesize;

    private List<CityDetail> childCities = new ArrayList<>();

    public CityDetail(int cityId, String cityName, int filesize, int cityType) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.filesize = formatDataSize(filesize);
        this.cityType = cityType;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getCityType() {
        return this.cityType;
    }

    public void setChildCities(List<CityDetail> c) {
        this.childCities = c;
    }

    public List<CityDetail> getChildCities() {
        return this.childCities;
    }

    public void addChildCity(CityDetail c) {
        this.childCities.add(c);
    }

    public void setStatus(boolean isAdded) {
        this.isAdded = isAdded;
    }

    public boolean getStatus() {
        return this.isAdded;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getFileSize() {
        return filesize;
    }

    public static String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format(Locale.getDefault(), "%dK", size / 1024);
        } else {
            ret = String.format(Locale.getDefault(), "%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

}
