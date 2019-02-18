package com.itcasthd.mobilesafe.service;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.engine.AppProcessInfoProvider;
import com.itcasthd.mobilesafe.receuver.MyAppWidgetProvider;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

public class MyNotificationService extends Service {

	private NotificationManager nm = null;
	private  Handler  handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			Notification noti = (Notification)msg.obj;
		//	nm.notify(10, noti);
		}
		
	};
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		new Thread() {
			int index = 0;
			
			public void run() {
				nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				// 自定义通知栏样式
				RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.example_appwidget);
				//设置信息
				remoteViews.setTextViewText(R.id.tv_widget_process_count, (index++)+"运行中的程序"+AppProcessInfoProvider.getAppProcessCount(getApplicationContext())+"个");
				long availSpace = AppProcessInfoProvider.getAvailSpace(getApplicationContext());
				long totalSpace = AppProcessInfoProvider.getTotalSpace(getApplicationContext());
				//可用
				String strAvailSpace = Formatter.formatFileSize(getApplicationContext(), availSpace);
				//共计
				String strTotalSpace = Formatter.formatFileSize(getApplicationContext(), totalSpace);
				remoteViews.setTextViewText(R.id.tv_widget_memony_size,"内存："+strAvailSpace+"/"+strTotalSpace);
				Notification noti = new Notification.Builder(MyNotificationService.this).setContent(remoteViews).setSmallIcon(R.drawable.ic_launcher)
						.getNotification();
				Intent intent = new Intent(getApplicationContext(), MyAppWidgetProvider.class);
				
				noti.flags = Notification.FLAG_NO_CLEAR;
				//增加点击事件发送广播杀死进程
				Intent killProcess = new Intent("android.intent.action.KILL_BACKGROUND_PROCESS");
				PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 0, killProcess, 
																				PendingIntent.FLAG_CANCEL_CURRENT);
				noti.contentView.setOnClickPendingIntent(R.id.btn_clear_memony, broadcast);
				Message  msg = new Message();
				msg.obj = noti;
				handler.sendMessage(msg);
				Log.i("notification", "index:"+index);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			};
			
		}.start();
	}

}
