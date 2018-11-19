package com.itcasthd.mobilesafe.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.itcasthd.mobilesafe.db.dao.domin.ProcessInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 获取进程信息
 * 
 * @author Administrator
 *
 */
public class AppProcessInfoProvider {

	/**
	 * 获取进程总数
	 * 
	 * @param context
	 *            上下文
	 * @return 返回进程总数
	 */
	public static int getAppProcessCount(Context context) {
		try {
			// 获取activityManager
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			// 获取正在运行的进程集合
			List<RunningAppProcessInfo> runList = am.getRunningAppProcesses();
			// 返回进程的数量
			return runList.size();
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 获取可用的内存数
	 * 
	 * @param context
	 * @return 返回可用的内存大小
	 */
	public static long getAvailSpace(Context context) {
		try {
			// 获取activityManager
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			// 构建存储可用内存的对象
			MemoryInfo memoryInfo = new MemoryInfo();
			// 给MemoryInfo对象赋值(可用内存)
			am.getMemoryInfo(memoryInfo);
			// 获取memoryInfo中可用内存大小
			return memoryInfo.availMem;

		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 获取总共的内存大小
	 * 
	 * @param context
	 * @return 总共的内存大小
	 */
	public static long getTotalSpace(Context context) {
		FileReader fileReader = null;
		BufferedReader buffer = null;
		try {
			// //获取activityManager
			// ActivityManager am = (ActivityManager)
			// context.getSystemService(Context.ACTIVITY_SERVICE);
			// //构建存储可用内存的对象
			// MemoryInfo memoryInfo = new MemoryInfo();
			// //给MemoryInfo对象赋值(可用内存)
			// am.getMemoryInfo(memoryInfo);
			// //获取memoryInfo中可用内存大小(totalMem 如果在低版本则无法使用)
			// return memoryInfo.totalMem;
			// 上面是14以上的版本在可以使用的，低版本可以使用以下的方法获取
			// 低版本可以读取proc/meminfo文件中的第一行就是总内存的大小,获取数字字符即可
			fileReader = new FileReader("proc/meminfo");
			buffer = new BufferedReader(fileReader);
			String memInfo = buffer.readLine();
			// 将字符串转换为字符数组
			char[] charArray = memInfo.toCharArray();
			// 循环遍历每一个字符,如果字符的ASCII在0到9的区域内，说明该字符是数字
			StringBuilder sbstr = new StringBuilder();
			for (char chars : charArray) {
				if (chars >= '0' && chars <= '9') {
					sbstr.append(chars);
				}
			}
			if (!"".equals(sbstr.toString())) {
				return Long.parseLong(sbstr.toString()) * 1024;
			}
			return 0;

		} catch (Exception ex) {
			return 0;
		} finally {

			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (buffer != null) {
					buffer.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 获取进程信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<ProcessInfo> getProcessInfoList(Context context) {
		try {
			List<ProcessInfo> processList = new ArrayList<ProcessInfo>();
			PackageManager pm = context.getPackageManager();
			// 获取进程相关信息 ProcessInfo 是自己创建的model
			// 获取activityManager管理者对象
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			// 获取正在运行的进程的集合
			List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
			// 遍历上面的信息,获取进程相关的信息(名称,包名，图标，已使用的内存大小，是否为系统进程(状态机))
			for (RunningAppProcessInfo process : runningAppProcesses) {
				ProcessInfo processInfo = new ProcessInfo();
				// 获取进程的名称 ==应用包名
				processInfo.setPackageName(process.processName);
				// 获取进程占用的内存大小(根据pid拿到该应用所占的内存)
				android.os.Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[] { process.pid })[0];
				// 获取已使用的内存
				processInfo.setMemSize(memoryInfo.getTotalPrivateDirty() * 1024);

				// 获取应用名称
				try {
					ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.getPackageName(), 0);
					processInfo.setName(applicationInfo.loadLabel(pm).toString());
					// 获取图标
					processInfo.setIcon(applicationInfo.loadIcon(pm));
					// 判断是否为系统进程
					if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
						processInfo.setSystem(true);
					} else {
						processInfo.setSystem(false);
					}
				} catch (Exception ex) {
					processInfo.setName(process.processName);
					processInfo.setSystem(true);
					processInfo.setIcon(
							context.getResources().getDrawable(com.itcasthd.mobilesafe.R.drawable.ic_launcher));

				}
				processList.add(processInfo);
			}
			return processList;
		} catch (Exception ex) {
			return null;
		} finally {

		}
	}

}
