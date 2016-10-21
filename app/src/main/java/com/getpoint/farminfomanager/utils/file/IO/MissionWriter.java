package com.getpoint.farminfomanager.utils.file.IO;

import android.util.Log;

import com.getpoint.farminfomanager.entity.points.BypassPoint;
import com.getpoint.farminfomanager.entity.points.ClimbPoint;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.ForwardPoint;
import com.getpoint.farminfomanager.utils.file.FileList;
import com.getpoint.farminfomanager.utils.file.FileStream;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Write a mission to file.
 */
public class MissionWriter {
    private static final String TAG = "MissionWriter";

    public static boolean write(MissionProxy mission, String filepath) {
        return write(mission, filepath, FileStream.getWaypointFilename("waypoints"));
    }

    public static boolean write(MissionProxy mission, String filepath, String filename) {
        try {

            if (!filename.endsWith(FileList.WAYPOINT_FILENAME_EXT)) {
                filename += FileList.WAYPOINT_FILENAME_EXT;
            }

            final FileOutputStream out = FileStream.getWaypointFileStream(filepath, filename);
            /**
             *  把当前任务点写进文件中
             */
            final List<MissionItemProxy> boundaryPoints = mission.getBoundaryItemProxies();
            final List<MissionItemProxy> bypassPoints = mission.getBypassItemProxies();
            final List<MissionItemProxy> climbPoints = mission.getClimbItemProxies();
            final List<MissionItemProxy> forwardPoints = mission.getForwardItemProies();

            /**
             *  写入文件头部信息
             *  refedge=6
             *  spraywidth=5
             *  vel=5
             *  alt=-1000
             *  safewidth=2
             *  dangerwidth=2
             */

            out.write("refedge=6".getBytes());
            newline(out);

            out.write("spraywidth=5".getBytes());
            newline(out);

            out.write("vel=5".getBytes());
            newline(out);

            out.write("alt=-1000".getBytes());
            newline(out);

            out.write("safewidth=2".getBytes());
            newline(out);

            out.write("dangerwidth=2".getBytes());
            newline(out);

            /**
             *   向文件中写入边界点的信息
             */
            out.write(("frame points=" + boundaryPoints.size()).getBytes());
            newline(out);

            for (MissionItemProxy itemProxy : boundaryPoints) {
                out.write(itemProxy.getPointInfo().toString().getBytes());
                out.write("\r\n".getBytes());
            }

            /**
             *  向文件中写入障碍点的信息
             */
            out.write(("danger num="
                    + (bypassPoints.size()
                    + climbPoints.size()
                    + forwardPoints.size())).getBytes());

            newline(out);


            /**
             *  写入绕飞点
             */
            for (MissionItemProxy itemProxy : bypassPoints) {
                for (DangerPoint innerPoint : ((BypassPoint) itemProxy.getPointInfo())
                        .getInnerPoint()) {
                    out.write(innerPoint.toString().getBytes());
                    out.write(" ".getBytes());
                }
                Log.i(TAG, "write bypass");
                newline(out);
            }

            Log.i(TAG, "bypass.size = " + bypassPoints.size());

            /**
             *  写入爬升点
             */
            for (MissionItemProxy itemProxy : climbPoints) {
                for (DangerPoint innerPoint : ((ClimbPoint) itemProxy.getPointInfo())
                        .getInnerPoint()) {
                    out.write(innerPoint.toString().getBytes());
                    out.write(" ".getBytes());
                }
                Log.i(TAG, "write climb");
                newline(out);
            }

            /**
             *  写入直飞点
             */
            for (MissionItemProxy itemProxy : forwardPoints) {
                for (DangerPoint innerPoint : ((ForwardPoint) itemProxy.getPointInfo())
                        .getInnerPoint()) {
                    out.write(innerPoint.toString().getBytes());
                    out.write(" ".getBytes());

                }
                Log.i(TAG, "write forward");
                newline(out);
            }


            out.write("Method=0".getBytes());
            newline(out);
            /**
             *  写入结束信号
             */
            //out.write("end".getBytes());
            //newline(out);
            out.close();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static MissionProxy read(String filepath) {

        Log.i(TAG, "read mission from file:" + filepath);

        /**
         *  Java 文件相关的操作
         */
        File file = new File(filepath);
        MissionProxy missionProxy = null;
        MissionParser parser = new MissionParser();
        int linenum = 0;
        try {
            String line;
            FileInputStream in = new FileInputStream(file);

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while (!((line = reader.readLine()).contains("Method"))) {
                /**
                 *  解析读取的行，成功则返回一个 mission 对象,
                 *  跳过前六行的附属信息
                 */
                linenum ++;
                if(linenum == 1 && !line.contains("refedge")) {
                    break;
                }

                if(linenum > 6) {
                    missionProxy = parser.mission_parse_line(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return missionProxy;
    }

    private static void newline(FileOutputStream o) {
        try {
            o.write("\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
