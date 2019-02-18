package com.itcasthd.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Administrator
 * 加锁应用程序的数据库
 *
 */
public class AppLockOpenHelper extends SQLiteOpenHelper {

	public AppLockOpenHelper(Context context) {
		super(context, "lock.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 
		db.execSQL("create table lockApp(_id integer primary key autoincrement,packagename varchar(50));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	

}
