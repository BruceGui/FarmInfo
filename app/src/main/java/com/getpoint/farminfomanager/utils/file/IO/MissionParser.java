package com.getpoint.farminfomanager.utils.file.IO;

import android.util.Log;

import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.FramePoint;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.entity.points.StationPoint;
import com.getpoint.farminfomanager.entity.points.enumc.DangerPointType;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/9/12.
 * <p>
 * 用来解析读取任务文件的，每次解析一行，成功后返回一个 missionproxy.
 */


public class MissionParser {

    private static final String TAG = "MissionParser";

    /**
     * 解析的状态
     */
    enum Mission_states {
        MISSION_PARSER_START,
        MISSION_GOT_STATION_POINTS,
        MISSION_GOT_FRAME_POINTS,
        MISSION_GOT_SUCCESS
    }

    Mission_states state = Mission_states.MISSION_PARSER_START;
    static boolean mission_received;
    private MissionProxy m;

    private int mAllStationPoi;
    private int mAllFramePoi;
    private int mAllDangerPoi;

    private int mCurStationPoi;
    private int mCurFramePoi;
    private int mCurDangerPoi;

    /**
     * @param line 待解析的行
     * @return 成功后就返回一个 missionproxy
     */
    public MissionProxy mission_parse_line(String line) {
        mission_received = false;

        switch (state) {
            case MISSION_GOT_SUCCESS:
            case MISSION_PARSER_START:
                if(line.contains("base station")) {

                    final String[] s = line.split("=");
                    mAllStationPoi = Integer.parseInt(s[1]);

                    if(mAllStationPoi == 0) {
                        state = Mission_states.MISSION_GOT_STATION_POINTS;
                    }

                    mCurStationPoi = 0;
                    m = new MissionProxy();

                } else {
                    final String[] s = line.split(" ");
                    LatLong coord = new LatLong(Double.parseDouble(s[1]),
                            Double.parseDouble(s[0]));
                    /**
                     *   把当前点添加到任务中去
                     */
                    final StationPoint stationPoint = new StationPoint(coord, Float.parseFloat(s[2]));
                    MissionItemProxy newItem = new MissionItemProxy(m, stationPoint);
                    m.addItem(newItem);

                    mCurStationPoi++;

                    /**
                     *  成功添加所有边界点
                     */
                    if(mCurStationPoi == mAllStationPoi) {
                        state = Mission_states.MISSION_GOT_STATION_POINTS;
                    }
                }
                break;
            case MISSION_GOT_STATION_POINTS:
                if (line.contains("frame points")) {
                    Log.i(TAG, "start parser");
                    final String[] s = line.split("=");
                    mAllFramePoi = Integer.parseInt(s[1]);
                    Log.i(TAG, "frame " + mAllFramePoi);
                    if (mAllFramePoi == 0) {
                        state = Mission_states.MISSION_GOT_FRAME_POINTS;
                    }
                    mCurFramePoi = 0;
                } else {
                    final String[] s = line.split(" ");
                    LatLong coord = new LatLong(Double.parseDouble(s[1]),
                            Double.parseDouble(s[0]));
                    /**
                     *   把当前点添加到任务中去
                     */
                    final FramePoint framePoint = new FramePoint(coord, Float.parseFloat(s[2]));
                    MissionItemProxy newItem = new MissionItemProxy(m, framePoint);
                    m.addItem(newItem);

                    mCurFramePoi++;

                    /**
                     *  成功添加所有边界点
                     */
                    if(mCurFramePoi == mAllFramePoi) {
                        state = Mission_states.MISSION_GOT_FRAME_POINTS;
                    }
                }
                break;
            case MISSION_GOT_FRAME_POINTS:
                if (line.contains("danger")) {
                    final String[] s = line.split("=");
                    mAllDangerPoi = Integer.parseInt(s[1]);
                    Log.i(TAG, "danger num=" + mAllDangerPoi);
                    if(mAllDangerPoi == 0) {
                        state = Mission_states.MISSION_PARSER_START;
                        mission_received = true;
                    }
                    mCurDangerPoi = 0;
                } else {

                    /**
                     *   添加一个点的信息，因为一行有多个点，所以这样处理
                     */
                    final String[] s = line.split(" ");
                    final int pointnum = s.length / 5;   //获取点的个数
                    final int pointtype = Integer.parseInt(s[3]); //获取点的类型

                    /**
                     *  首先获取行内的所有点
                     */
                    List<PointInfo> dps = new ArrayList<>();
                    for (int i = 0; i < pointnum; i++) {
                        LatLong coord = new LatLong(Double.parseDouble(s[5 * i + 1]),
                                Double.parseDouble(s[5 * i + 0]));

                        final PointInfo dp = new PointInfo(coord,
                                Float.parseFloat(s[5 * i + 2]));
                        dp.setPointNum((short) pointtype);

                        dps.add(dp);
                    }

                    final DangerPoint dangerPoint = new DangerPoint(dps);

                    switch (pointtype) {
                        case 6:
                            dangerPoint.setdPType(DangerPointType.BYPASS);
                            break;
                        case 7:
                            dangerPoint.setdPType(DangerPointType.CLIMB);
                            break;
                        case 8:
                            dangerPoint.setdPType(DangerPointType.FORWARD);
                            break;
                        default:
                            break;
                    }

                    MissionItemProxy newItem = new MissionItemProxy(m, dangerPoint);
                    m.addItem(newItem);

                    mCurDangerPoi++;

                    /**
                     *  成功读取任务文件，并返回任务对象
                     */

                    if(mCurDangerPoi == mAllDangerPoi) {
                        state = Mission_states.MISSION_GOT_SUCCESS;
                        mission_received = true;
                        Log.i(TAG, "Read Mission SUCCESS");
                    }

                }
                break;
        }

        if (mission_received) {
            return m;
        } else {
            return null;
        }
    }
}
