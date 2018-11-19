package com.itcasthd.mobilesafe.Utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;

/**
 * 
 * 判断服务的状态
 * 
 * @author Administrator
 *
 */
public class ServiceUtils {
	/**
	 * 根据服务器名判断服务是否运行中
	 * 
	 * @param serviceName
	 *            服务名
	 * @return true 运行中 false 没有运行
	 */
	public static boolean isRunning(Context context, String serviceName) {
		boolean blRunning = false;
		// activityManager 管理者对象,可以获取当前手机正在运行的所有服务
		ActivityManager mAm = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		// 获取手机中正在运行的服务 参数是那要拿回多少个服务
		List<RunningServiceInfo> runningServices = mAm.getRunningServices(100);
		for (RunningServiceInfo rfi : runningServices) {
				//判断服务名称是否和传递进来的服务名称是否一样,一样则代表有运行的服务 
				if(rfi.service.getClassName().equals(serviceName))
				{
					blRunning = true;
					break;
				}
		}
		return blRunning;
	}
}
