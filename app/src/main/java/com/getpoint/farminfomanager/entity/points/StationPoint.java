package com.getpoint.farminfomanager.entity.points;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.coordinate.LatLongAlt;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;

/**
 * Created by Gui Zhou on 2016/10/24.
 */

public class StationPoint extends PointInfo{

    public StationPoint(LatLong coord, float height) {

        this.position = new LatLongAlt(coord.getLatitude(), coord.getLongitude(), height);
        this.pointType = PointItemType.STATIONPOINT;

    }

}
