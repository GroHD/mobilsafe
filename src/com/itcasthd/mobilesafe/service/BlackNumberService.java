package com.itcasthd.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.itcasthd.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class BlackNumberService extends Service {

	private BlackNumberReceiver mBlackNumberReceiver;
	private BlackNumberDao mBlackNumberDao;
	private TelephonyManager mTm;
	private TelephonyReceiver mTeleListne;
	private MyContentObjServer blackNumberContent;
	private MyContentObjServer sMSContent;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// 拦截短信
		IntentFilter intentFilter = new IntentFilter();
		// 注册短信广播接收者
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		// 注册优先级
		intentFilter.setPriority(Integer.MAX_VALUE);
		mBlackNumberReceiver = new BlackNumberReceiver();
		registerReceiver(mBlackNumberReceiver, intentFilter);

		mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mTeleListne = new TelephonyReceiver();
		mTm.listen(mTeleListne, PhoneStateListener.LISTEN_CALL_STATE);
	}

	// 内部类监听电话状态

	class TelephonyReceiver extends PhoneStateListener {

		

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				BlackNumberDao dao = BlackNumberDao.getBlackNumberDao(getApplicationContext());
				int mode = dao.findPhone(incomingNumber);
				// 1 是短信 2是电话 3是短信和电话
				if (mode == 2 || mode == 3) {
					Class<?> clazz;
					try {
						clazz = Class.forName("android.os.ServiceManager");
						Method method = clazz.getMethod("getService", String.class);
						IBinder ibinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
						ITelephony asInterface = ITelephony.Stub.asInterface(ibinder);
						asInterface.endCall();
						//删除电话
						Uri uri = Uri.parse("content://call_log/calls");
						blackNumberContent = new MyContentObjServer(new Handler(),uri,"number = ?",new String[] {incomingNumber});
						getContentResolver().registerContentObserver(uri, true, blackNumberContent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			}
		}
	}
	//内容观察者，当数据库发生改变的时候调用这个类里的方法。
	class MyContentObjServer extends ContentObserver{

		private Uri uri;
		private String where;
		private String [] params;
		public MyContentObjServer(Handler handler,Uri uri,String  where,String [] params) {
			super(handler);
			this.uri = uri;
			this.where = where;
			this.params = params;
			
		}
		
		//当uri所连接的数据库改变以后删除数据库的数据
		@Override
		public void onChange(boolean selfChange) {
			//删除
			Log.i("Delete", "Delete:");
			getContentResolver().delete(uri, where, params);
		}
		
	}

	// 内部类监听短信是谁发出的
	class BlackNumberReceiver extends BroadcastReceiver {

		

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objes = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objes) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
				// 拿到电话号码,判断电话号码是否在黑名单中存在
				String phone = sms.getOriginatingAddress();
				mBlackNumberDao = BlackNumberDao.getBlackNumberDao(context);
				int mode = mBlackNumberDao.findPhone(phone);
				if (mode == 1 || mode == 3) {
				//	abortBroadcast();
					//拦截之后删除短信（现在的BUG就是要删除一次性都删除了短信，无法删除当前收到的短信）
					Uri uri = Uri.parse("content://sms");
					sMSContent = new MyContentObjServer(new Handler(),uri," address=?",new String[] {phone});
					getContentResolver().registerContentObserver(uri, true, sMSContent);
				}

			}

		}

	}

	@Override
	public void onDestroy() {

		if (mBlackNumberReceiver != null) {
			unregisterReceiver(mBlackNumberReceiver);
		}
		
		if(blackNumberContent!=null) {
			getContentResolver().unregisterContentObserver(blackNumberContent);
		}
		if(sMSContent!=null) {
			getContentResolver().unregisterContentObserver(sMSContent);
		}
	}

}
