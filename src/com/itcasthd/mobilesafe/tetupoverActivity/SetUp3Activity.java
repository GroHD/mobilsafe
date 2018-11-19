package com.itcasthd.mobilesafe.tetupoverActivity;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetUp3Activity extends BaseSetupActivity {

	private EditText et_phoneNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		initUi();
		// 判断是否添加过联系人
		String contact = SpUtils.getString(this, ContentValue.CONTACT, "");
		if (!TextUtils.isEmpty(contact)) {
			et_phoneNum.setText(contact);
		}

	}

	/**
	 * 初始化UI
	 */
	private void initUi() {
		et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
	}

	// 选择联系人
	public void btn_selectPhoneNum(View view) {
		Intent intent = new Intent(this, ContactListActivity.class);
		startActivityForResult(intent, 10);
	}

	/*
	 * 选择联系人后返回这里的数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			String phone = data.getStringExtra("userPhoe");

			// 过滤特殊字符
			phone = phone.replace("-", "").replace(" ", "").trim();
			et_phoneNum.setText(phone);
			// 保存紧急联系人
			// SpUtils.putString(this, ContentValue.CONTACT, phone);
		}
	}


	@Override
	public void Move_Next() {
		// 在点击下一页的时候保存联系人
				String pone_contact = et_phoneNum.getText().toString();
				if(TextUtils.isEmpty(pone_contact)) {
					Toast.makeText(getApplicationContext(), "全号码不可为空", 0).show();
					return ;
				}
				SpUtils.putString(this, ContentValue.CONTACT, pone_contact);
				Intent intent = new Intent(this, SetUp4Activity.class);
				startActivity(intent);
				finish();
				// 开启平移动画
				overridePendingTransition(R.anim.next_to_in, R.anim.next_to_out);
		
	}

	@Override
	public void Move_Pre() {
		Intent intent = new Intent(this, SetUp2Activity.class);
		startActivity(intent);
		finish();
		// 开启平移动画
		overridePendingTransition(R.anim.pre_to_in, R.anim.pre_to_out);
		
	}
}
