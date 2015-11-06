package com.appchina.utils;

import android.app.Activity;

/**
 * Created by xuzhiguo on 15/11/4.
 */
public class ShakeUtils {

    private static SensorManagerHelper helper;

    public static void open(Activity activity) {
        if (helper == null) {
            helper = new SensorManagerHelper(activity);
            helper.start();
        }

    }

    public static void stop() {
        if (helper!=null)
        helper.stop();
        helper = null;
    }
}
