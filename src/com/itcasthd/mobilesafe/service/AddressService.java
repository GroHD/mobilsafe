package com.itcasthd.mobilesafe.service;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.engine.AddressDao;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 显示来电归属地的服务
 * */
public class AddressService extends Service {

	private TelephonyManager mTm;
	private TextView mTv_address;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			String address = msg.obj.toString();

			mTv_address.setText(address);
		}

	};
	/*
	 * 自定义toast
	 */
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private View mToast; // 自定提toast的view对象
	private WindowManager mWm;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		String phoneNum = intent.getStringExtra("phoneNum");
		showToast(phoneNum);
		return START_NOT_STICKY;
	}

	private void query(final String phone) {

		new Thread() {
			public void run() {
				String address = "";
				if (phone.endsWith("18747129831")) {
					address = "亲爱的老公来电话了";
				} else if (phone.equals("15049230049") || phone.equals("15561097401")) {
					address = "亲爱的老婆来电话了";
				} else {
					address = AddressDao.getAddress(phone);
				}

				// 发送消息
				Message msg = new Message();
				msg.obj = address;
				mHandler.sendMessage(msg);
			};

		}.start();
	}

	public void showToast(String incomingNumber) {
		// 查询电话归属地
		query(incomingNumber);
		/*
		 * 自定义Toast mParams 是全局变量 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 不可以被触摸的
		 */

		final WindowManager.LayoutParams params = mParams;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		// 在响铃的时候显示toast TYPE_PHONE(在响铃的时候显示toast 和电话类型一致)
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		// 指定toast起始位置在位置 (左上角)
		params.gravity = Gravity.LEFT + Gravity.TOP;

		params.setTitle("Toast");
		// 显示toast效果

		mToast = View.inflate(getApplicationContext(), R.layout.toastview, null);
		// 设置显示的位置
		params.x = SpUtils.getInt(getApplicationContext(), ContentValue.LOCATION_X, 0);
		params.y = SpUtils.getInt(getApplicationContext(), ContentValue.LOCATION_y, 0);
		Log.i("X:Y", params.x + ":" + params.y);
		// 显示归属地信息
		mTv_address = (TextView) mToast.findViewById(R.id.tv_call_address);
		// 从sp中获取文件的索引匹配图片
		int[] drable = { R.drawable.call_locate_white, R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		// 拿到选中的index
		int bagk_index = SpUtils.getInt(getApplicationContext(), ContentValue.TOAST_STYLE, 0);
		// 设置背景图片
		mTv_address.setBackgroundResource(drable[bagk_index]);
		// 移动
		mToast.setOnTouchListener(new OnTouchListener() {

			private float startX;
			private float startY;
			// 拿到适配屏幕的宽和高

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 拿到开始的x和y
					startX = event.getX();
					startY = event.getY();

					break;
				case MotionEvent.ACTION_MOVE:
					// 拿到当前移动的x和y点
					float moveX = event.getX();
					float moveY = event.getY();
					float endX = moveX - startX;
					float endY = moveY - startY;

					System.out.println("moveX:" + moveX + " moveY:" + moveY);
					// 移动  设置最新的地址
					params.x = params.x + (int) endX;
					params.y = params.y + (int) endY;
					// 告诉窗体 按照手势的移动去更新位置
					mWm.updateViewLayout(mToast, params);
					
					break;
				case MotionEvent.ACTION_UP:
					// 抬起
					SpUtils.putInt(getApplicationContext(), ContentValue.LOCATION_X, params.x);
					SpUtils.putInt(getApplicationContext(), ContentValue.LOCATION_y, params.y);
					break;

				}
				return true;
			}
		});

		// 将toast挂在到windowManager窗体上才可以使用(需要权限 android.permission.SYSTEM_ALERT_WINDOW)
		mWm.addView(mToast, params);

	}

	@Override
	public void onStart(Intent intent, int startId) {

	}

	@Override
	public void onDestroy() {
		if (mWm != null && mToast != null) {
			// 关闭电话的监听
			mWm.removeView(mToast);

		}
	}

}
