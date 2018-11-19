package com.itcasthd.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.db.MySQLiteOpenHelper;
import com.itcasthd.mobilesafe.db.dao.domin.BlackNumberInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackNumberDao {

	private static BlackNumberDao bnd = null;
	private static MySQLiteOpenHelper mSqlite;

	public static BlackNumberDao getBlackNumberDao(Context context) {
		if (bnd == null) {
			bnd = new BlackNumberDao();
			mSqlite = new MySQLiteOpenHelper(context);
		}
		return bnd;
	}

	/**
	 * 插入一条数据
	 * 
	 * @param phone
	 *            电话号码
	 * @param model
	 *            拦截的类型(1 是短信 2是电话 3是全部)
	 */
	public void insert(String phone, String model) {
		if (mSqlite != null) {
			SQLiteDatabase db = mSqlite.getWritableDatabase();
			ContentValues content = new ContentValues();
			content.put("phone", phone);
			content.put("model", model);
			db.insert("blackNumber", null, content);
			db.close();

		}
	}

	/**
	 * 根据电话号码删除数据
	 * 
	 * @param phone
	 *            要删除的电话号码
	 */
	public void delete(String phone) {
		if (mSqlite != null) {
			SQLiteDatabase db = mSqlite.getWritableDatabase();
			db.delete("blackNumber", "phone=?", new String[] { phone });
			db.close();
		}
	}

	/**
	 * 根据电话号码更新拦截模式
	 * 
	 * @param phone
	 *            电话号码
	 * @param model
	 *            拦截模式
	 */
	public void update(String phone, String model) {
		if (mSqlite != null) {
			SQLiteDatabase db = mSqlite.getWritableDatabase();
			ContentValues uptv = new ContentValues();
			uptv.put("model", model);
			db.update("blackNumber", uptv, "phone=?", new String[] { phone });
		}
	}

	public List<BlackNumberInfo> findAll() {
		List<BlackNumberInfo> blackInfoList = new ArrayList<BlackNumberInfo>();
		if (mSqlite != null) {
			SQLiteDatabase db = mSqlite.getReadableDatabase();
			Cursor query = db.query("blackNumber", new String[] { "phone", "model" }, null, null, null, null,
					"_id desc");
			while (query.moveToNext()) {
				BlackNumberInfo info = new BlackNumberInfo();
				String phone = query.getString(0);
				String model = query.getString(1);
				info.setPhone(phone);
				info.setModel(model);
				blackInfoList.add(info);
			}
			query.close();
			db.close();
		}
		return blackInfoList;
	}

	/**
	 * 分页加载数据
	 * 
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页多少条数据
	 * @return 分页后的数据,如果没有数据则返回长度为0的数据
	 */
	public List<BlackNumberInfo> findPageIndex(int pageIndex, int pageSize) {
		List<BlackNumberInfo> blackInfoList = new ArrayList<BlackNumberInfo>();
		if (mSqlite != null) {
			SQLiteDatabase read = mSqlite.getReadableDatabase();
			String sql = " select model,phone from blackNumber order by _id desc limit ?,?";
			Cursor curaor = read.rawQuery(sql, new String[] { ((pageIndex - 1) * pageSize) + "", pageSize + "" });
			while (curaor.moveToNext()) {
				BlackNumberInfo bni = new BlackNumberInfo();
				bni.setModel(curaor.getString(0));
				bni.setPhone(curaor.getString(1));
				blackInfoList.add(bni);
			}
		}
		return blackInfoList;
	}

	/**
	 * 拿到表数据的大小
	 * @return
	 */
	public int getCount() {
		
		int count = 0;
		if (mSqlite != null) {
			SQLiteDatabase read = mSqlite.getReadableDatabase();
			Cursor rawQuery = read.rawQuery("select count(1) from blackNumber", null);
			while (rawQuery.moveToNext()) {
				count = rawQuery.getInt(0);
			}
		}
		return count;
	}

	/**
	 * 根据当前号码查询该号码的拦截方式
	 * @param mBlackNumberDao
	 */
	public int findPhone(String phone) {
	
		int count = 0;
		if (mSqlite != null) {
			SQLiteDatabase read = mSqlite.getReadableDatabase();
			Cursor rawQuery = read.rawQuery("select model from blackNumber where phone =?",new String[] {phone});
			while (rawQuery.moveToNext()) {
				count = rawQuery.getInt(0);
			}
		}
		return count;
	}

}
