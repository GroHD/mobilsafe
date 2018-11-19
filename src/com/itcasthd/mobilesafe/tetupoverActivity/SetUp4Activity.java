package com.itcasthd.mobilesafe.tetupoverActivity;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.mainActivity.MainActivity;
import com.itcasthd.mobilesafe.receuver.DeviceAdminRecever;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

public class SetUp4Activity extends BaseSetupActivity {

	private CheckBox cb_fdbaohu;
	private ComponentName mComponentName;
	private DevicePolicyManager mDPM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		initUi();
		StartDeviceManager();
		boolean bhchecked = SpUtils.getBoolean(getApplicationContext(), ContentValue.START_PROTECTION, false);
		if (bhchecked) {
			cb_fdbaohu.setText("已开启防盗保护");
		} else {
			cb_fdbaohu.setText("没有开启防盗保护");
		}
		cb_fdbaohu.setChecked(bhchecked);
		cb_fdbaohu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean bhChecked = cb_fdbaohu.isChecked();
				if (bhChecked) {

					// 激活设备管理器
					if (mDPM.isAdminActive(mComponentName)) {
						cb_fdbaohu.setText("已开启防盗保护");
						SpUtils.putBoolean(getApplicationContext(), ContentValue.START_PROTECTION, bhChecked);
					} else {
						Toast.makeText(getApplicationContext(), "请激活设备管理器", 0).show();
						cb_fdbaohu.setText("没有开启防盗保护");
						cb_fdbaohu.setChecked(false);
						SpUtils.Remove(getApplicationContext(), ContentValue.START_PROTECTION);
					}

				} else {
					cb_fdbaohu.setText("没有开启防盗保护");
					SpUtils.Remove(getApplicationContext(), ContentValue.START_PROTECTION);
				}
			}
		});

	}

	/**
	 * 开启设备管理器
	 */
	private void StartDeviceManager() {
		try {
			mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
			mComponentName = new ComponentName(this, DeviceAdminRecever.class);
			if (!mDPM.isAdminActive(mComponentName)) {
				Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
				intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "手机安全卫士");
				startActivity(intent);
			}

		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.getMessage(), 1).show();
		}
	}

	/**
	 * 
	 * 初始化UI
	 */
	private void initUi() {
		cb_fdbaohu = (CheckBox) findViewById(R.id.cb_fdbaohu);
	}

	@Override
	public void Move_Next() {
		SpUtils.putBoolean(getApplicationContext(), ContentValue.SET_UP_OVER, true);
		Intent intent = new Intent(this, SetUpOverActivity.class);
		startActivity(intent);
		finish();
		// 开启平移动画
		overridePendingTransition(R.anim.next_to_in, R.anim.next_to_out);

	}

	@Override
	public void Move_Pre() {
		Intent intent = new Intent(this, SetUp3Activity.class);
		startActivity(intent);
		finish();
		// 开启平移动画,上一页移入,本页移出
		overridePendingTransition(R.anim.pre_to_in, R.anim.pre_to_out);

	}
}
