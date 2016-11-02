package com.getpoint.farminfomanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.MUHLink.Connection.MUHLinkClient;
import com.MUHLink.Connection.MUHLinkConnection;
import com.MUHLink.Connection.MUHLinkStream;
import com.MUHLink.Protocol.GPSHMCPacket;
import com.MUHLink.Protocol.GPSLinkPacket;
import com.MUHLink.Protocol.GPSParser;
import com.MUHLink.Protocol.common.msg_gps_bestpos;
import com.MUHLink.Protocol.common.msg_gps_bestvel;
import com.MUHLink.Protocol.common.msg_gps_psrdop;
import com.MUHLink.Protocol.enums.MUH_MSG_ID;
import com.getpoint.farminfomanager.utils.AttributesEvent;
import com.getpoint.farminfomanager.utils.GPS;

/**
 * Created by Gui Zhou on 29/05/2016.
 */
public class GPSDeviceManager implements MUHLinkStream.MuhLinkInputStream {

    private static final String TAG = "GPSDeviceManager";

    private final GPSParser parser;
    private final LocalBroadcastManager localBroadcastManager;
    private final MUHLinkClient deviceClient;
    private final MUHLinkConnection deviceConnection;
    private GPS gps;


    public GPSDeviceManager(Context context, GPS gps, MUHLinkConnection deviceConnection, LocalBroadcastManager localBroadcastManager) {

        this.gps = gps;
        this.deviceConnection = deviceConnection;
        this.deviceClient = new MUHLinkClient(context, this, this.deviceConnection);
        this.localBroadcastManager = localBroadcastManager;
        this.parser = new GPSParser();
    }

    public void connect(int connectionType) {
        deviceClient.openConnection(connectionType);
    }

    public void disconnect() {
        deviceClient.closeConnection();
    }

    public boolean isconnect() {
        return deviceClient.isConnected();
    }

    private void sendMsgEvent(String event) {
        Intent i = new Intent(event);
        this.localBroadcastManager.sendBroadcast(i);
    }

    public void handleGPSMessage(GPSLinkPacket packet) {

        switch (packet.msg_ID) {
            case MUH_MSG_ID.GPS_BIN_BESTPOS:
                Log.i(TAG, "GOTPOS");
                gps.getGPSBestPosMSG(new msg_gps_bestpos(packet));
                sendMsgEvent(AttributesEvent.GPS_MSG_BESTPOS);
                break;
            case MUH_MSG_ID.GPS_BIN_PSRDOP:
                Log.i(TAG, "GOTDOP");
                gps.getGPSPsrDopMSG(new msg_gps_psrdop(packet));
                sendMsgEvent(AttributesEvent.GPS_MSG_PSRDOP);
                break;
            case MUH_MSG_ID.GPS_BIN_BESTVEL:
                Log.i(TAG, "GOTVEL");
                gps.getGPSBestVelMSG(new msg_gps_bestvel(packet));
                sendMsgEvent(AttributesEvent.GPS_MSG_BESTVEL);
                break;
            default:
                break;
        }

    }

    @Override
    public void notifyConnected() {

    }

    @Override
    public void notifyDisconnected() {

    }

    @Override
    public void notifyReceivedData(GPSHMCPacket packet) {

        switch (packet.compid) {
            case MUH_MSG_ID.UAV_COMP_SENSOR_GPS:

                switch (packet.msgid) {
                    case MUH_MSG_ID.UMH_GPS_RawData:

                        int msglen = packet.len;
                        packet.payload.resetIndex();
                        for (int j = 0; j < msglen; j++) {
                            GPSLinkPacket p = parser.datalink_parser_char(packet.payload.getByte() & 0x00ff);
                            if (p != null) {
                                /**
                                 *  获得 GPS 数据包，并处理
                                 */
                                handleGPSMessage(p);
                            }
                        }
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }

    }
}
