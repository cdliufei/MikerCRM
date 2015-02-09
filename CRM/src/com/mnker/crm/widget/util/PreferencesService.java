package com.mnker.crm.widget.util;

import java.util.Map;
import java.util.Set;

import com.mnker.crm.http.URLS;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {
	private static SharedPreferences mPreferences;
	private static Context mContext;
	private static String mShareName = "temdata";

	private PreferencesService() {
	}

	private static PreferencesService mPreferencesService;

	public static void init(Context context) {
		if (mPreferencesService == null) {
			mContext = context;
			mPreferencesService = new PreferencesService();
			mPreferences = mContext.getSharedPreferences(mShareName, Context.MODE_PRIVATE);
		}
	}

	/**
	 * 批量保存
	 */
	public static void saveInfo(Map<String, String> info) {
		Editor editor = mPreferences.edit();
		Set<String> keys = info.keySet();
		for (String str : keys) {
			editor.putString(str, info.get(str));
		}
		editor.commit();
	}

	/**
	 * 保存string类型数据
	 */
	public static void setPreferences(String name, String value) {
		Editor editor = mPreferences.edit();
		editor.putString(name, value);
		editor.commit();
	}

	/**
	 * 保存long类型数据
	 */
	public static void setLong(String name, long value) {
		Editor editor = mPreferences.edit();
		editor.putLong(name, value);
		editor.commit();
	}

	public static String getCity() {
		return mPreferences.getString("city_name", "");
	}

	/**
	 * 保存long类型数据
	 */
	public static long getLong(String name) {
		return mPreferences.getLong(name, -1);
	}

	public static void setInt(String name, int value) {
		Editor editor = mPreferences.edit();
		editor.putInt(name, value);
		editor.commit();
	}

	/**
	 * 保存int类型数据
	 */
	public static int getInt(String name) {
		return mPreferences.getInt(name, -1);
	}

	/**
	 * 根据key读取参数
	 */
	public static String getInfo(String key) {
		return mPreferences.getString(key, "");
	}

	/**
	 * 读取状�?
	 */
	public static boolean getBoolean(String key, boolean def) {
		return mPreferences.getBoolean(key, def);
	}

	public static void delete(String key) {
		Editor editor = mPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 读取状�?
	 */
	public static void putBoolean(String key, boolean val) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(key, val);
		editor.commit();
	}

	public static void clear() {
		Editor editor = mPreferences.edit();
		editor.clear();
		editor.commit();
		setPreferences("url", URLS.baseUrl);
	}

}
