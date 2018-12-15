package com.itcasthd.mobilesafe.process;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.service.LockClearProcessService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ProcessManagerSettingActivity extends Activity {

	private Intent mLockClearProcessServiceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_setting);
		initUI();
	}

	/**
	 * 初始化UI表单
	 */
	private void initUI() {
		final CheckBox mCb_systemIsShow = (CheckBox) findViewById(R.id.cb_systemIsShow);
		// 回显选中
		boolean showSystem = SpUtils.getBoolean(getApplicationContext(), ContentValue.SYSTEMPROCESSISSHOW, false);
		if (showSystem) {
			mCb_systemIsShow.setText("显示系统进程");
		} else {
			mCb_systemIsShow.setText("隐藏系统进程");
		}
		mCb_systemIsShow.setChecked(showSystem);
		// 点击改变文字
		mCb_systemIsShow.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mCb_systemIsShow.setText("显示系统进程");
				} else {
					mCb_systemIsShow.setText("隐藏系统进程");
				}
				SpUtils.putBoolean(getApplicationContext(), ContentValue.SYSTEMPROCESSISSHOW, isChecked);
			}
		});
		final CheckBox mCb_lockClear = (CheckBox) findViewById(R.id.cb_lockClear);
		boolean lockClear = SpUtils.getBoolean(getApplicationContext(), ContentValue.SYSTEMPROCESSLOCKCLEAR, false);
		mCb_lockClear.setChecked(lockClear);
		if (lockClear) {
			mCb_lockClear.setText("锁屏清理已开启");
		} else {
			mCb_lockClear.setText("锁屏清理已关闭");
		}
		mCb_lockClear.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// 开启服务
				if (isChecked) {
					mLockClearProcessServiceIntent = new Intent(getApplicationContext(), LockClearProcessService.class);
					startService(mLockClearProcessServiceIntent);
					mCb_lockClear.setText("锁屏清理已开启");

				} else {
					// 停止服务
					// if (mLockClearProcessReceiver != null) {
					// // unregisterReceiver(mLockClearProcessReceiver);
					// }
					if (mLockClearProcessServiceIntent != null) {
						stopService(mLockClearProcessServiceIntent);
					}
					mCb_lockClear.setText("锁屏清理已关闭");
				}
				SpUtils.putBoolean(getApplicationContext(), ContentValue.SYSTEMPROCESSLOCKCLEAR, isChecked);
			}
		});
	}
}
