package com.sthh.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Created by kimxu on 16/1/7.
 */

public class GlobalUtils {
    private static String mUUIDString;

    public static String getUUIDString(Context context) {
        if (mUUIDString != null) {
            return mUUIDString;
        }
        PrefUtil.init(context);
        String uuidString = PrefUtil.getString(PrefUtil.UUID, null);
        if (uuidString != null) {
            mUUIDString = uuidString;
        } else {
            UUID uuid = UUID.randomUUID();
            PrefUtil.putString(PrefUtil.UUID, uuid.toString());
            mUUIDString = uuid.toString();
        }
        return mUUIDString;
    }

    private static String mIMEIString;

    public static String getIMEIString(Context context) {
        if (mIMEIString != null)
            return mIMEIString;
        PrefUtil.init(context);
        String imeiString = PrefUtil.getString(PrefUtil.IMEI, null);
        if (mIMEIString != null) {
            mIMEIString = imeiString;
        } else {
            String imei = ((TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            PrefUtil.putString(PrefUtil.IMEI, imei);
            if (imei != null)
                mIMEIString = imei;
        }
        return mIMEIString;
    }

    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    //获得包名,版本名，版本号
    private String getAppInfo(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName + "|" + versionName + "|" + versionCode;
        } catch (Exception e) {
        }
        return null;
    }

}
