package com.itcasthd.mobilesafe.settingActivity;

import com.itcasthd.mobilesafe.R;
import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.SpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ToastLocation extends Activity {

	private WindowManager wm;
	private int mScreenWidth;
	private int mScreenHeight;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toast_location);
		initUi();
	}

	private void initUi() {
		// 可拖拽的按钮
		final Button btn_drag = (Button) findViewById(R.id.btn_drag);
		//提示位置
		final Button btn_show_top = (Button) findViewById(R.id.btn_show_top);
		final Button btn_show_bottom = (Button) findViewById(R.id.btn_show_bottom);
		//获取窗体管理器
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		mScreenWidth = wm.getDefaultDisplay().getWidth();
		mScreenHeight = wm.getDefaultDisplay().getHeight();
		// 获取坐标
		int x = SpUtils.getInt(getApplicationContext(), ContentValue.LOCATION_X, 0);
		int y = SpUtils.getInt(getApplicationContext(), ContentValue.LOCATION_y, 0);
		// 指定宽高都为WRAP_CONTENT
		LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// 将左上角的坐标作用在btn_drag 对应规则上
		layoutParams.leftMargin = x;
		layoutParams.topMargin = y;
		if (y < mScreenHeight / 2) {
			btn_show_top.setVisibility(View.INVISIBLE);
			btn_show_bottom.setVisibility(View.VISIBLE);
		} else {
			btn_show_top.setVisibility(View.VISIBLE);
			btn_show_bottom.setVisibility(View.INVISIBLE);
		}
		// 设置作用到 btn_drag
		btn_drag.setLayoutParams(layoutParams);

		//双击恢复默认值
			btn_drag.setOnClickListener(new OnClickListener() {
				private long startTime =0;
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(startTime!=0) {
						long endTime = System.currentTimeMillis();
						if((endTime-startTime)<500) {
							int btn_left = btn_drag.getWidth();
							int btn_top = btn_drag.getHeight();
							int btn_right = btn_left/2 + (int) mScreenWidth/2;
							int btn_bottom = btn_top/2 + (int) mScreenHeight/2;
							btn_left = mScreenWidth/2-btn_left/2;
							btn_top = mScreenHeight/2-btn_top/2;
							btn_drag.layout(btn_left,btn_top, btn_right, btn_bottom);
							
							
							int y = btn_drag.getTop();
							
							int x = btn_drag.getLeft();
							
							if (y < mScreenHeight / 2) {
								btn_show_top.setVisibility(View.INVISIBLE);
								btn_show_bottom.setVisibility(View.VISIBLE);
							} else {
								btn_show_top.setVisibility(View.VISIBLE);
								btn_show_bottom.setVisibility(View.INVISIBLE);
							}
							SpUtils.putInt(getApplicationContext(), ContentValue.LOCATION_X, x);
							SpUtils.putInt(getApplicationContext(), ContentValue.LOCATION_y, y);
						
						}
					}
					
					startTime = System.currentTimeMillis();
				}
			});
	
		
		// 监听某一个控件拖拽过程(按下 移动 抬起)
		btn_drag.setOnTouchListener(new OnTouchListener() {

			private float startX;
			private float startY;

			private float moveX;
			private float moveY;
			private int endX;
			private int endY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO 处理控件移动的时间
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 按下
					// event.getRawX()和event.getRawY();//距离0,0坐标点的距离

					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					// 移动
					moveX = event.getX();
					moveY = event.getY();
					// 拿到移动的距离
					float disX = moveX - startX;
					float disY = moveY - startY;
					// 设置当前控件所在屏幕的位置
					int btn_left = btn_drag.getLeft();
					int btn_top = btn_drag.getTop();
					btn_left = btn_left + (int) disX;
					btn_top = btn_top + (int) disY;
					int btn_right = btn_drag.getRight() + (int) disX;
					int btn_bottom = btn_drag.getBottom() + (int) disY;

					// 判断错误,防止把btn按钮拖拽出手机屏幕
					if (btn_left < 0) {
						break;
					}
					if (btn_top < 0) {
						break;
					}
					if (btn_right > mScreenWidth) {
						break;
					}
					// mScreenHeight-22 就是地边缘显示的最大值
					if (btn_bottom > (mScreenHeight - 22)) {
						break;
					}
					endX = btn_left;
					endY = btn_top;

					if (endY < mScreenHeight / 2) {
						btn_show_top.setVisibility(View.INVISIBLE);
						btn_show_bottom.setVisibility(View.VISIBLE);
					} else {
						btn_show_top.setVisibility(View.VISIBLE);
						btn_show_bottom.setVisibility(View.INVISIBLE);
					}

					// 移动控件
					btn_drag.layout(endX, endY, btn_right, btn_bottom);
				
					break;
				case MotionEvent.ACTION_UP:
					// 拿起
					// 存储移动的位置
					SpUtils.putInt(getApplicationContext(), ContentValue.LOCATION_X, endX);
					SpUtils.putInt(getApplicationContext(), ContentValue.LOCATION_y, endY);
					break;

				}
				// 返回false 不响应事件,button false 则会响应按钮的click 事件
				return false;
			}

		});

		
	}
}
