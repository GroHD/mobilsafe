package com.itcasthd.mobilesafe.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpUtils {

	//写
	/**
	 * 写入数据
	 * @param context  上下文对象
	 * @param key  存储节点的名称
	 * @param value  存储节点的值
	 */
	public static void putBoolean(Context context,String key ,boolean value) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	//读
	
	/**
	 * 获取存储的值
	 * @param context 上下文对象
	 * @param key  获取的节点名称
	 * @param defValue  不存在返回的默认值
	 * @return
	 */
	public static boolean getBoolean (Context context,String key ,boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	
	//写
	/**
	 * 写入数据
	 * @param context  上下文对象
	 * @param key  存储节点的名称
	 * @param value  存储节点的值
	 */
	public static void putString(Context context,String key ,String value) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	//读
	
	/**
	 * 获取存储的值
	 * @param context 上下文对象
	 * @param key  获取的节点名称
	 * @param defValue  不存在返回的默认值
	 * @return
	 */
	public static String getString (Context context,String key ,String defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	
	//写
	/**
	 * 写入数据
	 * @param context  上下文对象
	 * @param key  存储节点的名称
	 * @param value  存储节点的值
	 */
	public static void putInt(Context context,String key ,int value) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}
	//读
	
	/**
	 * 获取存储的值
	 * @param context 上下文对象
	 * @param key  获取的节点名称
	 * @param defValue  不存在返回的默认值
	 * @return
	 */
	public static int getInt (Context context,String key ,int defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	

	/**
	 * 根据key和上下文删除节点
	 * @param context  上下文
	 * @param key  删除的key
	 */
	public static void Remove(Context context,String key) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.remove(key);
		edit.commit();
	}

}
