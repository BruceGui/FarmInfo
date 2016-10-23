package com.getpoint.farminfomanager.entity.points.enumc;

/**
 * Created by Gui Zhou on 2016/10/22.
 */

public enum DangerPointType {

    BYPASS(6),
    CLIMB(7),
    FORWARD(8);

    private final int id;

    DangerPointType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
