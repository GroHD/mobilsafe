package com.itcasthd.mobilesafe.service;

import com.itcasthd.mobilesafe.receuver.LockClearProcessReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockClearProcessService extends Service {

	private LockClearProcessReceiver mLockClearProcessReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		
		IntentFilter intent = new IntentFilter();
		intent.addAction("android.intent.action.SCREEN_OFF");
		mLockClearProcessReceiver = new LockClearProcessReceiver();
		registerReceiver(mLockClearProcessReceiver, intent);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		if(mLockClearProcessReceiver!=null) {
			unregisterReceiver(mLockClearProcessReceiver);
		}
		super.onDestroy();
	}

}
