package com.MUHLink.Connection;

import com.MUHLink.Protocol.GPSHMCPacket;
import com.MUHLink.Protocol.GPSLinkPacket;

/**
 * Created by Gui Zhou on 2016/3/17.
 */
public interface MUHLinkConnectionListener {

    /**
     *  当 MUHLink 建立连接时调用
     */
    void onConnect();

    /**
     * 当 有数据接收时调用
     * @param packet 接收的数据
     */
    void onReceivePacket(GPSHMCPacket packet);

    /**
     *  当关闭连接时调用
     */
    void onDisconnect();



}
