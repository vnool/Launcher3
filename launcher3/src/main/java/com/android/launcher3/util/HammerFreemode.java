package com.android.launcher3.util;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

//import android.support.design.widget.Snackbar;

/**
 * Created by chengliang.chengliang.ding on 18/3/19.
 */

public class HammerFreemode {
    public static long FREE_LONG = 1 * 3600 * 1000; //自由时长
    public static String FREE_KEY = "srfoxs";


    static void setFree(Context c) {


        String freetime = (System.currentTimeMillis() + FREE_LONG) + "";
        HammerConfig.set("free_time", freetime);
        HammerConfig.set("free_sum", HammerFunctions.MD5_32(freetime + FREE_KEY));

    }

    static boolean checkFree() {
        String freetime = HammerConfig.get("free_time");

        if (freetime == null || freetime.equals("")) {
            return false;
        }
        if (Long.parseLong(freetime) > System.currentTimeMillis()) {
            String free_sum = HammerConfig.get("free_sum");

            if (HammerFunctions.MD5_32(freetime + FREE_KEY).equals(free_sum)) {
                return true;
            }
        }
        return false;

    }

    interface Done {
        void done();
    }

    static void brodcast2hammer(String action, final Done d) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //  HammerShell.exec("\nam force-stop com.tencent.mm", true);
                HammerShell.exec("am force-stop com.tencent.mm\n"
                        + "am force-stop com.android.launcher3", true);
                if (d != null) d.done();
            }
        }).start();


        // HammerShell.exec("am force-stop com.android.launcher3");


//        Intent intent = new Intent();
//        intent.setAction("com.hammer.nail.action.freedom");
//        intent.putExtra("msg", "简单的消息");
//        intent.putExtra("action", action);
//        //发送广播
//        c.sendBroadcast(intent);

    }

    public static boolean checkAndShow(Activity C) {
        boolean isfree = checkFree();
        if (isfree) {
            createFloatView(C);
            checkTimer(0); //启动定时器
        }
        return isfree;
    }

    static void closeFreedom(Context c) {
        timer.cancel();
        timer = null;
        HammerConfig.set("free_time", "0");
        FreemodeWindowIsAdded = false;

        if (btn_floatView != null) {
            btn_floatView.setText("关闭中...");
        }
        btn_floatView = null;

        brodcast2hammer("stop_free", null);

    }

    public static void switch2freedom(final Activity C) {


        createFloatView(C);
        setFree(C);

        brodcast2hammer("set_free", new Done() {
            @Override
            public void done() {
                //checkTimer(0); //启动定时器. 程序已重启，来不及执行这里
            }
        });
        //这个地方会重启的


    }


    static boolean FreemodeWindowIsAdded = false;
    static Button btn_floatView = null;
    static WindowManager wm;
    static WindowManager.LayoutParams params;

    static void createFloatView(Activity c) {
        if (!requestAlertWindowPermission(c)) {
          //  Toast.makeText(c,"开启自由模式失败！！\n请给本程序悬浮权限",Toast.LENGTH_LONG).show();
            return;
        }
        if (FreemodeWindowIsAdded) return;

        btn_floatView = new Button(c.getApplicationContext());
        btn_floatView.setText("进入自由模式...");
        btn_floatView.setBackgroundColor(Color.rgb(255, 0, 0));

        wm = (WindowManager) c.getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();

        // 设置window type
       // params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
         * 即拉下通知栏不可见
         */

        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
         * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
         */

        // 设置悬浮窗的长得宽
        params.y = 1;
        params.x = 1;
        params.width = 300;
        params.height = 100;

        // 设置悬浮窗的Touch监听
        btn_floatView.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;
            long touchDownTime = 0;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        touchDownTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        wm.updateViewLayout(btn_floatView, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - touchDownTime < 100) {
                            btn_floatView.performClick();
                        }
                        break;
                }
                return true;
            }
        });
        btn_floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //wm.removeView(btn_floatView);
                closeFreedom(view.getContext());

            }
        });

        wm.addView(btn_floatView, params);
        FreemodeWindowIsAdded = true;

    }

    static Timer timer;
    static long endTime = 0;

    static void checkTimer(long delay) {
        if (timer != null) {
            return;
        }
        endTime = System.currentTimeMillis() + FREE_LONG;
        //=========

        final int TIME_ON = 1;
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case TIME_ON:
                        if (btn_floatView == null) {
                            return;
                        }
                        String time = msg.getData().getString("time").toString();
                        btn_floatView.setText("关闭自由模式(" + time + ")");
                        //  wm.updateViewLayout(btn_floatView,params);
                        break;

                }
            }
        };


        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {

                long leftTime = endTime - System.currentTimeMillis();
                if (leftTime < 1) {

                    closeFreedom(null);

                }
                String time = timeString(leftTime);
                Message msg = new Message();
                msg.what = TIME_ON;
                Bundle bl = new Bundle();
                bl.putString("time", time);
                msg.setData(bl);
                handler.sendMessage(msg);

            }
        }, delay, 1000);


    }


    static String timeString(long t) {
        int st = (int) (t / 1000);
        int s = (st % 60);
        int m = (int) Math.floor(st / 60);

        return m + "\'" + s + "\"";

    }


    public static boolean requestAlertWindowPermission(Activity c) {
        if (checkAlertWindowsPermission(c) == false) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + c.getPackageName()));
            c.startActivityForResult(intent, 1);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkAlertWindowsPermission(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1));
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }
}
