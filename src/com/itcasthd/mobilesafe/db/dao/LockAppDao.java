package com.itcasthd.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.itcasthd.mobilesafe.db.AppLockOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Administrator
 * 应用加锁的dao
 *
 */
public class LockAppDao {

	private static LockAppDao dao = null;
	private static AppLockOpenHelper mysqlite;
	
	public static LockAppDao getLockAppDao(Context context) {
			if(dao == null) {
				dao = new LockAppDao();
				mysqlite = new AppLockOpenHelper(context);
			}
			return dao;
	}
	
	/**
	 * 插入数据
	 * @param sql执行的sql语句
	 * @param packageName 加锁的应用程序包名
	 */
	public void insert(String packageName) {
		SQLiteDatabase sql = mysqlite.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put("packagename", packageName);
		sql.insert("lockApp", null, content);
		sql.close();
	}
	
	/**
	 * 删除一条数据
	 * @param packageName 根据加锁的应用程序包名删除一条数据
	 */
	public void delete(String packageName) {
		SQLiteDatabase sql = mysqlite.getWritableDatabase();
		sql.delete("lockApp", " packagename =?", new String [] {packageName});
		sql.close();
	}
	
	//查询所有的加锁的应用
	
	public List<String> getAll() {
		List<String> lockAppList = new ArrayList<String>();
		SQLiteDatabase sql = mysqlite.getWritableDatabase();
		Cursor query = sql.query("lockApp", new String [] {"packagename"}, null, null, null, null, null, null);
		while(query.moveToNext()) {
			String packageName = query.getString(0);
			lockAppList.add(packageName);
		}
		sql.close();
		return lockAppList; 

	}
}
