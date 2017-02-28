package com.getpoint.farminfomanager.utils;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.baidu.mapapi.map.offline.MKOfflineMap;

/**
 * Created by Gui Zhou on 2016/11/8.
 */

/**
 *  通知正在下载的离线地图的 进度 只管后台下载 不管通知
 */
public class OfflineMapDownService extends IntentService {

    private static final String TAG = "OfflineMapDownService";

    private  int NOTIFY_ID = 1200;
    private NotificationCompat.Builder mOfflineMapNotify;
    private NotificationManager notificationManager;

    public OfflineMapDownService() {super("OfflineMapDownService");}

    public OfflineMapDownService(MKOfflineMap o) {
        super("OfflineMapDownService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
