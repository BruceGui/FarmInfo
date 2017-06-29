package com.MUHLink.Connection;

import com.MUHLink.Protocol.GPSHMCPacket;
import com.MUHLink.Protocol.GPSLinkPacket;

/**
 * Created by Gui Zhou on 2016/3/17.
 */
public class MUHLinkStream {

    public interface MUHLinkOutputStream {

        //void sendMuhPacket(MUHLinkPacket packet);

        boolean isConnected();

        void openConnection(int connectionType);

        void closeConnection();
    }

    public interface MuhLinkInputStream {

        void notifyConnected();

        void notifyDisconnected();

        void notifyReceivedData(GPSHMCPacket packet);

    }

}
