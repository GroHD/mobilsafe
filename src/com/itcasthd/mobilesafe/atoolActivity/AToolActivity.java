package com.itcasthd.mobilesafe.atoolActivity;

import java.io.File;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.engine.SmsBackUp;
import com.itcasthd.mobilesafe.engine.SmsBackUp.CallBack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AToolActivity extends Activity {

	private TextView tv_query_phone_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
		initUI();
		initSmsBack();
		initCommonNumQuery();
		

	}
	
	/**
	 * 常用号码查询
	 * 使用ExpandableListView 来实现
	 * 
	 */
	private void initCommonNumQuery(){
		TextView tv_commonnum_query =(TextView)findViewById(R.id.tv_commonnum_query);
		tv_commonnum_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),CommonNumQueryAcivity.class));
				
			}
		});
	}
	
	/**
	 * 备份短信
	 */
	public void initSmsBack() {
		tv_query_phone_address = (TextView) findViewById(R.id.tv_back_SMS);
		tv_query_phone_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 开启短信备份
				showSmsBackUpDialog();
			}
		});

	}

	/**
	 * 弹出备份短信
	 */
	protected void showSmsBackUpDialog() {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("正在备份短信...");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度对话框的样式是水平
		dialog.setMax(100);
		dialog.setProgress(50);
		dialog.show();
		// 需要备份的短信字段
		/*
		 * address 发短信的人
		 *  date 是发送或接受短信事件 
		 *  read 是否读取短信 1 是已读 0是未读
		 *   type 短信类型 1 是收短信 2是发短信
		 * body 短信内容
		 * 
		 */

		new Thread() {
			public void run() {
				//要备份保存的地址
				String smsBack = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separatorChar+"SmsBack.xml";
				//使用回调用来执行某一个方法
				SmsBackUp.backup(getApplicationContext(), smsBack, new CallBack() {
					
					@Override
					public void setProgress(int max) {
						//设置当前进度条到某一个进度
						dialog.setProgress(max);
						
					}
					
					@Override
					public void setMax(int max) {
						// TODO 设置总大小
						dialog.setMax(max);
					}
				});
			};

		}.start();

	}

	/**
	 * 初始化UI
	 */
	public void initUI() {
		tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);
		tv_query_phone_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), QueryAddressActivity.class));
			}
		});
	}
}
