package com.itcasthd.mobilesafe.view;

import com.itcasthd.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private CheckBox cb_autoUpdate;
	private TextView tv_ttleDes;
	//自定义控件属性
	/**
	 * 标题
	 * */
	private String mDestitle;
	/**
	 * 为选中的属性
	 * */
	private String mDesoff;
	/**
	 * 选中的属性
	 * */
	private String mDeson;

	public SettingItemView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 获取自定义和原生属性
		initAttrs(attrs);
		// 将xml转换为view 最后一个参数是否将该view转换为该对象
		View.inflate(context, R.layout.setting_item_view, this);
		TextView tv_titileMess = (TextView) findViewById(R.id.tv_titleMes);
		tv_ttleDes = (TextView) findViewById(R.id.tv_titleDes);
		cb_autoUpdate = (CheckBox) findViewById(R.id.cb_autoUpdate);
		
		tv_titileMess.setText(mDestitle);
	
	}

	/**
	 * 
	 * @param attrs
	 *            构造方法中维护好的属性集合 返回属性集合中自定义的属性
	 */
	private void initAttrs(AttributeSet attrs) {
		//第一个参数是命名空间  第二个参数是自定义属性的名字
		mDestitle = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itcasthd.mobilesafe",
				"destitle");
		mDesoff = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itcasthd.mobilesafe", "desoff");
		mDeson = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itcasthd.mobilesafe", "deson");
	}

	/**
	 * 判断checked是否选中的方法
	 * 
	 * @return 返回当前setting_item_view 里的checkbox 是否选中状态,true 选中 fasle 没选中
	 */
	public boolean isCheck() {

		return cb_autoUpdate.isChecked();
	}

	/**
	 * @param bool
	 *            是否选中checked,由点击checkbox来切换状态
	 */
	public void setCheck(boolean bool) {
		cb_autoUpdate.setChecked(bool);
		if (bool) {
			tv_ttleDes.setText(mDeson);

		} else {
			tv_ttleDes.setText(mDesoff);
		}

	}

}
