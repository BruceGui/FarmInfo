package com.getpoint.farminfomanager.utils.virtualnavbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;

/**
 * Created by Gui Zhou on 2016/10/24.
 */

public class VirtualNavBar {

    /**
     *  获取虚拟按键的高度
     * @param c
     * @return
     */
    public static int getNavigationBarHeight(Context c) {

        int result = 0;

        if (hasNavBar(c)) {
            Resources res = c.getResources();
            int resId = res.getIdentifier("navigation_bar_height", "dimen", "android");

            if (resId > 0) {
                result = res.getDimensionPixelSize(resId);
            }
        }

        return result;
    }

    /**
     * 检测是否存在虚拟按键栏
     *
     * @param c
     * @return
     */
    public static boolean hasNavBar(Context c) {

        Resources res = c.getResources();
        int resId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resId != 0) {
            boolean hasNav = res.getBoolean(resId);
            String sNavBarOverried = getNavBarOverride();

            if ("1".equals(sNavBarOverried)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverried)) {
                hasNav = true;
            }

            return hasNav;
        } else {
            return !ViewConfiguration.get(c).hasPermanentMenuKey();
        }

    }

    /**
     * 判断虚拟按键是否重写
     *
     * @return
     */
    public static String getNavBarOverride() {

        String sNavBarOverride = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {

            }
        }

        return sNavBarOverride;
    }

}
