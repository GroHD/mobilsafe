package com.itcasthd.mobilesafe.receuver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/**
 * 设备管理器广播接收者,这个广播接收者是 DeviceAdminReceiver  的子类
 * */
public class DeviceAdminRecever extends DeviceAdminReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);		
	}
}
