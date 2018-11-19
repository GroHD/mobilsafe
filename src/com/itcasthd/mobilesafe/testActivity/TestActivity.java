package com.itcasthd.mobilesafe.testActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	TextView tv = new TextView(this);
	tv.setText("这是一个测试activity");
	setContentView(tv);
	
}
}
