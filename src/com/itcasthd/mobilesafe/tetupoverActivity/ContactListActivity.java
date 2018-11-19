package com.itcasthd.mobilesafe.tetupoverActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itcasthd.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContactListActivity extends Activity {

	private ListView lv_content;
	private List<HashMap<String, String>> contactMap = new ArrayList<HashMap<String, String>>();

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// 收到消息填充数据
			lv_content.setAdapter(new MyAdapter());
		}
	};

	/**
	 * 填充数据
	 */
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return contactMap.size();
		}

		@Override
		public HashMap<String, String> getItem(int position) {
			// TODO Auto-generated method stub
			return contactMap.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView != null) {
				view = convertView;
			} else {
				view = View.inflate(ContactListActivity.this, R.layout.listview_contact_item, null);
			}
			TextView tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
			TextView tv_contact_phone = (TextView) view.findViewById(R.id.tv_contact_phone);
			tv_contact_name.setText(getItem(position).get("userName"));
			tv_contact_phone.setText(getItem(position).get("userPhone"));
			return view;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		lv_content = (ListView) findViewById(R.id.lv_content);
		initData();
		// 当点击某一个item之后运行这个方法
		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String, String> contact = contactMap.get(position);

				String userName = contact.get("userName");
				String userPhone = contact.get("userPhone");
				Log.i("click", userName + ":" + userPhone);
				Intent data = new Intent();
				data.putExtra("userPhoe", userPhone);
				setResult(10, data);
				// 关闭窗体发送返回数据
				finish();
			}
		});
	}

	/**
	 * 通过内容提供者获取系统联系人数据
	 */
	private void initData() {
		// TODO 读取系统联系人
		// 1拿到内容解析器
		// 2拿到url地址
		/**
		 * 三张表: raw_contacts 联系人的表 contact_id 联系人唯一性id data 用户信息表 :raw_contact_id
		 * 作为外键和raw_contacts中contact_id做关联，从data1字段中拿到电话号码和名字
		 * mimetpe_id字段包含当前行data1对应的数据类型 mimetype 类型表，获取对应的mimetype字段的类型
		 */
		// 防止数据过多加载数据的时候页面假死,所以把数据放入页面中

		new Thread() {

			public void run() {
				try {
					contactMap.clear();
					Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
					Cursor cursor = getContentResolver().query(uri, new String[] { "contact_id" }, null, null, null);

					while (cursor.moveToNext()) {

						String contant_id = cursor.getString(0);
						// 因为如果删除联系人的话，在contant_id是null 就会报错
						if (contant_id != null) {
							// Toast.makeText(getApplicationContext(), "contant_id:"+contant_id, 1).show();
							Uri Datauri = Uri.parse("content://com.android.contacts/data");
							Cursor query = getContentResolver().query(Datauri, new String[] { "data1", "mimetype" },
									"raw_contact_id=?", new String[] { contant_id }, null);
							HashMap<String, String> map = new HashMap<String, String>();
							while (query.moveToNext()) {

								// vnd.android.cursor.item/name
								// vnd.android.cursor.item/phone_v2
								String data1 = query.getString(0);
								String data2 = query.getString(1);

								if (!TextUtils.isEmpty(data1)) {
									if ("vnd.android.cursor.item/name".equals(data2)) {
										map.put("userName", data1);
									} else if ("vnd.android.cursor.item/phone_v2".equals(data2)) {
										map.put("userPhone", data1);
									}

								}

							}
							query.close();
							if (map.size() > 0) {
								contactMap.add(map);
							}
						}

					}
					cursor.close();
					// 发送消息告诉可以更新数据
					handler.sendEmptyMessage(0);
				} catch (Exception ex) {
					ex.printStackTrace();
					// Toast.makeText(ContactListActivity.this, "error:" + ex.getMessage(),
					// 1).show();
				}
			};
		}.start();

	}
}
