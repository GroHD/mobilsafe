package com.itcasthd.mobilesafe.receuver;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.service.AddressService;
import com.itcasthd.mobilesafe.service.BlackNumberService;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 监听到开机广播就发短信到安全号码位置
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();

		// if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
		
		checkSIM(context);
		// 检测是否有开启黑名单
		checkBlackNumber(context);
		
		
	
		// }
	}
	
	/**
	 * 检测黑名单是否开启，开启则运行
	 * @param context
	 */
	public void checkBlackNumber(Context context) {
	
		boolean isBlackNumber = SpUtils.getBoolean(context, ContentValue.BLACKNUMBERCONFIG,false);
		if(isBlackNumber) {
			//开启黑名单
			context.startService(new Intent(context,BlackNumberService.class));
		}
	}

	/**
	 * 判断SIM卡是否更改，如果更改则发送短信
	 * @param context
	 */
	public void checkSIM(Context context) {
		// 1.拿到本机的序列号
		String spSimPhoneNum = SpUtils.getString(context, ContentValue.BINDSIM, "");
		// 2.拿到存储的序列号
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);

		if (tm != null) {
			String deviceId = tm.getSimSerialNumber();
			// 3.对比两个序列号
			if (!deviceId.equals(spSimPhoneNum)) {
				// 4.不一样发短信
				SmsManager sms = SmsManager.getDefault();
				String contact = SpUtils.getString(context, ContentValue.CONTACT, "");
				List<String> strList = sms.divideMessage(
						"您的手机更换SIM卡,可发送以下代码：【#*location*# 获取手机位置】  【#*alarm*# 播放报警音乐】【#*wipedata*# 删除手机数据(找不回)】【#*lockscreen*# 手机锁屏】");
				for (int i = 0; i < strList.size(); ++i) {
					sms.sendTextMessage(contact, null, strList.get(i), null, null);
					deleteSIM(context);
				}
				return;
			}

		}

	}

	/*
	 * 删除最新发送的一条短信数据
	 */
	public void deleteSIM(Context context) {
		// 1 拿到内容提供者
		Uri uri = Uri.parse("content://sms/");

		// 2 根据内容提供者拿到最新短信的id
		Cursor query = context.getContentResolver().query(uri, new String[] { "_id", "thread_id" }, null, null,
				"_id desc");
		while (query.moveToNext()) {
			long threadId = query.getLong(1);

			// 3 根据id删除对应的短信
			context.getContentResolver().delete(Uri.parse("content://sms/conversations/" + threadId), null, null);
			break;
		}
		query.close();

		// 4

	}

}
