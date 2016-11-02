package com.MUHLink.Protocol.enums;

/**
 * Created by Gui Zhou on 2016-03-10.
 */

public class MUH_MSG_ID {

    /**
     * UAV 接收的数据的组件 ID
     */
    public static final int UAV_COMP_SENSOR_GPS = 57;

    /**
     * GPSHMC 接收数据的同步信息
     */
    public static final int GPSHMC_STX1 = 0xEB;
    public static final int GPSHMC_STX2 = 0x90;

    /**
     *  GPSHMC 接收的数据类型
     */
    public static final int UMH_GPS_RawData = 0x20;

    /**
     * GPS 接受数据的同步信息
     */
    public static final int GPSLINK_STX1 = 0xAA;
    public static final int GPSLINK_STX2 = 0x44;
    public static final int GPSLINK_STX3 = 0x12;

    /**
     * GPS 接收的数据类型
     */
    public static final int GPS_BIN_BESTPOS = 0x002A;
    public static final int GPS_BIN_BESTVEL = 0x0063;
    public static final int GPS_BIN_PSRDOP = 0x00AE;

    /**
     * 数据长度的定义
     */
    public static final int DATALINK_MAX_PAYLOAD_LEN = 300;
}
