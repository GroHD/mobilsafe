package com.itcasthd.mobilesafe.receuver;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.service.LocationService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	/**
	 * 监听短信广播
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// 拨打电话

		// 判断是否开启防盗保护
		if (SpUtils.getBoolean(context, ContentValue.START_PROTECTION, false)) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			// 获取内容的格式
			for (Object obj : objs) {
				// [1] 获取smsmanager实例
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
				// 拿到短信的内容
				String messageBody = sms.getMessageBody();
				// 是否拦截该短信
				boolean isLanJie = false;
				// 判断是否包含播放报警短信的关键字
				if ("#*alarm*#".contains(messageBody)) {
					// 播放音乐
					MediaPlayer mp = MediaPlayer.create(context, R.raw.waring);
					// 设置一直循环
					mp.setLooping(true);
					mp.start();
					isLanJie = true;
				} else if ("#*location*#".contains(messageBody)) {
					// 开启一个服务
					context.startService(new Intent(context, LocationService.class));
					isLanJie = true;
				} else if ("#*lockscreen*#".contains(messageBody)) {
					// 锁屏
					ComponentName componentName = new ComponentName(context, DeviceAdminRecever.class);
					DevicePolicyManager dpm = (DevicePolicyManager) context
							.getSystemService(context.DEVICE_POLICY_SERVICE);
					if (dpm.isAdminActive(componentName)) {
						dpm.lockNow();
					}
					isLanJie = true;
				} else if ("#*wipedata*#".contains(messageBody)) {
					// 恢复出厂设备(请勿测试)
					ComponentName componentName = new ComponentName(context, DeviceAdminRecever.class);
					DevicePolicyManager dpm = (DevicePolicyManager) context
							.getSystemService(context.DEVICE_POLICY_SERVICE);
					if (dpm.isAdminActive(componentName)) {
						// 本机
						dpm.wipeData(0);
						// 清空内存卡
						dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);

					}
					isLanJie = true;
				}
				if (isLanJie) {
					// 拦截短信,不广播短信（4.0 以上的手机无效）
					abortBroadcast();
					// 删除短信

				}
			}

		}
	}

}
