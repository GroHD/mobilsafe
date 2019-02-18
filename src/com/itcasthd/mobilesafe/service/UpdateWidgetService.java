package com.itcasthd.mobilesafe.service;

import java.io.FilterReader;
import java.util.Timer;
import java.util.TimerTask;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.db.dao.domin.ProcessInfo;
import com.itcasthd.mobilesafe.engine.AppProcessInfoProvider;
import com.itcasthd.mobilesafe.mainActivity.MainActivity;
import com.itcasthd.mobilesafe.receuver.MyAppWidgetProvider;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 
 * @author Administrator
 *
 */
public class UpdateWidgetService extends Service {

	private Timer timer;
	private InnterReceiver mFilterReader;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 获取进程总数和可用内存数(使用定时器)
		startTime();
		// 注册锁屏和解锁的广播接受者
		IntentFilter intentFilter = new IntentFilter();
		// 解锁
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		// 锁屏
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		mFilterReader = new InnterReceiver();
		// 注册广播接受者
		registerReceiver(mFilterReader, intentFilter);
	}

	// 解锁和锁屏的广播接受
	class InnterReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == Intent.ACTION_SCREEN_ON) {
				// 解锁,开启运行的定时器
				if (timer != null) {
					startTime();
				}
			} else if (intent.getAction() == Intent.ACTION_SCREEN_OFF) {
				// 锁屏 取消运行的定时器
				if (timer != null) {
					timer.cancel();
					// 让GC回收垃圾
					timer.purge();
				}
			}

		}

	}

	private void startTime() {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO 定时刷新数据
				updateAppWidget();

			}
		}, 0, 5000);

	}

	private void updateAppWidget() {
		// TODO
		// 获取appWidget对象
		AppWidgetManager awm = AppWidgetManager.getInstance(this);
		// 获取窗体小部件对应的布局转换成的view对象（第一个参数是该应用的包名，第二个是窗口小部件的布局文件）
		RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.example_appwidget);
		// 给窗口小部件View对象，内部控件赋值
		remoteViews.setTextViewText(R.id.tv_widget_process_count,
				"运行中程序" + AppProcessInfoProvider.getAppProcessCount(getApplicationContext()) + "个");
		// 获取可用内存大小
		long availSpace = AppProcessInfoProvider.getAvailSpace(getApplicationContext());
		// 获取总内存大小
		long totalSpace = AppProcessInfoProvider.getTotalSpace(getApplicationContext());
		String availSpaceStr = Formatter.formatFileSize(getApplicationContext(), availSpace);
		String totalSpaceStr = Formatter.formatFileSize(getApplicationContext(), totalSpace);
		remoteViews.setTextViewText(R.id.tv_widget_memony_size, "内存：" + availSpaceStr + "/" + totalSpaceStr + "");
		// 点击窗体小部件进入应用
		// 第一个参数就是在哪个控件上响应点击事件
		// 第二个参数是延期的意图
		Intent intent = new Intent("android.intent.action.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		PendingIntent activity = PendingIntent.getActivity(getApplicationContext(), 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.rl_root, activity);
		// 使用延期意图发送广播杀死进程(使用自定义广播)
		Intent killProcess = new Intent("android.intent.action.KILL_BACKGROUND_PROCESS");
		PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 0, killProcess,
				PendingIntent.FLAG_CANCEL_CURRENT);

		remoteViews.setOnClickPendingIntent(R.id.btn_clear_memony, broadcast);

		// 告诉widget更新控件
		// 第一个参数是上下文，第二个参数是窗体小部件广播接收者的字节码文件
		ComponentName componentName = new ComponentName(getApplicationContext(), MyAppWidgetProvider.class);
		// 更新窗体小部件
		awm.updateAppWidget(componentName, remoteViews);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 开启服务
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO 停止服务
		//停止广播接收
		if(mFilterReader!=null) {
			unregisterReceiver(mFilterReader);
		}
		//取消定时任务
		if(timer!=null) {
			timer.cancel();
			timer.purge();
		}
		super.onDestroy();
	}

}
