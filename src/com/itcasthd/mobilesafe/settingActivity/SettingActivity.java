package com.itcasthd.mobilesafe.settingActivity;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.ServiceUtils;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.service.AddressService;
import com.itcasthd.mobilesafe.service.BlackNumberService;
import com.itcasthd.mobilesafe.service.RocketService;
import com.itcasthd.mobilesafe.view.SettingAddressItem;
import com.itcasthd.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class SettingActivity extends Activity {

	private SettingAddressItem mSdi_gsd_item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initUpdate();
		// 是否显示归属地
		initGuiShuDiShow();
		// 设置显示归属地的样式
		initGuiShuDiSetting();
		// 设置归属地显示的位置
		initGuiShuDiLocation();
		// 开启黑名单拦截
		initBlackNumber();

	}

	/**
	 * 开启黑名单拦截
	 */
	private void initBlackNumber() {
		
		final SettingItemView siv = (SettingItemView) findViewById(R.id.si_blackNumber);
		//如果服在运行的时候则返回true,否则返回false
		Boolean isRuning = ServiceUtils.isRunning(getApplicationContext(), "com.itcasthd.mobilesafe.service.BlackNumberService");
		siv.setCheck(isRuning);
		// 设置点击事件
		siv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 拿到是否选中
				boolean is_checked = siv.isCheck();
			
				siv.setCheck(!is_checked);
				boolean isChecked = siv.isCheck();
				SpUtils.putBoolean(getApplicationContext(),  ContentValue.BLACKNUMBERCONFIG, isChecked);
				//判断是否选中
				if(isChecked) {
					
				
					//选中开启服务
					startService(new Intent(getApplicationContext(),BlackNumberService.class));
				}
				else {
					//停止服务
					stopService(new Intent(getApplicationContext(),BlackNumberService.class));
				}

			}
		});
	}

	/**
	 * 设置归属地位置
	 */
	private void initGuiShuDiLocation() {
		SettingAddressItem sai_gsd_location = (SettingAddressItem) findViewById(R.id.sai_gsd_location);
		sai_gsd_location.SetTitle("提示归属地提示框位置");
		sai_gsd_location.SetContent("设置提示框归属地位置");
		sai_gsd_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), ToastLocation.class));
			}
		});

	}

	// 设置归属地的样式
	private void initGuiShuDiSetting() {

		mSdi_gsd_item = (SettingAddressItem) findViewById(R.id.sai_gsd_item);
		// 描述
		mSdi_gsd_item.SetTitle("设置归属地显示风格");
		final String[] items = { "透明", "橙色", "蓝色", "灰色", "绿色" };
		// 拿到存储的归属地样式
		final int toast_style = SpUtils.getInt(getApplicationContext(), ContentValue.TOAST_STYLE, 0);
		// 通过索引，获取字符串数组中的文件，显示给描述内容控件
		String toast_item = items[toast_style];
		mSdi_gsd_item.SetContent(toast_item);
		// 监听点击事件,弹出对话框
		mSdi_gsd_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹出一个对话框
				showToastStyleDialog("设置归属地显示风格", items, toast_style);
			}
		});

	}

	/**
	 * 选择样式的对话框
	 */
	private void showToastStyleDialog(String title, final String[] items, int selectIndex) {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		// 设置样式
		builder.setIcon(R.drawable.ic_launcher);

		builder.setSingleChoiceItems(items, selectIndex, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SpUtils.putInt(getApplicationContext(), ContentValue.TOAST_STYLE, which);
				mSdi_gsd_item.SetContent(items[which]);
				dialog.dismiss();

			}
		});
		builder.show();
	}

	// 是否显示归属地
	private void initGuiShuDiShow() {
		// 判断服务是否开启,如果服务启动就开启来电归属地，服务关闭就关闭来电归属地
		boolean isRunning = ServiceUtils.isRunning(getApplicationContext(), AddressService.class.getName());

		SpUtils.getBoolean(getApplicationContext(), ContentValue.GUISHUDISHOW, isRunning);
		updateItemView(R.id.si_gsd, ContentValue.GUISHUDISHOW);
	}

	public void initUpdate() {
		updateItemView(R.id.si_setting, ContentValue.AUTOUPDATE);
	}

	/**
	 * 更新控件属性
	 * 
	 * @param rid
	 *            控件id
	 * @param contentValue
	 *            节点id
	 */
	public void updateItemView(final int rid, final String contentValue) {

		// 获取是否更新属性
		boolean checked = SpUtils.getBoolean(this, contentValue, false);
		final SettingItemView siv = (SettingItemView) findViewById(rid);
		siv.setCheck(checked);
		// 设置点击事件
		siv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 拿到是否选中
				boolean is_checked = siv.isCheck();
				SpUtils.putBoolean(SettingActivity.this, contentValue, (!is_checked));
				siv.setCheck(!is_checked);

			}
		});

	}
}
