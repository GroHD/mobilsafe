package com.itcasthd.mobilesafe.process;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.IvParameterSpec;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.db.dao.domin.ProcessInfo;
import com.itcasthd.mobilesafe.engine.AppProcessInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProcessManagerActivity extends Activity {

	private TextView mTv_processNum;
	private TextView mTv_process;
	private ListView mTv_processList;
	private Button mBt_all;
	private Button mBt_reverse;
	private Button mBt_clear;
	private Button mBt_setting;
	private List<ProcessInfo> mProcessInfoList;
	private List<ProcessInfo> mSystemProcessInfo;
	private List<ProcessInfo> mUserProcessInfo;
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			MyAdapter myAdapter = new MyAdapter();
			mTv_processList.setAdapter(myAdapter);
		}
	};
	private TextView mTv_des;
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mProcessInfoList.size()+2;
		}

		@Override
		public int getViewTypeCount() {
			// TODO 获取条目类型的总数,两种
			return super.getViewTypeCount()+1;
		}
		@Override
		public int getItemViewType(int position) {
			if(position == 0 || position == mUserProcessInfo.size()+1) {
				return 0;
			}
			else {
				return 1;
			}
			
		}
		@Override
		public Object getItem(int position) {
			if(position == 0 || position == mUserProcessInfo.size()+1) {
				return null;
			}
			else {
				if(position < mUserProcessInfo.size()+1) {
					return mUserProcessInfo.get(position-1);
				}
				else {
					//-2是因为有两个提示框
					return mSystemProcessInfo.get(position-mUserProcessInfo.size()-2);
				}
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			if(type == 0) {
				ViewHoledBar vileHoledBar = null;
				if(convertView == null) {
					convertView = View.inflate(getApplicationContext(), R.layout.listview_app_item_text, null);
					vileHoledBar = new ViewHoledBar();
					vileHoledBar.tv_showBar = (TextView)convertView.findViewById(R.id.tv_showBar);
					convertView.setTag(vileHoledBar);
				}
				vileHoledBar =(ViewHoledBar) convertView.getTag();
				if(position == 0) {
					vileHoledBar.tv_showBar.setText("用户应用("+mUserProcessInfo.size()+")");
				}
				else if(position == mUserProcessInfo.size()+1) {
					vileHoledBar.tv_showBar.setText("系统应用("+mSystemProcessInfo.size()+")");
				}
			}
			else {
				//显示正常的item
				 ViewHoled viewHoled = null; 
				if(convertView == null) {
					viewHoled = new ViewHoled();
					convertView = View.inflate(getApplicationContext(), R.layout.listview_process_item, null);
					viewHoled.iv_icom =(ImageView) convertView.findViewById(R.id.iv_icon);
					viewHoled.tv_name =(TextView) convertView.findViewById(R.id.tv_appName);
					viewHoled.tv_package =(TextView) convertView.findViewById(R.id.tv_apppackage);
					viewHoled.cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
					convertView.setTag(viewHoled);
				}
				viewHoled =(ViewHoled) convertView.getTag();
				ProcessInfo info = (ProcessInfo) getItem(position);
				viewHoled.iv_icom.setImageDrawable(info.getIcon());
				viewHoled.tv_name.setText(info.getName());
				viewHoled.tv_package.setText(info.getPackageName());
				
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					
						
					}
				});
			}
			
			return convertView;
		}
		
	}
	
	
	/**
	 * ListView数据
	 * @author Administrator
	 *
	 */
	class ViewHoled{
		public ImageView iv_icom;
		public TextView tv_name;
		public TextView tv_package;
		public CheckBox cb_select; 
	}
	/**
	 * 显示滚动条
	 * @author Administrator
	 *
	 */
	class ViewHoledBar{
		public TextView tv_showBar;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process);

		initUI();
		initProcessCount();
		initMem();
		initProcessList();
		
	}

	/**
	 * 显示进程列表
	 */
	private void initProcessList() {
		new Thread() {
			public void run() {
				mProcessInfoList = AppProcessInfoProvider.getProcessInfoList(getApplicationContext());
				mSystemProcessInfo = new ArrayList<ProcessInfo>();
				mUserProcessInfo = new ArrayList<ProcessInfo>();
				for(ProcessInfo process:mProcessInfoList) {
					if(process.isSystem()) {
						mSystemProcessInfo.add(process);
					}
					else {
						mUserProcessInfo.add(process);
					}
				}
				
				handler.sendEmptyMessage(0);
				
			};
			
		}.start();
	}

	/**
	 * 显示剩余内存数和内存总数
	 */
	private void initMem() {
		//剩余内存
		long availSpace = AppProcessInfoProvider.getAvailSpace(getApplicationContext());
		//内存总大小
		long totalSpace = AppProcessInfoProvider.getTotalSpace(getApplicationContext());
		String vavilSpaceMem = Formatter.formatShortFileSize(getApplicationContext(), availSpace);
		String totalSpaceMen = Formatter.formatShortFileSize(getApplicationContext(), totalSpace);
		mTv_process.setText("剩余/总共内存："+vavilSpaceMem+"/"+totalSpaceMen);
	}

	/**
	 * 初始化线程数
	 */
	private void initProcessCount() {
		int appProcessCount = AppProcessInfoProvider.getAppProcessCount(getApplicationContext());
		mTv_processNum.setText("线程总："+String.valueOf(appProcessCount));
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		mTv_processNum = (TextView) findViewById(R.id.tv_processNum);
		mTv_process = (TextView) findViewById(R.id.tv_processMem);
		mTv_processList = (ListView) findViewById(R.id.tv_processList);
		mBt_all = (Button) findViewById(R.id.bt_all);
		mBt_reverse = (Button) findViewById(R.id.bt_reverse);
		mBt_clear = (Button)findViewById(R.id.bt_clear);
		mBt_setting = (Button)findViewById(R.id.bt_setting);
		mTv_des = (TextView)findViewById(R.id.tv_des);
		mTv_processList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(mProcessInfoList!=null) {
					if(firstVisibleItem >mUserProcessInfo.size()) {
						mTv_des.setText("系统应用("+mSystemProcessInfo.size()+")");
					}
					else {
						mTv_des.setText("用户应用("+mUserProcessInfo.size()+")");
					}
				}
				
			}
		});
	}
}
