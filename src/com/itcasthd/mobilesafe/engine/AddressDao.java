package com.itcasthd.mobilesafe.engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * 访问指定数据库的路径
 * 
 * @author Administrator
 *
 */
public class AddressDao {

	// 拿到归属地的电话号码
	private static String dbFilePath = "data/data/com.itcasthd.mobilesafe/files/address.db";

	/**
	 * 根据电话号码拿到电话号码的归属地
	 * 
	 * @param phone
	 *            电话号码
	 */
	public static String getAddress(String phone) {
		// 正则表达式匹配手机号

		String phoneAddress = "其它号码";
		if (phone.matches("^[1][3,4,5,7,8][0-9]{9}$")) {
			phone = phone.substring(0, 7);
			// 以只读的方式打开数据库
			SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.OPEN_READONLY);
			Cursor query = db.query("data1", new String[] { "outkey" }, "id = ?", new String[] { phone }, null, null,
					null);
			// 判断是否有数据
			if (query != null && query.getCount() > 0 && query.moveToNext()) {
				String outkey = query.getString(0);
				Cursor locationQuery = db.query("data2", new String[] { "location" }, "id=?", new String[] { outkey },
						null, null, null);
				if (locationQuery != null && locationQuery.getCount() > 0 && locationQuery.moveToNext()) {
					phoneAddress = locationQuery.getString(0);

				}

			}
		} 
		

		return phoneAddress;

	}
}
