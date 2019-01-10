package com.itcasthd.mobilesafe.receuver;

import com.itcasthd.mobilesafe.service.UpdateWidgetService;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * 添加窗口的小部件，需要继承AppWidgetProvider
 * 
 * @author Administrator
 *
 */
@SuppressLint("NewApi")
public class MyAppWidgetProvider extends AppWidgetProvider {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("mess", "onReceive  这个方法每次都第一个调用该方法");
		super.onReceive(context, intent);
	}
	//创建一个窗体小部件方法
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		Log.i("mess", "onEnabled  创建第一个窗口小部件");
		//开启服务
		context.startService(new Intent(context,UpdateWidgetService.class));
		super.onEnabled(context);
		
	}
	//窗口小部件更新
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Log.i("mess", "onUpdate  创建多一个窗口小部件");
		context.startService(new Intent(context,UpdateWidgetService.class));
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
	}
	//当窗体小部件宽高发生改变的时候调用该方法,创建小部件也调用这个方法
	@Override
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		Log.i("mess", "onAppWidgetOptionsChanged  创建多一个窗口小部件");
		// TODO Auto-generated method stub
		context.startService(new Intent(context,UpdateWidgetService.class));
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
	}
	
	//当删除最后一个小部件的时候回调用该方法
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		//停止服务
		context.stopService(new Intent(context,UpdateWidgetService.class));
		super.onDisabled(context);
	}
	
	//删除窗口小部件，删除一个调用一次这个方法
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Log.i("mess", "onUpdate  删除一个窗口小部件");
		super.onDeleted(context, appWidgetIds);
		
	}
	

}
