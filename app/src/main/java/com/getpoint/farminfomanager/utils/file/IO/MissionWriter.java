package com.getpoint.farminfomanager.utils.file.IO;

import android.util.Log;

import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.utils.file.FileList;
import com.getpoint.farminfomanager.utils.file.FileManager;
import com.getpoint.farminfomanager.utils.file.FileStream;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;

/**
 * Write a mission to file.
 */
public class MissionWriter {
	private static final String TAG = MissionWriter.class.getSimpleName();

	public static boolean write(MissionProxy mission) {
		return write(mission, FileStream.getWaypointFilename("waypoints"));
	}

	public static boolean write(MissionProxy mission, String filename) {
		try {
			if (!FileManager.isExternalStorageAvailable())
				return false;

			if (!filename.endsWith(FileList.WAYPOINT_FILENAME_EXT)) {
				filename += FileList.WAYPOINT_FILENAME_EXT;
			}

			final FileOutputStream out = FileStream.getWaypointFileStream(filename);
			/**
			 *  把当前任务点写进文件中
			 */
			//TODO
            final List<MissionItemProxy> boundaryPoints = mission.getBoundaryItemProxies();
            final List<MissionItemProxy> bypassPoints = mission.getBypassItemProxies();
            final List<MissionItemProxy> climbPoints = mission.getClimbItemProxies();
            final List<MissionItemProxy> forwardPoints = mission.getForwardItemProies();

            /**
             *   向文件中写入边界点的信息
             */
            out.write(("frame points=" + boundaryPoints.size()).getBytes());
            out.write("\r\n".getBytes());

            for(MissionItemProxy itemProxy : mission.getBoundaryItemProxies()) {
                out.write(itemProxy.getPointInfo().toString().getBytes());
                out.write("\r\n".getBytes());
            }

            /**
             *  向文件中写入障碍点的信息
             */
            out.write(("danger point="
                        + (bypassPoints.size()
                        + climbPoints.size()
                        + forwardPoints.size())).getBytes());

            out.close();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return false;
		}
		return true;
	}
}
