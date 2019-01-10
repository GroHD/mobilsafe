package com.itcasthd.mobilesafe.receuver;

import java.util.List;

import com.itcasthd.mobilesafe.db.dao.domin.ProcessInfo;
import com.itcasthd.mobilesafe.engine.AppProcessInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *杀死进程的广播接收者
 * @author Administrator
 *
 */
public class KillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 杀死进程
		List<ProcessInfo> processInfoList = AppProcessInfoProvider.getProcessInfoList(context);
		for(ProcessInfo info:processInfoList) {
			AppProcessInfoProvider.killProcess(context, info);
		}
		
	}

}
