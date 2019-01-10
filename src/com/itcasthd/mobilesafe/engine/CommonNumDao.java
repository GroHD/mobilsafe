package com.itcasthd.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.itcasthd.mobilesafe.db.dao.domin.CommonNumInfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 常用号码查询
 * 
 * @author Administrator
 *
 */
public class CommonNumDao {

	// 拿到归属地的电话号码
	private static String dbFilePath = "data/data/com.itcasthd.mobilesafe/files/commonnum.db";

	/**
	 * 
	 * 打开数据库
	 */
	public static SQLiteDatabase openSqlite() {
		try {
			return SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.OPEN_READONLY);

		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * 获取组
	 */
	public static List<CommonNumInfo> getGroup() {
		SQLiteDatabase openSqlite = openSqlite();
		List<CommonNumInfo> connInfoList = new ArrayList<CommonNumInfo>();
		if (openSqlite != null) {

			Cursor cursor = openSqlite.query("classlist", new String[] { "name", "idx" }, null, null, null, null, null);
			while (cursor.moveToNext()) {
				CommonNumInfo common = new CommonNumInfo();
				String idx = cursor.getString(1);
				common.setnumId(idx);
				String name = cursor.getString(0);
				common.set_name(name);
				connInfoList.add(common);
			}
			openSqlite.close();
		}

		return connInfoList;
	}

	/**
	 * 根据分组id拿到具体的数据信息
	 * 
	 * @param idx
	 *            分组的id
	 */
	public static List<CommonNumInfo> getNumInfo(String idx) {
		SQLiteDatabase openSqlite = openSqlite();
		List<CommonNumInfo> commonNumInfo = new ArrayList<CommonNumInfo>();
		if (openSqlite != null) {
			Cursor query = openSqlite.query("table" + idx, new String[] { "number", "name" }, null, null, null, null,
					null);
			while (query.moveToNext()) {
				CommonNumInfo common = new CommonNumInfo();
				String number = query.getString(0);
				String name = query.getString(1);
				common.setnumId(number);
				common.set_name(name);
				commonNumInfo.add(common);
			}

			openSqlite.close();
		}
		return commonNumInfo;
	}

}
