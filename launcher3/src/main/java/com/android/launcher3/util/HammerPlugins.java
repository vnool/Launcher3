package com.android.launcher3.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.launcher3.AppInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.ShortcutInfo;

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
    public static boolean checkEnv(Launcher act) {

        if (HammerFreemode.checkAndShow(act)) {
            return true;
        }
        return isHammerRunning(act) && checkLogined(act);
    }


    static boolean checkLogined(Activity act) {

        String employee = HammerConfig.get("login_employee", "");


        if (employee.equals("")) {
//            Intent it = new Intent();
//            it.setClassName("com.hammer.nail", ".activity.Login2");
//            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            configContext.startActivity(it);

            Toast.makeText(act, "未登录！！", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            // intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.hammer.nail", "com.hammer.nail.activity.SalerLogin2");
            if (cn != null) {
                intent.setComponent(cn);
                act.startActivity(intent);
            }


            return false;
        } else {
            Toast.makeText(act, "加油," + employee, Toast.LENGTH_LONG).show();
            return true;
        }
    }

    public static void adminCheck(final Activity C, final adminCheckBack back) {
        adminCheck(C, back, true);
    }

    public static void adminCheck(final Activity C, final adminCheckBack back, boolean cancelable) {
        if (HammerFreemode.checkAndShow(C)) {
            back.back();
            ;
        }

        final EditText textObj = new EditText(C);
        textObj.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(C)
                .setMessage("请输入管理员密码")
                //.setIcon(android.R.drawable.ic_dialog_info)
                .setView(textObj)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String usrtxt = textObj.getText().toString();
                        String pwd = GetAdminPassword();
                        if (usrtxt != null && usrtxt.equals(pwd)) {
                            HammerFreemode.switch2freedom(C);
                            //TODO 不再进入app列表  back.back();

                        } else {
                            Toast.makeText(C, "密码错误 ", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .show();


    }

    public static void autoShowAllapp(Activity C, final adminCheckBack back) {
        if (HammerFreemode.checkAndShow(C)) {
            back.back();
            ;
        }
    }

    static String GetAdminPassword() {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String time = ft.format(d);
        String md5 = HammerFunctions.MD5_32(time + "hammer");
        md5 = md5.replaceAll("[a-zA-Z]", "");
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


    static HammerFunctions.InputDialog inputDialog;

    static boolean isHammerRunning(final Launcher act) {
        //TODO 更好的做法是，自动为xposed installer 勾上

        boolean isrun = ifHammerRunningPleaseHackMe2report().equals("running");
        if (isrun) {
            return true;
        }
        if (inputDialog != null && inputDialog.dialog.isShowing()) {
            return false;
        }
        inputDialog = new HammerFunctions.InputDialog(act);
        inputDialog.title = "系统损坏！！";
        inputDialog.message = "A.请重启手机以修复损坏\nB.请输入管理员密码，进入管理功能";
        inputDialog.callback = new HammerFunctions.InputDialog.Returns() {
            @Override
            public void back(String input) {
                String pwd = GetAdminPassword();
                if (input != null && input.equals(pwd)) {
                    HammerFreemode.switch2freedom(act);
                    act.showAllApp(act.getWindow().getDecorView());
                    inputDialog.dialog.dismiss();

                } else {
                    Toast.makeText(act, "密码错误 ", Toast.LENGTH_LONG).show();
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            isHammerRunning(act);//
//                        }
//                    }, 2000);
                }
            }
        };

        inputDialog.show();

        return false;
    }

    //hammer运行后，会修改这个函数，让它返回值为running
    public static String ifHammerRunningPleaseHackMe2report() {
        return "no";
        //return "running";
    }


}
