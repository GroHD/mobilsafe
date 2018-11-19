package com.itcasthd.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/*
 * 自定义控件
 * 能够获取焦点的自定义textview
 * */
public class FousTextView extends TextView {

	//这个是使用在通过java代码创建控件
	public FousTextView(Context context) {
		super(context);
	}
	//这个方法由系统调用(带属性和上下文)
	public FousTextView(Context context,AttributeSet  attrs) {
		super(context,attrs);
	}
	//由系统调用(带上下文和属性以及在布局文件中定义的样式文件)
	public FousTextView(Context context,AttributeSet  attrs,int defstyle) {
		super(context,attrs,defstyle);
		// TODO Auto-generated constructor stub
	}
	
	//重写获取焦点的方法，由系统调用
	@Override
	public boolean isFocused() {
		//必须获取焦点
		return true;
	}
	

}
