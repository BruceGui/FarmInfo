package com.getpoint.farminfomanager.utils;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.getpoint.farminfomanager.entity.offlinemap.CityDetail;

/**
 * Created by Gui Zhou on 2016/11/3.
 */

public interface MKOfflineMapDownloadListener {

    void startDownload(MKOLSearchRecord c);

}
