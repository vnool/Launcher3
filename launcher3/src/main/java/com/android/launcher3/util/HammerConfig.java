package com.android.launcher3.util;

import java.util.HashMap;

/**
 * Created by chengliang.chengliang.ding on 18/2/28.
 */

public class HammerConfig {
    static PropertiesConfig config;

    public static void init() {
        if (config == null) {

            config = PropertiesConfig.getInstance("/data/data/com.hammer.nail/config.xml");
        }
    }

    public static void set(String key, String val) {
        init();
        config.put(key, val);
    }

    public static void set(HashMap hm) {
        init();
        config.put(hm);
    }

    public static String get(String key, String defaultVal) {
        init();
        return config.get(key, defaultVal);
    }

    public static String get(String key) {

        return  get(key, "");

    }

    public static boolean onoff(String key, String defaultVal) {
        String set = get(key, defaultVal);
        return set.equals("1") ? true : false;
    }


}
