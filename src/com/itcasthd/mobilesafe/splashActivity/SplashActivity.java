package com.itcasthd.mobilesafe.splashActivity;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.Utils.StreamUtils;
import com.itcasthd.mobilesafe.mainActivity.MainActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	protected static final int UPDATE_CERSION = 0;// 更新
	protected static final int ENTER_HOME = 1;// 正确进入主界面
	protected static final int CONN_ERROR = -1;// 链接或解析异常
	private TextView tv;
	private int mLocalVersionCode;
	// handler
	Handler mHandler = new Handler() {
		/**
		 * 处理消息机制 用来处理从线程中传递回来的数据
		 */
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_CERSION:
				Bundle data = msg.getData();
				String versionName = data.getString("versionName");
				String versionDes = data.getString("versionDes");
				String downloadUrl = data.getString("downloadUrl");
				shouUpdateDialog(versionName, versionDes, downloadUrl);
				// 更新
				break;
			case ENTER_HOME:
				// 进入主页
				enterHome();
				break;
			case CONN_ERROR:
				// 出现错误
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 1).show();
				// 异常也进入home页面
				enterHome();
				break;
			}

		};

	};
	private RelativeLayout rl_root;

	/**
	 * 弹出对话框,提示用户更新
	 */
	protected void shouUpdateDialog(String versionName, String versionDes, final String loadUrl) {
		Builder alert = new AlertDialog.Builder(this);
		alert.setIcon(R.drawable.update);
		alert.setTitle("程序有最新版本");
		alert.setMessage("本次更新如下\r\n   更新版本:" + versionName + "\r\n更新内容:\r\n   " + versionDes);
		alert.setPositiveButton("更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 下载apk

				System.out.println("更新apk");
				downloadApk(loadUrl);

			}
		});
		// 点击取消按钮
		alert.setNegativeButton("取消更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.out.println("取消更新apk");
				// 取消更新后跳转主界面
				enterHome();
			}
		});
		// 当点击手机上的后退之后进入主界面,或者没点击取消也没点击更新,则走这个处理方法
		alert.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				enterHome();
				dialog.dismiss();
			}
		});
		alert.show();
	}

	/**
	 * 使用XUtils下载apk下载apk,这个需要网络和sd卡的权限
	 * 
	 * @param loadUrl
	 */
	private void downloadApk(String loadUrl) {
		// sd卡可用
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			System.out.println(loadUrl);
			int index = loadUrl.lastIndexOf("/");// 拿到最后一个/的位置
			String apkName = loadUrl.substring(index + 1);
			// 拼接文件名 File.separator 是系统分隔符
			final String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
					+ apkName;
			System.out.println(sdPath);
			// 发送请求,获取下载apk,并且放到指定的位置
			HttpUtils utils = new HttpUtils();
			/*
			 * 第一个参数下载的地址 第二个参数保存到那个目录下 第三个参数是下载数据执行的回调函数
			 */
			utils.download(loadUrl, sdPath, new RequestCallBack<File>() {

				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					// TODO Auto-generated method stub
					// 下载成功
					// 下载成功(下载过后存放在sd卡中的apk)
					File file = responseInfo.result;
					Log.i("log", "下载成功");
					// 下载成功 提示用户安装
					installApk(file);
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					// 下载失败
					Log.i("log", "下载失败");
					// 下载失败进入主界面
					enterHome();
				}

				// 刚刚开始下载的方法
				@Override
				public void onStart() {
					// 刚开始下载
					Log.i("log", "刚开始下载");
				}

				/*
				 * 第一个参数是 下载的总大小 第二个参数是 当前的下载位置 第三个参数是 是否正在下载
				 */
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					// TODO Auto-generated method stub
					// 下载中的
					Log.i("log", "下载中  total:" + total + " current:" + current + " ");

				}
			});
			// 发送请求
			// utils.send(method, url, callBack)

		} else {
			// sd卡不可用
			Toast.makeText(this, "sd卡不可用,请稍后再试!", 1).show();
			// 下载失败进入主界面
			enterHome();
		}
	}

	/**
	 * 安装apk
	 * 
	 * @param apkPath
	 *            apk 文件路径
	 */
	protected void installApk(File file) {
		if (file.exists()) {
			// 文件不存在
			// 调用隐士意图安装apk
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			// 文件作为数据源
			// intent.setData(Uri.fromFile(file));
			// 设置安装的类型
			// intent.setType("application/vnd.android.package-archive");
			// 下面的这个相当于上面的两个
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			// 开启界面
			// startActivity(intent);
			// 开启安装界面的时候返回
			startActivityForResult(intent, 0);
		}
		// 安装应用
	}

	// 开启安装界面结束后，返回结果调用的方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		enterHome();
	}

	/**
	 * 跳转主界面
	 */
	protected void enterHome() {

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		// 在开启一个新的界面后将当前界面关闭,防止按后退键进入该界面
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置没有大标题,这种不推荐,推荐使用style的配置
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		// 初始化uid
		initUi();
		// 初始化数据
		initData();
		// 创建桌面快捷键
		initShortcut();

		// 初始化动画类,淡入
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		// 设置执行时长
		alphaAnimation.setDuration(1000);
		// rl_root是根节点的id
		rl_root.setAnimation(alphaAnimation);

	}

	/*
	 * 创建快捷键
	 */
	private void initShortcut() {
		// 是否已创建快捷键方式
		boolean setingShortCut = SpUtils.getBoolean(getApplicationContext(), ContentValue.SETINGSHORTCUT, false);
		if (setingShortCut) {
			return;
		}
		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 维护图标
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		// 维护名称
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机卫士");
		// 点击快捷键跳转到activity
		// 维护要开启的意图
		Intent startIntent = new Intent("android.intent.action.HOME");
		startIntent.addCategory("android.intent.category.DEFAULT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, startIntent);
		// 发送广播
		sendBroadcast(intent);
		// 设置已创建桌面快捷键
		SpUtils.putBoolean(getApplicationContext(), ContentValue.SETINGSHORTCUT, true);

	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		// 1.获取应用版本名称
		String versionName = getVersionName();
		tv.setText("当前版本：" + versionName);
		// 检测(本地版本号和服务器版本号对比) 是否有更新,如果有更新则提示用户下载
		// 本地的
		mLocalVersionCode = gerVersionCode();
		// 获取服务器版本号
		/*
		 * json的内容: 1.最新版本的版本名称 2.更新的版本描述信息 3.服务端的版本号
		 * 
		 */
		// 判断是否自动更新
		boolean autoUpdate = SpUtils.getBoolean(this, ContentValue.AUTOUPDATE, false);
		if (autoUpdate) {
			getServiceVersionCode();
		} else {
			// 不自动更新 进入主界面
			// 4000秒以后进入界面
			mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
			// enterHome();
		}

	}

	/**
	 * 获取服务器的版本号,并且判断是否需要更新
	 * 
	 * @return 服务器的版本号
	 */
	private int getServiceVersionCode() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {

				Message msg = Message.obtain();
				long starTime = System.currentTimeMillis();
				try {

					URL url = new URL("http://192.168.0.5/update.json");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					// 请求方式
					conn.setRequestMethod("GET");
					// 设置请求超时
					conn.setConnectTimeout(5000);
					// 设置读取数据超时
					conn.setReadTimeout(3000);

					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream in = conn.getInputStream();
						// 自己写的utils工具,用来把流转换为字符串
						String jsonStr = StreamUtils.Stream2String(in);
						// 解析json字符串
						JSONObject jsonObj = new JSONObject(jsonStr);
						String versionName = jsonObj.get("versionName").toString();
						String versionDes = jsonObj.get("versionDes").toString();
						String versionCode = jsonObj.get("versionCode").toString();
						String downloadUrl = jsonObj.get("downloadUrl").toString();
						// System.out.println("versionName:"+versionName+" versionDes:"+versionDes);
						// System.out.println("versionCode:"+versionCode+" downloadUrl:"+downloadUrl);
						// 对比版本号 如果服务器的版本号大于本地的版本号,则提示用户更新数据
						if (mLocalVersionCode < Integer.parseInt(versionCode)) {
							// 提示用户更新,消息机制
							msg.what = UPDATE_CERSION;
							Bundle bundle = new Bundle();
							bundle.putString("versionName", versionName);
							bundle.putString("versionDes", versionDes);
							bundle.putString("downloadUrl", downloadUrl);
							msg.setData(bundle);

						} else {
							// 不需要更新进入应用程序主界面
							msg.what = ENTER_HOME;
						}

					}
				} catch (Exception ex) {
					msg.what = CONN_ERROR;
					msg.obj = ex.getMessage();
				} finally {
					long sendTime = System.currentTimeMillis();
					if (sendTime - starTime < 4000) {
						SystemClock.sleep(4000 - (sendTime - starTime));
					}
					mHandler.sendMessage(msg);
				}
			}
		}.start();
		return 0;
	}

	/**
	 * 拿到本地的版本号
	 * 
	 * @return 本地的版本号
	 */
	private int gerVersionCode() {
		// TODO Auto-generated method stub
		// 包管理者packageManager
		PackageManager pm = getPackageManager();
		// 从包的管理对象中,获取报名的基本信息(版本名称，版本号)，传0就是获取基本信息
		try {
			PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
			// 获取对应的版本名称
			return packageInfo.versionCode;// 版本名称
			// 检测是否有更新数据,如果有则提示用户更新数据

			// packageInfo.versionCode;//版本号
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取版本名称,版本文件在清单文件中
	 * 
	 * @return 返回版本号,错误返回null
	 */
	private String getVersionName() {
		// 包管理者packageManager
		PackageManager pm = getPackageManager();
		// 从包的管理对象中,获取报名的基本信息(版本名称，版本号)，传0就是获取基本信息
		try {
			PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
			// 获取对应的版本名称
			return packageInfo.versionName;// 版本名称

			// packageInfo.versionCode;//版本号
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	/**
	 * 初始化Ui方法
	 */
	public void initUi() {
		tv = (TextView) findViewById(R.id.tv_version_name);
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);

	}

}
