package com.itcasthd.mobilesafe.tetupoverActivity;

import com.itcasthd.mobilesafe.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SetUp1Activity extends BaseSetupActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	
	}
	
	//下面实现的是BaseSetupActivity里的方法

	//抽象方法,下一页
	@Override
	public void Move_Next() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(),SetUp2Activity.class);
		startActivity(intent);
		finish();
		//开启平移动画
		overridePendingTransition(R.anim.next_to_in, R.anim.next_to_out);
	}

	@Override
	public void Move_Pre() {
		// TODO Auto-generated method stub
		
	}


	

}
