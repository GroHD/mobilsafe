package com.itcasthd.mobilesafe.tetupoverActivity;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;;

public class SetUpOverActivity extends Activity {

	private Button btn_resut_set;
	private TextView tv_phone;
	private ImageView iv_set_lock;

	/*
	 * 设置界面
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boolean setUpOver = SpUtils.getBoolean(getApplicationContext(), ContentValue.SET_UP_OVER, false);
		if (setUpOver) {
			// 设置密码成功，并且已设置了对应的导航界面 -->那么就展示到功能列表
			setContentView(R.layout.activity_setup_over);
			initUi();
			initData();
			btn_resut_set.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(), SetUp1Activity.class);
					startActivity(intent);
					finish();
				}
			});
		} else {
			// 没有设置导航界面, 跳转到导航界面的设置
			Intent intent = new Intent(this, SetUp1Activity.class);
			startActivity(intent);
			// 关闭当前界面
			finish();

		}

		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		String phone = SpUtils.getString(getApplicationContext(), ContentValue.CONTACT, "");
		Boolean checked = SpUtils.getBoolean(getApplicationContext(), ContentValue.START_PROTECTION, false);
		if (checked) {
			iv_set_lock.setImageResource(R.drawable.lock);
		} else {
			iv_set_lock.setImageResource(R.drawable.unlock);
		}
		tv_phone.setText(phone);

	}

	private void initUi() {
		btn_resut_set = (Button) findViewById(R.id.btn_resut_set);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		iv_set_lock = (ImageView) findViewById(R.id.iv_set_lock);
	}
}
