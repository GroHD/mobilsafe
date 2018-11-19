package com.itcasthd.mobilesafe.blackNumberActivity;

import java.util.List;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.db.dao.BlackNumberDao;
import com.itcasthd.mobilesafe.db.dao.domin.BlackNumberInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BlackNumberActivity extends Activity {

	private BlackNumberDao mDao;
	private List<BlackNumberInfo> mFindAll;
	private ListView mLv_phoneNumber;
	private boolean mIsLoad = false;
	private int mCount = 0;
	private MyAddadpter maddadpter = null;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.arg1 == 1) {
				ShowAddPhone(msg.obj.toString(), msg.arg2);

			} else {
				// 更新方法
				if (maddadpter == null) {
					maddadpter = new MyAddadpter();
					mLv_phoneNumber.setAdapter(maddadpter);
				} else {
					// 只是更新数据
					maddadpter.notifyDataSetChanged();
				}
			}
		};

	};

	class MyAddadpter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mFindAll.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mFindAll.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.listview_blacknumber_item, null);
				// 将findViewById封装到这里，可以减少查询的次数
				// 1.
				viewHolder = new ViewHolder();
				// 电话号码
				TextView tv_black_number = (TextView) convertView.findViewById(R.id.tv_black_number);
				viewHolder.setTv_black_number(tv_black_number);
				// 说明
				TextView tv_black_Contas = (TextView) convertView.findViewById(R.id.tv_black_Contas);
				viewHolder.setTv_black_Contas(tv_black_Contas);
				ImageButton ibttn_deleblackNum = (ImageButton) convertView.findViewById(R.id.ibttn_deleblackNum);
				viewHolder.setIbttn_deleblackNum(ibttn_deleblackNum);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			BlackNumberInfo blackNumberInfo = mFindAll.get(position);
			viewHolder.getTv_black_number().setText(blackNumberInfo.getPhone());
			// 1 是短信 2是电话 3是全部
			String modeText = blackNumberInfo.getModel().equals("1") ? "拦截短信"
					: (blackNumberInfo.getModel().equals("2") ? "拦截电话" : "全部拦截");
			viewHolder.getTv_black_Contas().setText(modeText);
			// 1.获取到电话号码
			final String number = viewHolder.getTv_black_number().getText().toString().trim();
			// 删除黑名单
			viewHolder.getIbttn_deleblackNum().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 删除黑名单
					if (mDao != null) {
						mDao.delete(number);
						initData();
					}
				}
			});
			final View clickView = convertView;
			// 修改状态
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new Thread() {
						public void run() {
							// TODO 点击获取当前点击的条目
							ViewHolder vh = (ViewHolder) clickView.getTag();
							String phone = vh.getTv_black_number().getText().toString().trim();
							int mode = mDao.findPhone(phone);
							Message msg = new Message();
							msg.arg1 = 1;
							msg.arg2 = mode;
							msg.obj = phone;
							handler.sendMessage(msg);
						};

					}.start();

				}
			});
			return convertView;
		}

	}

	/**
	 * 用来复用控件
	 */
	class ViewHolder {
		private TextView tv_black_number;
		private TextView tv_black_Contas;

		public TextView getTv_black_number() {
			return tv_black_number;
		}

		public void setTv_black_number(TextView tv_black_number) {
			this.tv_black_number = tv_black_number;
		}

		public TextView getTv_black_Contas() {
			return tv_black_Contas;
		}

		public void setTv_black_Contas(TextView tv_black_Contas) {
			this.tv_black_Contas = tv_black_Contas;
		}

		public ImageButton getIbttn_deleblackNum() {
			return ibttn_deleblackNum;
		}

		public void setIbttn_deleblackNum(ImageButton ibttn_deleblackNum) {
			this.ibttn_deleblackNum = ibttn_deleblackNum;
		}

		private ImageButton ibttn_deleblackNum;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacknumber);
		Button btn_addBlack = (Button) findViewById(R.id.btn_addBlack);
		mLv_phoneNumber = (ListView) findViewById(R.id.lv_phoneNumber);
		final ListView lv_phoneNumber = (ListView) findViewById(R.id.lv_phoneNumber);
		btn_addBlack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 添加黑名单
				ShowAddPhone("", 1);

			}
		});

		// 监听滚动状态
		lv_phoneNumber.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO 当滚动过程中的状态发生改变的时候
				// OnScrollListener.SCROLL_STATE_FLING 滚动中
				// OnScrollListener.SCROLL_STATE_IDLE 空闲状态
				// OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 用手触摸的去滚动的状态
				// lv_phoneNumber.getLastVisiblePosition() 获取最后一个可见条目的索引值

				/*
				 * 条件1 判断滚动是否为空闲 条件2 listView的可见条目的索引值是否是当前数据集合List中的最后一条 条件3
				 * 防止重复加载数据,如果是耗时的数据,则有可能重复加载数据
				 */
				// 判断拿到表里的数据总数是否是最后一条数据
				if (mCount == mFindAll.size()) {
					return;
				}
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lv_phoneNumber.getLastVisiblePosition() >= mFindAll.size() - 1 && !mIsLoad) {
					// 防止重复加载,当加载完之后设置为false
					mIsLoad = true;
					new Thread() {
						public void run() {
							mDao = BlackNumberDao.getBlackNumberDao(getApplicationContext());
							// 查询部分数据
							int pageIndex = (mFindAll.size() / 10) + 1;

							List<BlackNumberInfo> fpi = mDao.findPageIndex(pageIndex, 10);
							// 把查询出的数据添加到list中
							mFindAll.addAll(fpi);
							// 告诉handler更新
							handler.sendEmptyMessage(1);
							// 设置可以进行下次更新查询的数据
							mIsLoad = false;
						};

					}.start();

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO 滚动过程汇总调用的方法

			}
		});

		initData();
	}

	/**
	 * 获取数据
	 */
	private void initData() {
		new Thread() {
			public void run() {
				mDao = BlackNumberDao.getBlackNumberDao(getApplicationContext());
				mFindAll = mDao.findPageIndex(1, 10);
				mCount = mDao.getCount();
				handler.sendEmptyMessage(1);

			};

		}.start();

	}

	private void ShowAddPhone(String phone, int mode) {
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alert = builder.create();
		View blackNumView = View.inflate(getApplicationContext(), R.layout.dialog_add_blocaknumber, null);
		alert.setView(blackNumView);
		alert.show();
		Button btn_close = (Button) blackNumView.findViewById(R.id.btn_close);

		btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 关闭提示框
				alert.dismiss();
			}
		});
		final EditText et_blackPhone = (EditText) blackNumView.findViewById(R.id.et_blackPhone);
		if (!"".equals(phone)) {
			et_blackPhone.setText(phone);
		}
		final RadioGroup rg_group = (RadioGroup) blackNumView.findViewById(R.id.rg_group);

		if (mode == 2) {
			rg_group.check(R.id.rg_tellsms);
		} else if (mode == 3) {
			rg_group.check(R.id.rg_tellall);
		}
		Button btn_saveBloackPhone = (Button) blackNumView.findViewById(R.id.btn_saveBloackPhone);
		btn_saveBloackPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 保存电话号码
				String phone = et_blackPhone.getText().toString().trim();
				String mode = "1";
				switch (rg_group.getCheckedRadioButtonId()) {
				case R.id.rg_tellphone:
					mode = "1";
					break;
				case R.id.rg_tellsms:
					mode = "2";
					break;
				case R.id.rg_tellall:
					mode = "3";
					break;
				}
				if (!"".equals(phone)) {
					mDao.update(phone, mode);
				} else {
					mDao.insert(phone, mode);
				}
				alert.dismiss();
				initData();
			}
		});
	}
}
