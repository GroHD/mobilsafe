package com.itcasthd.mobilesafe.receuver;

import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.ServiceUtils;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.service.AddressService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * 拨打电话广播接受
 * 
 * @author Administrator
 *
 */
public class CallTelephone extends BroadcastReceiver {

	private boolean isServiceRun = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (SpUtils.getBoolean(context, ContentValue.GUISHUDISHOW, false)) {

			if (intent != null && intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				// 获取拨打的电话号码
				String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				// Intent startService = new Intent(context, AddressService.class);
				// startService.putExtra("phoneNum", phoneNumber);
				// context.startService(startService);
				// showToast(phoneNumber, context);
			} else {
				String phoneNum = intent.getStringExtra("incoming_number");
				TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
				switch (tm.getCallState()) {
				case TelephonyManager.CALL_STATE_RINGING:
					// 响铃未接
					System.out.println("start1");
					 

					if (!ServiceUtils.isRunning(context, AddressService.class.getName())) {
						Intent startService = new Intent(context, AddressService.class);
						startService.putExtra("phoneNum", phoneNum);
						context.startService(startService);
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					// 拨打电话
					// Toast.makeText(context, "phoneNum:"+phoneNum.replace(" ", ""), 1).show();
					if (phoneNum != null) {
						System.out.println("start2"+phoneNum);
						if (!ServiceUtils.isRunning(context, AddressService.class.getName())) {
							// 没有该服务则运行该服务，否则不运行
							Intent CallStartService = new Intent(context, AddressService.class);
							CallStartService.putExtra("phoneNum", phoneNum.replace(" ", ""));
							context.startService(CallStartService);
						}
					}
				

					break;
				case TelephonyManager.CALL_STATE_IDLE:
					Intent stopService = new Intent(context, AddressService.class);
					context.stopService(stopService);
					if (ServiceUtils.isRunning(context, AddressService.class.getName())) {
						context.stopService(new Intent(context, AddressService.class));
					}
					break;
				
				}
			}
		}

	}
}
