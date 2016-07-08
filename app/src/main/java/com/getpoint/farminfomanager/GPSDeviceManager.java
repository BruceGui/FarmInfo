package com.getpoint.farminfomanager;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.MUHLink.Connection.MUHLinkClient;
import com.MUHLink.Connection.MUHLinkConnection;
import com.MUHLink.Connection.MUHLinkStream;
import com.MUHLink.Protocol.GPSLinkPacket;
import com.MUHLink.Protocol.MUHLinkPacket;
import com.MUHLink.Protocol.common.msg_gps_bestpos;
import com.MUHLink.Protocol.common.msg_gps_bestvel;
import com.MUHLink.Protocol.common.msg_gps_psrdop;
import com.MUHLink.Protocol.enums.MUH_MSG_ID;
import com.getpoint.farminfomanager.utils.GPS;

/**
 * Created by Gui Zhou on 29/05/2016.
 */
public class GPSDeviceManager implements MUHLinkStream.MuhLinkInputStream {

    private static final String TAG = "GPSDeviceManager";

    private final LocalBroadcastManager localBroadcastManager;
    private final MUHLinkClient deviceClient;
    private final MUHLinkConnection deviceConnection;
    private GPS gps;


    public GPSDeviceManager(Context context, GPS gps, MUHLinkConnection deviceConnection, LocalBroadcastManager localBroadcastManager) {

        this.gps = gps;
        this.deviceConnection = deviceConnection;
        this.deviceClient = new MUHLinkClient(context, this, this.deviceConnection);
        this.localBroadcastManager = localBroadcastManager;

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

    @Override
    public void notifyConnected() {

    }

    @Override
    public void notifyDisconnected() {

    }

    @Override
    public void notifyReceivedData(GPSLinkPacket packet) {
        Log.i(TAG, "Handler Data!");

        switch (packet.msg_ID) {
            case MUH_MSG_ID.GPS_BIN_BESTPOS:
                Log.i(TAG, "GOTPOS");
                gps.getGPSBestPosMSG(new msg_gps_bestpos(packet));
                break;
            case MUH_MSG_ID.GPS_BIN_PSRDOP:
                Log.i(TAG, "GOTDOP");
                gps.getGPSPsrDopMSG(new msg_gps_psrdop(packet));
                break;
            case MUH_MSG_ID.GPS_BIN_BESTVEL:
                Log.i(TAG, "GOTVEL");
                gps.getGPSBestVelMSG(new msg_gps_bestvel(packet));
                break;
        }
    }
}
