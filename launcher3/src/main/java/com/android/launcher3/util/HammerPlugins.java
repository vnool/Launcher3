package com.android.launcher3.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.launcher3.AppInfo;
import com.android.launcher3.ShortcutInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chengliang.chengliang.ding on 18/2/2.
 */

public class HammerPlugins {
    public static interface adminCheckBack {
        void back();
    }

    //dingchengliang
    public static boolean checkLogined(Activity act) {

        String employee = HammerConfig.get("login_employee", "");


        if (employee.equals("")) {
//            Intent it = new Intent();
//            it.setClassName("com.hammer.nail", ".activity.Login2");
//            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            configContext.startActivity(it);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            // intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.hammer.nail", "com.hammer.nail.activity.SalerLogin2");
            if (cn != null) {
                intent.setComponent(cn);
                act.startActivity(intent);
            }

            Toast.makeText(act, "未登录！！", Toast.LENGTH_LONG).show();
            return false;
        } else {
            Toast.makeText(act, "加油," + employee, Toast.LENGTH_LONG).show();
            return true;
        }
    }

    public static void adminCheck(final Context C, final adminCheckBack back) {
        Dialogs.InputDialog inputDialog = new Dialogs.InputDialog(C);
        inputDialog.message = "请输入管理员密码";
        inputDialog.callback = new Dialogs.InputDialog.Returns() {
            @Override
            public void back(String input) {
                String pwd = GetAdminPassword();
                if (input != null && input.equals(pwd)) {
                        back.back();

                } else {
                    Toast.makeText(C, "密码错误 "  , Toast.LENGTH_LONG).show();
                }
            }
        };

        inputDialog.show();
    }

    static String GetAdminPassword() {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String time = ft.format(d);
        String md5 = MD5_32(time + "hammer");
        return md5.substring(0, 5);
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
            ComponentName cn = it.getComponent();
            if (cn != null) {
                String pkgName = cn.getPackageName();
                if ("com.tencent.mm;com.hammer.nail".contains(pkgName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String MD5_32(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Log.e(Tag, exception2String(e));
        }
        return re_md5;
    }
}
