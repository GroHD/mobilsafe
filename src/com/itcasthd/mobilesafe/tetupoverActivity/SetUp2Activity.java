package com.itcasthd.mobilesafe.tetupoverActivity;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SetUp2Activity extends BaseSetupActivity {
	SettingItemView siv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		siv = (SettingItemView)findViewById(R.id.imvs_bindSim);
		String simNum = SpUtils.getString(this, ContentValue.BINDSIM, "");
		//判断是否绑定sim卡,空位没有绑定sim卡
		siv.setCheck(!TextUtils.isEmpty(simNum));
		siv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean check = siv.isCheck();
				siv.setCheck(!check);
				if(!check) {
					String simNum = GetSimNum();
					
					//设置绑定sim卡
					SpUtils.putString(getApplicationContext(), ContentValue.BINDSIM, simNum);
				}
				else {
					//删除节点
					SpUtils.Remove(getApplicationContext(),ContentValue.BINDSIM);
				}
			}
		});
	}
	
	/**
	 * 获取sim卡的序列号
	 * @return  已获取的sim卡序列号
	 */
	private String GetSimNum() {
		TelephonyManager tm  = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		//获取sim卡的序列卡号
		String simSerialNumber = tm.getSimSerialNumber();
		Toast.makeText(this, simSerialNumber, 1).show();
		return simSerialNumber;
	}
	@Override
	public void Move_Next() {
		// TODO Auto-generated method stub
		boolean check = siv.isCheck();
		if(!check) {
			Toast.makeText(this, "必须绑定sim卡", 0).show();
			return ;
		}
	
		Intent intent = new Intent(this,SetUp3Activity.class);
		startActivity(intent);
		finish();
		//开启平移动画
		overridePendingTransition(R.anim.next_to_in, R.anim.next_to_out);
		
	}
	@Override
	public void Move_Pre() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,SetUp1Activity.class);
		startActivity(intent);
		finish();
		//开启平移动画
		overridePendingTransition(R.anim.pre_to_in, R.anim.pre_to_out);
		
	}
}
