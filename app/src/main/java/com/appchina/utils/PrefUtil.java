package com.appchina.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
	private static final String NAME = "shou_you_han_hua";
	public static final String ST_THEME = "st_theme";
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
