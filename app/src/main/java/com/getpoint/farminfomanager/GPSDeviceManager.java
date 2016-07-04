package com.getpoint.farminfomanager;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.MUHLink.Connection.MUHLinkClient;
import com.MUHLink.Connection.MUHLinkConnection;
import com.MUHLink.Connection.MUHLinkStream;
import com.MUHLink.Protocol.MUHLinkPacket;

/**
 * Created by Gui Zhou on 29/05/2016.
 */
public class GPSDeviceManager implements MUHLinkStream.MuhLinkInputStream {

    private static final String TAG = "GSPDeviceManager";

    private final LocalBroadcastManager localBroadcastManager;
    private final MUHLinkClient deviceClient;
    private final MUHLinkConnection deviceConnection;


    public GPSDeviceManager(Context context, MUHLinkConnection deviceConnection, LocalBroadcastManager localBroadcastManager) {

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
    public void notifyReceivedData(MUHLinkPacket packet) {

    }
}
