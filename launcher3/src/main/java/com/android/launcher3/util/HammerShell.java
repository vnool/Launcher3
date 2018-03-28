package com.android.launcher3.util;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by chengliang.chengliang.ding on 18/3/19.
 */

public class HammerShell {
    public static void exec(String shell) {
        exec(shell, false);
    }

    public static void exec(String shell, boolean useroot) {

        String runAS = useroot ? "su" : "sh";
        Log.e("myxposed", runAS + " for command:\n" + shell);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(runAS);

            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            //os.writeBytes("setenforce 0 \n");
            os.writeBytes(shell + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("myxposed", " shell done. ");
    }

    public static void closeFireWalls() {
        exec("setenforce 0");
    }

}
