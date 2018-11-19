package com.itcasthd.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.itcasthd.mobilesafe.db.dao.domin.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author Administrator
 *
 */
public class AppInfoProvider {
	/**
	 * 返回当前手机所有的应用的相关信息(名称，报名，图标（内存，sk卡），（用户，系统）)
	 * @param context  获取包管理者的上下文环境
	 * @return  包含手机安装相关的程序信息
	 */
	public static List<AppInfo>  getAppInfoList(Context  context) {
		//1.包的管理者对象
		PackageManager packageManager = context.getPackageManager();
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		//2,获取安装在手机上应用相关的集合
		List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
		//3,循环遍历信息的集合
		for(PackageInfo packageInfo :packageInfoList) {
			AppInfo appInfo = new AppInfo();
			//4.获取应用的包名
			appInfo.setPackageName(packageInfo.packageName);
			//5,获取应用名称
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			appInfo.setName(applicationInfo.loadLabel(packageManager).toString());
			//6,获取图标
			appInfo.setIcon(applicationInfo.loadIcon(packageManager));
			//7,判断是否为系统应用(每一个手机上的对应的flag都不一样)
			if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
				//系统应用
				appInfo.setSystem(true);
			}
			else {
				//非系统应用
				appInfo.setSystem(false);
			}
			
			//8.是否为SD卡中安装的应用
			if((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
				//sd卡里安装的应用
				appInfo.setSdCard(true);
			}
			else {
				//不再SD卡中安装的应用
				appInfo.setSdCard(false);
			}
			
			appInfoList.add(appInfo);
		}
		return appInfoList;
	}
}
