package com.itcasthd.mobilesafe.atoolActivity;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.engine.AddressDao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QueryAddressActivity extends Activity {
	private EditText mEt_phone;
	private TextView tv_phone_show_address;
	private Button btn_phone_query;
	private String mPhoneAddress;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			tv_phone_show_address.setText(mPhoneAddress);

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_address);
		//
		initUI();
		btn_phone_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phone = mEt_phone.getText().toString().trim();
				if (mEt_phone.length() > 2) {
					query(phone);

				} else {
					Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
					boolean b = mEt_phone.isFocused();
				
					
					//手机震动效果 （必须在清单文件里增加震动的权限  android.permission.VIBRATE）
					Vibrator vm =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					//震动的毫秒
					vm.vibrate(200);
					//规律震动
					/*
					 * 第一个参数：震动的规则(不震动时间,震动时间,....)
					 * 第二个参数：重复的次数  -1则不重复，只执行一次
					 * */
					//vm.vibrate(new long[] {800,500,800,500}, -1);
					
					//使用动画
					mEt_phone.startAnimation(shake);
					
				}
			}

		});
		mEt_phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				//文本框内容改变则处理数据
						String phone = mEt_phone.getText().toString().trim();
						if (mEt_phone.length() > 2) {
							query(phone);
						}
						else {
							tv_phone_show_address.setText("");
						}
						Log.i("txt", "1");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				Log.i("txt", "2");
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.i("txt", "0");
			}
		});
		
		
	}

	/**
	 * 查询电话号码
	 * 
	 * @param phone
	 */
	private void query(final String phone) {
		// TODO
		new Thread() {
			public void run() {
				mPhoneAddress = AddressDao.getAddress(phone);
				handler.sendEmptyMessage(1);
			};

		}.start();

	}

	private void initUI() {
		btn_phone_query = (Button) findViewById(R.id.btn_phone_query);
		mEt_phone = (EditText) findViewById(R.id.et_phone);
		tv_phone_show_address = (TextView) findViewById(R.id.tv_phone_show_address);
	}
}
