package com.android.launcher3;

import android.content.Intent;
import android.util.Log;

/**
 * Created by chengliang.chengliang.ding on 18/2/2.
 */

public class DropTargetUtils {


    public static boolean isBlock(String type, Object info) {
        Intent it = null;
        if (info instanceof ShortcutInfo) {
            ShortcutInfo si = (ShortcutInfo) info;
            it = si.getIntent();
        } else if (info instanceof AppInfo) {
            AppInfo ai = (AppInfo) info;
            //TODO it = ai.getIntent();
            //not apply for master
        } else {
            Log.e("myxposed", "supportsDrop!!" + info.getClass().toString() + "==" + info.toString());
        }

        if (it != null) {
            String pkgName = it.getComponent().getPackageName();
            if ("com.tencent.mm;com.hammer.nail".contains(pkgName)) {
                return true;
            }
        }

        return false;
    }
}
