package com.android.launcher3.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesConfig {

    public static PropertiesConfig instance = null;
    String propertyPath = "";

    private PropertiesConfig(String path) {
        if (path == null || path.length() < 3) {
            //Log.e("PropertiesConfig","myx, path is bad");
            Exception e = new Exception("path is bad");
            e.printStackTrace();
        }

        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
                f.setWritable(true, false);
                f.setReadable(true, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        propertyPath = path;

    }

    public static PropertiesConfig getInstance(String path) {
        if (instance == null) {
            instance = new PropertiesConfig(path);
        }

        return instance;

    }

    public String get(String key, String defaultvalue) {

        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream(propertyPath);
            props.load(fis);
            fis.close();
            String value = props.getProperty(key);

            if (value == null) return defaultvalue;

            return value;
            //System.out.println(key  + " "+strValue);
        } catch (FileNotFoundException e) {
            Log.e("ini", "config.properties Not Found Exception", e);
        } catch (IOException e) {
            Log.e("ini", "config.properties IO Exception", e);
        }
        return defaultvalue;
    }


    public void put(String key, String value) {
        Properties props = new Properties();

        try {
            FileInputStream fis = new FileInputStream(propertyPath);
            props.load(fis);
            fis.close();
            OutputStream out = new FileOutputStream(propertyPath);
//            Enumeration<?> e = props.propertyNames();
//            if (e.hasMoreElements()) {
//                while (e.hasMoreElements()) {
//                    String s = (String) e.nextElement();
//                    if (!s.equals(key)) {
//                        props.setProperty(s, props.getProperty(s));
//                    }
//                }
//            }
            props.setProperty(key, value);
            props.store(out, null);

            out.close();

            //   value = props.getProperty(key );
            // System.out.println(key  + " "+value);
        } catch (FileNotFoundException e) {
            Log.e("ini", "config.properties Not Found Exception", e);
        } catch (IOException e) {
            Log.e("ini", "config.properties IO Exception", e);
        }
    }

    public void put(HashMap<String, String> hm) {
        Properties props = new Properties();

        try {
            FileInputStream fis = new FileInputStream(propertyPath);
            props.load(fis);
            fis.close();
            OutputStream out = new FileOutputStream(propertyPath);

            for (String key : hm.keySet()) {
                String value = hm.get(key);
                props.setProperty(key, value);
            }

            props.store(out, null);

            out.close();

            // value = props.getProperty(key );
            // System.out.println(key  + " "+value);
        } catch (FileNotFoundException e) {
            Log.e("ini", "config.properties Not Found Exception", e);
        } catch (IOException e) {
            Log.e("ini", "config.properties IO Exception", e);
        }
    }
}
