package com.itcasthd.mobilesafe.receuver;

import java.util.List;

import com.itcasthd.mobilesafe.db.dao.domin.ProcessInfo;
import com.itcasthd.mobilesafe.engine.AppProcessInfoProvider;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 锁屏清理进程
 * 
 * @author Administrator
 *
 */
public class LockClearProcessReceiver extends BootReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		// 锁屏清理进程
		Log.i("intent.getAction()", intent.getAction());
		if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
			Log.i("LockCrear", "Clear.....");
			List<ProcessInfo> processInfoList = AppProcessInfoProvider.getProcessInfoList(context);
			for (ProcessInfo processInfo : processInfoList) {
				// 不删除自己的
				if (!processInfo.getPackageName().equals(context.getPackageName())) {
					AppProcessInfoProvider.killProcess(context, processInfo);
				}
			}
		} else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
		}
	}
}
