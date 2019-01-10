package com.itcasthd.mobilesafe.atoolActivity;

import java.util.List;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.db.dao.domin.CommonNumInfo;
import com.itcasthd.mobilesafe.engine.CommonNumDao;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class CommonNumQueryAcivity extends Activity {

	private ExpandableListView mElv_commonNumQuery;
	private List<CommonNumInfo> mGroup;
	private Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			//绑定数据
			mElv_commonNumQuery.setAdapter(new MyAdapter());
		};

	};

	class MyAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			// TODO 获取分组的数量
			return mGroup.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO 获取子节点的数量
			return CommonNumDao.getNumInfo(mGroup.get(groupPosition).getnumId()).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO 那个分组的元素
			return mGroup.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO 拿到子节点的数量
			return CommonNumDao.getNumInfo(mGroup.get(groupPosition).getnumId()).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO 那个分组id
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO 那个子节点的id
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO 无需修改
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			// TODO 主节点
			TextView textView = new TextView(getApplicationContext());
			CommonNumInfo info = (CommonNumInfo) getGroup(groupPosition);
			textView.setText("		"+info.get_name());
			textView.setTextColor(Color.RED);// 字体颜色
			textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);// 像素大小 dip = dp dpi == ppi(像素密度) 像素密度越大屏幕越好
			return textView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.activity_common_query_child_item, null);
			}
			CommonNumInfo info = (CommonNumInfo) getChild(groupPosition, childPosition);
			String name = info.get_name();
			String number = info.getnumId();
			TextView tv_common_query_name = (TextView) convertView.findViewById(R.id.tv_common_query_name);
			TextView tv_common_query_number = (TextView) convertView.findViewById(R.id.tv_common_query_number);
			tv_common_query_name.setText(name);
			tv_common_query_number.setText(number);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO 子节点是否可以被选中
			return true;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_num);
		initUi();
		// 初始化数据
		initData();
	}

	// 初始化数据
	private void initData() {
		mGroup = CommonNumDao.getGroup();
		handler.sendEmptyMessage(0);
		//点击子节点拨打电话
		mElv_commonNumQuery.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				CommonNumInfo commonNumInfo = CommonNumDao.getNumInfo(mGroup.get(groupPosition).getnumId()).get(childPosition);
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+commonNumInfo.getnumId()));
				startActivity(intent);
				return false;
			}
		});
	}

	/**
	 * 初始化UI
	 */
	private void initUi() {
		mElv_commonNumQuery = (ExpandableListView) findViewById(R.id.elv_commonNumQuery);

	}
}
