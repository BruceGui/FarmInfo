package com.MUHLink.Protocol.enums;

/**
 * Created by Gui Zhou on 2016-03-10.
 */

public class MUH_MSG_ID {

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
     * 同步头定义 数据长度的定义
     */
    public static final int DATALINK_STX1 = 0xEB;
    public static final int DATALINK_STX2 = 0x90;
    public static final int DATALINK_MAX_PAYLOAD_LEN = 125;

    /**
     * 系统和组件编码
     */
    public static final int UAV_SYS_ID = 0x01;   /* 系统号 */

    public static final int UAV_COMP_ID_FCC = 51; /* 飞控机组件号 */

    /**
     * -51- [UAV_COMP_ID_FCC] 飞控机类组件 消息ID
     */
    public static final int UAV_MSG_FCC_HEARTBEAT = 0x20;
    public static final int UAV_MSG_FCC_FLYSTATUS1 = 0x21;
    public static final int UAV_MSG_FCC_FLYSTATUS2 = 0x22;
    public static final int UAV_MSG_FCC_FLYSTATUS3 = 0x23;
    public static final int UAB_MSG_FCC_ATTITUDE = 0x27;
}
