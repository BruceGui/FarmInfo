package com.getpoint.farminfomanager.entity.offlinemap;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;

/**
 * Created by Gui Zhou on 2016/11/4.
 */

public class OnDownCity  {

    public MKOLSearchRecord r;
    private int ratio;

    public OnDownCity(MKOLSearchRecord r) {
        this.r = r;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }
}
