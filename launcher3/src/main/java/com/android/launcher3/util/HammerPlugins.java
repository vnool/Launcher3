package com.android.launcher3.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.launcher3.AppInfo;
import com.android.launcher3.ShortcutInfo;

/**
 * Created by chengliang.chengliang.ding on 18/2/2.
 */

public class HammerPlugins {
    public  static interface adminCheckBack{
        void back();
    }
    public static void adminCheck(final Context C, final  adminCheckBack back) {
        Dialogs.InputDialog inputDialog = new Dialogs.InputDialog(C);
        inputDialog.message = "请输入管理员密码";
        inputDialog.callback = new Dialogs.InputDialog.Returns() {
            @Override
            public void back(String input) {
                if (input != null && input.equals("qazxcv")) {
                    back.back();
                } else {
                    Toast.makeText(C, "密码错误", Toast.LENGTH_LONG).show();
                }
            }
        };

        inputDialog.show();
    }

    public static boolean isWorkspaceBlock(String type, Object info) {
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
