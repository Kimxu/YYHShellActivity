package com.sthh.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
	private static final String NAME = "shou_you_han_hua";
	public static final String ST_THEME = "st_theme";
	public static final String ST_LINK_SHOW = "st_link_show";
	public static final String ST_LINK_ACTION = "st_link_action";
	public static final String ST_DELAY_TIME = "st_delay_time";
	public static final String ST_LINK_TEXT = "st_link_text";
	public static final String ST_LINK_URL = "st_link_url";

	public static final String UUID = "uuid";

	public static final String IMEI = "imei";
	private static SharedPreferences pref;
	private PrefUtil() {
	}

	public static void init(Context context) {
        if (pref==null) {
            pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
	}

	public static void putString(String key, String value) {
		pref.edit().putString(key, value).apply();
	}

	public static String getString(String key, String defValue) {
		return pref.getString(key, defValue);
	}

	public static void putLong(String key, long value) {
		pref.edit().putLong(key, value).apply();
	}

	public static long getLong(String key, long defValue) {
		return pref.getLong(key, defValue);
	}

	public static void putBool(String key, boolean value) {
		pref.edit().putBoolean(key, value).apply();
	}

	public static boolean getBool(String key, boolean defValue) {
		return pref.getBoolean(key, defValue);
	}
}
