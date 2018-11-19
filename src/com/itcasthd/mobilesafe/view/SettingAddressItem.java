package com.itcasthd.mobilesafe.view;

import com.itcasthd.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 设置中心电话归属地设置
 * @author Administrator
 *
 */
public class SettingAddressItem extends RelativeLayout {

	public SettingAddressItem(Context context) {
		this(context,null);
	}

	public SettingAddressItem(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public SettingAddressItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View.inflate(context, R.layout.setting_address_item, this);
		
		
	}
	
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void SetTitle(String title) {
		TextView tv_phone_titleMes = (TextView)findViewById(R.id.tv_phone_titleMes);
		tv_phone_titleMes.setText(title);
	}
	
	/**
	 * 设置描述
	 * @param content
	 */
	public void SetContent(String content) {
		TextView tv_phone_titleDes = (TextView)findViewById(R.id.tv_phone_titleDes);
		tv_phone_titleDes.setText(content);
	}

}
