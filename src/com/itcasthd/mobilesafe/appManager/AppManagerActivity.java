package com.itcasthd.mobilesafe.appManager;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.PSource;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.db.dao.domin.AppInfo;
import com.itcasthd.mobilesafe.engine.AppInfoProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/*
 *软件管理 
 * */
public class AppManagerActivity extends Activity implements OnClickListener {

	private TextView mTv_memory;
	private TextView mTv_sd_memory;
	private ListView mLv_appList;
	private TextView mTv_des;

	private List<AppInfo> mAppInfoList;
	private List<AppInfo> mCusomInfo;
	private List<AppInfo> mSystemInfo;
	private AppInfo myAppInfo;

	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			mLv_appList.setAdapter(new MyApdate());
			mTv_des.setText("用户应用(" + mCusomInfo.size() + ")");
		}

	};
	private PopupWindow pw;

	class MyApdate extends BaseAdapter {

		@Override
		public int getViewTypeCount() {
			// TODO 获取数据适配器中条目类型的总数,修改为两种
			return super.getViewTypeCount() + 1;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO 指定索引指向的条目类型
			if (position == 0 || mCusomInfo.size() + 1 == position) {
				// 返回0，代表纯文本的状态码
				return 0;
			} else {
				// 返回1，代表图片+文本条目状态码
				return 1;
			}
		}

		@Override
		public int getCount() {
			// TODO 拿到系统应用和用户应用的大小,+2是listview中添加两个描述条目
			return mCusomInfo.size() + mSystemInfo.size() + 2;
		}

		// 修改，在获取数据的时候屏蔽掉第一个和 mCusomInfo.size() + 1 个数据
		@Override
		public Object getItem(int position) {
			if (position == 0 || position == mCusomInfo.size() + 1) {
				return null;
			} else {
				// TODO 拿到应用的详细信息
				if (position < mCusomInfo.size() + 1) {
					// 如果当前条目的index小于用户应用的大小则取用户应用
					return mCusomInfo.get(position - 1);
				} else {
					// 如果要取出的应用集合的大小，大于用户应用集合那么就取系统集合
					return mSystemInfo.get(position - mCusomInfo.size() - 2);
				}
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);

			if (type == 0) {
				// 展示灰色纯文本条目
				viewHoledBar viewHoledBar = null;
				if (convertView == null) {
					// listview_app_item_text 要展示的纯文本的类型
					convertView = View.inflate(getApplicationContext(), R.layout.listview_app_item_text, null);
					viewHoledBar = new viewHoledBar();
					viewHoledBar.tv_showBar = (TextView) convertView.findViewById(R.id.tv_showBar);
					convertView.setTag(viewHoledBar);
				}
				viewHoledBar = (viewHoledBar) convertView.getTag();
				if (position == 0) {
					viewHoledBar.tv_showBar.setText("用户应用(" + mCusomInfo.size() + ")");
				} else {
					viewHoledBar.tv_showBar.setText("系统应用(" + mSystemInfo.size() + ")");
				}

			} else {
				// 展示正常数据
				VieHoled vieHoled = null;
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(), R.layout.listview_app_item, null);
					vieHoled = new VieHoled();
					vieHoled.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
					vieHoled.tv_appName = (TextView) convertView.findViewById(R.id.tv_appName);
					vieHoled.tv_appPack = (TextView) convertView.findViewById(R.id.tv_appPack);
					convertView.setTag(vieHoled);
				}
				AppInfo appInfo = (AppInfo) getItem(position);
				vieHoled = (VieHoled) convertView.getTag();
				vieHoled.iv_icon.setImageDrawable(appInfo.getIcon());
				vieHoled.tv_appName.setText(appInfo.getName());
				vieHoled.tv_appPack.setText(appInfo.getPackageName());
			}

			return convertView;

		}

	}

	static class VieHoled {
		ImageView iv_icon;
		TextView tv_appName;
		TextView tv_appPack;
	}

	static class viewHoledBar {
		TextView tv_showBar;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 手机管理器
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		initUI();
		iniTitle();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		// TODO 初始化UI
		mTv_memory = (TextView) findViewById(R.id.tv_memory);
		mTv_sd_memory = (TextView) findViewById(R.id.tv_sd_memory);
		mLv_appList = (ListView) findViewById(R.id.lv_appList);
		mTv_des = (TextView) findViewById(R.id.tv_des);
		
		// ListView滚动事件
		mLv_appList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO 滚动过程中调用该方法
				// AbsListView 是listview对象
				// firstVisibleItem 第一个可见条目
				// visibleItemCount 当前一个屏幕可见数量
				// totalItemCount 总条目数
				if (mCusomInfo != null) {
					if (firstVisibleItem > mCusomInfo.size()) {

						// 滚动到了系统条目
						mTv_des.setText("系统应用(" + mSystemInfo.size() + ")");
					} else {
						// 滚动到了用户条目
						mTv_des.setText("用户应用(" + mCusomInfo.size() + ")");
					}
				}
			}
		});
		// ListView点击事件
		mLv_appList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO 点击按钮
				if (position < mCusomInfo.size() + 1) {
					myAppInfo = mCusomInfo.get(position - 1);

				} else {
					myAppInfo = mSystemInfo.get(position - mCusomInfo.size() - 2);
				}

				showPopupWindow(view);

			}
		});
	}

	/**
	 * 弹出框
	 */
	private void showPopupWindow(View view) {
		/*
		 * 动画，由透明到不透明
		 */
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(500);
		alpha.setFillAfter(true);
		/*
		 * 缩放动画 1.第一个和第三个参数是：从什么位置开始 2.第二个和第四个是到什么位置结束 3.第五个和第七个参数是执行多大
		 * 4.第六个和第八个参数是从那个位置开始 0.5F代表一半
		 * 
		 */
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5F,
				Animation.RELATIVE_TO_SELF, 0.5F);
		scaleAnimation.setDuration(500);
		scaleAnimation.setFillAfter(true);
		// 参数是否共享一个数学函数，就是是否动画集合执行相同的方法
		AnimationSet animationSet = new AnimationSet(true);
		// 把动画添加到集合中
		animationSet.addAnimation(alpha);
		animationSet.addAnimation(scaleAnimation);

		View popupview = View.inflate(getApplicationContext(), R.layout.popupwindow_layout, null);
		// 卸载
		TextView tv_uninstall = (TextView) popupview.findViewById(R.id.tv_uninstall);
		// 启动
		TextView tv_start = (TextView) popupview.findViewById(R.id.tv_start);
		// 分享
		TextView tv_share = (TextView) popupview.findViewById(R.id.tv_share);
		// 设置点击的方法
		tv_uninstall.setOnClickListener(this);
		tv_start.setOnClickListener(this);
		tv_share.setOnClickListener(this);

		pw = new PopupWindow(popupview, LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, true);
		// 设置一个透明背景,设备的背景色是透明背景
		pw.setBackgroundDrawable(new ColorDrawable());
		// 指定窗体位置
		pw.showAsDropDown(view, 35, -view.getHeight());
		// 执行动画
		popupview.setAnimation(animationSet);
	}

	/**
	 * 获取磁盘和sd卡可用大小
	 */
	private void iniTitle() {
		// 获取磁盘可用大小
		String path = Environment.getDataDirectory().getAbsolutePath();
		// 获取sd卡可用大小
		String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();

		// 获取上面路径下的文件夹可用大小
		long cpSize = getAvailSpace(path);
		long sdSize = getAvailSpace(sdPath);
		String cpmemy = Formatter.formatFileSize(getApplicationContext(), cpSize);
		String sdmemy = Formatter.formatFileSize(getApplicationContext(), sdSize);
		mTv_memory.setText("磁盘可用：" + cpmemy);
		mTv_sd_memory.setText("SD卡可用：" + sdmemy);
	}

	/**
	 * 根据路径获取磁盘大小,因为磁盘可能太大如果使用int代表则只能计算2G，所以需要long
	 * 
	 * @param path
	 */
	private long getAvailSpace(String path) {
		// 获取磁盘大小的类
		StatFs stf = new StatFs(path);
		// 获取可用区块的个数
		int count = stf.getAvailableBlocks();
		// 获取区块的大小,这个是没个区块占多少个字节
		int size = stf.getBlockSize();

		return count * size;

	}
	
	@Override
	protected void onResume() {
		//重新获取数据,因为在第一个创建窗体的时候会获取焦点，就会执行这个方法，当再次获取到焦点的时候又会执行这个方法,所以在这个实现方法里写一次代码即可
		new Thread() {
			public void run() {
				mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
				mSystemInfo = new ArrayList<AppInfo>();
				mCusomInfo = new ArrayList<AppInfo>();
				for (AppInfo appInfo : mAppInfoList) {
					if (appInfo.isSystem) {
						// 系统应用
						mSystemInfo.add(appInfo);
					} else {
						// 用户应用
						mCusomInfo.add(appInfo);
					}
				}
				mHandler.sendEmptyMessage(0);
			};

		}.start();
		
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_uninstall:
			// 卸载
			if (myAppInfo.isSystem) {
				Toast.makeText(getApplicationContext(), "系统应用不可卸载", 0).show();
			} else {
				// 使用意图卸载即可
				Intent uninstall = new Intent("android.intent.action.DELETE");
				uninstall.addCategory("android.intent.category.DEFAULT");
				uninstall.setData(Uri.parse("package:" + myAppInfo.getPackageName()));
				startActivity(uninstall);
			}

			break;
		case R.id.tv_start:
			// 启动应用
			// 通过桌面去启动指定包名的应用
			PackageManager pm = getPackageManager();
			Intent appIntent = pm.getLaunchIntentForPackage(myAppInfo.getPackageName());
			//判断是否为空
			if (appIntent != null) {
				startActivity(appIntent);
			}
			else {
				Toast.makeText(getApplicationContext(), "应用启动失败，请稍后再试", 0).show();
				
			}
			
			break;
		case R.id.tv_share:
			// 分享
			Intent sms = new Intent(Intent.ACTION_SEND);
			sms.putExtra(Intent.EXTRA_TEXT, "给你分享一个好玩的应用哦："+myAppInfo.getPackageName());
			sms.setType("text/plain");
			startActivity(sms);
			
			break;
		}
		//隐藏列表
		if(pw!=null) {
			pw.dismiss();
		}

	}
}
