package com.getpoint.farminfomanager.entity.markers;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.utils.MarkerWithText;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;

/**
 * Created by Station on 2016/8/3.
 */
public class DangerPointMarker extends PointMarker {

    /**
     *  标记障碍点的顺序 字母 A B C ....
     */
    public static char[] IND = new char[]{'A', 'B', 'C', 'D',
                                           'E', 'F', 'G', 'H', 'I', 'J'};
    private int orderNum;

    /**
     * @param origin
     * @param m
     */

    public DangerPointMarker(PointInfo origin, MissionItemProxy m) {
        super(origin, m);
    }

    public Bitmap getIcon(Resources res) {

        if (isSelected) {
            return MarkerWithText.getMarkerWithTextAndDetail(getIconSelected(),
                    IND[orderNum]+Integer.toString(markerNum), null, res);
        } else {
            return MarkerWithText.getMarkerWithTextAndDetail(getIconNormal(),
                    IND[orderNum]+Integer.toString(markerNum), null, res);
        }
    }

    public void setOrderNum(int num) {
        orderNum = num;
    }

    @Override
    protected int getIconNormal() {
        return R.drawable.ic_danger_point;
    }

    @Override
    protected int getIconSelected() {
        return R.drawable.ic_danger_point;
    }
}
