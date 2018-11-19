package com.itcasthd.mobilesafe.tetupoverActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/*
 * 这个是同一处理手势移动的梳理
 * */
public abstract class BaseSetupActivity extends Activity {
	private GestureDetector gestureDetector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//创建手势识别器对象
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			//重写手势识别器中的onFling,包含按下点和抬起点在移动过程中的方法
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				//e1 是起始点
				//e2是抬起点(终止点)
				// TODO Auto-generated method stub
				if(e1.getRawX()-e2.getRawX()>100) {
					//下一页 右向左滑动
					Move_Next();
				}
				else if(e2.getRawX()-e1.getRawX()>100) {
					//上一页  由左向右滑动
					Move_Pre();
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			
		
			
		});
	}
	
	//抽象方法定义跳转到下一页的方法
	
	public abstract void Move_Next();
	//抽象方法 定义跳转到上一页
	public abstract void Move_Pre();
	//同一处理下一页的点击事件
	public void btn_next_page(View view) {
		Move_Next();
	}
	//同一处理上一页的点击事件
	public  void btn_pre_page(View view) {
		Move_Pre();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);//将activity中手势移动操作,交由手势识别处理器处理
		return super.onTouchEvent(event);
	}
}
