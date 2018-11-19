package com.itcasthd.mobilesafe.mainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.MD5Utils;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.appManager.AppManagerActivity;
import com.itcasthd.mobilesafe.atoolActivity.AToolActivity;
import com.itcasthd.mobilesafe.blackNumberActivity.BlackNumberActivity;
import com.itcasthd.mobilesafe.process.ProcessManagerActivity;
import com.itcasthd.mobilesafe.settingActivity.SettingActivity;
import com.itcasthd.mobilesafe.testActivity.TestActivity;
import com.itcasthd.mobilesafe.tetupoverActivity.SetUpOverActivity;
import com.lidroid.xutils.cache.MD5FileNameGenerator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private GridView gv_home;
	private String[] mTitles;
	private int[] mDrawableIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 获取控件
		initUi();
		// 初始化数据
		initData();
		//初始化数据库
		initDB();
	}
	
	private void initDB() {
		//1 归属地的数据拷贝
		initAddressDB("address.db");
	}

	/**
	 * 拷贝数据库到files文件夹下
	 * @param string  数据库名称
	 */
	private void initAddressDB(String dbName) {
		//拿到当前app的files文件目录
		File filesDir = getFilesDir();
		//在files文件夹下创建同名的数据库文件
		File file = new File(filesDir,dbName);
		//判断文件是否存在
		if(!file.exists()) {
			//读取第三方资产目录下的文件 (assets)
			try {
				//
				InputStream open = getAssets().open(dbName);
				//将读取的内容写入到指定文件夹的目录中去
				FileOutputStream fos = new FileOutputStream(file);
				byte [] buffer = new byte[1024];
				int len = -1;
				while((len = open.read(buffer))!=-1) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				open.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mTitles = new String[] { "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
		mDrawableIds = new int[] { R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
				R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
				R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings };
		// 九宫格设置数据设备其
		gv_home.setAdapter(new MyAdapter());
		// 给九宫格注册点击事件
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 获取按的是那个按钮
				switch (position) {
				case 0:
					// 开启对话框
					showDialog();
					break;
				case 1:
					startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
					break;
				case 2:
					startActivity(new Intent(getApplicationContext(),AppManagerActivity.class));
					break;
				case 3:
					startActivity(new Intent(getApplicationContext(),ProcessManagerActivity.class));
					break;
				case 4:
					Toast.makeText(getApplicationContext(), "流量统计", 1).show();
					break;
				case 5:
					Toast.makeText(getApplicationContext(), "手机杀毒", 1).show();
					break;
				case 6:
					Toast.makeText(getApplicationContext(), "缓存清理", 1).show();
					break;
				case 7:
					startActivity(new Intent(getApplicationContext(),AToolActivity.class));
					break;
				case 8:
					Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
					startActivity(intent);
					break;
				}
			}

		});
	}

	/**
	 * 密码对话框
	 */
	private void showDialog() {
		// 判断本地是否存储密码
		String password = SpUtils.getString(this, ContentValue.PASSWORD, null);
		if (password == null) {
			// 第一次设置密码
			showSetPasswordDialog();
		} else {
			// 第二次进入加载密码
			showConfirmPasswordDialog();
		}

	}

	/**
	 * 进入界面设置密码
	 */
	private void showConfirmPasswordDialog() {
		// TODO Auto-generated method stub
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		// 设置对话框的样式
		final View view = View.inflate(this, R.layout.dialog_confim_password, null);
		// 让对话框显示自己定义的界面效果
	//	dialog.setView(view);
		//以下是兼容低版本的数据，在4.0以下的版本有默认的内边距
		dialog.setView(view, 0, 0, 0, 0);

		dialog.show();
		// 点击确认按钮
		Button btn_submit = (Button) view.findViewById(R.id.btn_submit);
		// 点击取消按钮
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancle);
		// 设置按钮事件
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 输入密码
				EditText et_Password = (EditText) view.findViewById(R.id.tv_password);
				String et_confim_pass = SpUtils.getString(MainActivity.this, ContentValue.PASSWORD, "");
				String con_pass = et_Password.getText().toString().trim();
				if(MD5Utils.MD5(con_pass).equals(et_confim_pass)) {
					// 开启界面
					Intent intent = new Intent(getBaseContext(), SetUpOverActivity.class);
					startActivity(intent);
					// 跳转到新的界面之后,需要隐藏对话框
					dialog.dismiss();
				}
				else {
					Toast.makeText(MainActivity.this, "密码输入错误,请重新输入!", 0).show();
					et_Password.setText("");
				}
			}
		});
		
		// 取消事件
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 隐藏界面
				dialog.dismiss();
			}
		});
	}

	/**
	 * 初始化密码
	 */
	private void showSetPasswordDialog() {
		// TODO Auto-generated method stub
		// 因为需要自己定义对话框的展示样式,所以需要调用setView方法
		// View是由自己编写的view对象xml来显示的
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		// 设置对话框的样式
		final View view = View.inflate(this, R.layout.dialog_set_password, null);
		// 让对话框显示自己定义的界面效果
		dialog.setView(view);

		dialog.show();
		// 点击确认按钮
		Button btn_submit = (Button) view.findViewById(R.id.btn_submit);
		// 点击取消按钮
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancle);
		// 设置按钮事件
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 输入密码
				EditText et_Password = (EditText) view.findViewById(R.id.tv_password);
				// 确认密码
				EditText et_repl_password = (EditText) view.findViewById(R.id.tv_repl_password);
				String pass = et_Password.getText().toString().trim();
				String conPass = et_repl_password.getText().toString().trim();
				if (!TextUtils.isEmpty(pass) && !TextUtils.isEmpty(conPass)) {
					if (conPass.equals(pass)) {
						// 保存密码
						
						// String mdPass =
						SpUtils.putString(MainActivity.this, ContentValue.PASSWORD, MD5Utils.MD5(pass));
						// 开启界面
						Intent intent = new Intent(getBaseContext(), SetUpOverActivity.class);
						startActivity(intent);
						// 跳转到新的界面之后,需要隐藏对话框
						dialog.dismiss();
					} else {
						Toast.makeText(MainActivity.this, "两次密码不一致,请重新输入!", 0).show();
						et_Password.setText("");
						et_repl_password.setText("");
					}
				} else {
					// 提示密码有为空的
					Toast.makeText(MainActivity.this, "密码不可为空!", 0).show();
				}

			}
		});
		// 取消事件
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 隐藏界面
				dialog.dismiss();
			}
		});
	}
	
	

	/**
	 * 初始化ui
	 */
	private void initUi() {
		gv_home = (GridView) findViewById(R.id.gv_home);

	}

	/**
	 * 
	 * 给九宫格填充数据
	 * 
	 * @author Administrator
	 *
	 */
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDrawableIds.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mDrawableIds[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView != null) {
				view = convertView;
			} else {
				view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
			}
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_title.setText(mTitles[position]);
			iv_icon.setBackgroundResource(mDrawableIds[position]);
			return view;
		}

	}

}
