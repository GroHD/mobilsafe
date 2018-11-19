package com.itcasthd.mobilesafe.service;

import java.util.List;

import com.itcasthd.mobilesafe.Utils.ContentValue;
import com.itcasthd.mobilesafe.Utils.ModifyOffset;
import com.itcasthd.mobilesafe.Utils.PointDouble;
import com.itcasthd.mobilesafe.Utils.SpUtils;
import com.itcasthd.mobilesafe.service.LocationService.MyLocationListener;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * 定位服务
 */
public class LocationService extends Service {

	private MyLocationListener myLocationListener;
	private LocationManager lm;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		//因为每次收到短信都需要重新查询坐标,所以在onstart中
		// 获取位置管理对象
		lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);
		// 以最优的方式获取经纬度坐标
		Criteria criteria = new Criteria();
		// 允许花费流量获取坐标
		criteria.setCostAllowed(true);
		// 指定获取经纬度的精确度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 拿到最优的获取方式
		String bestProvider = lm.getBestProvider(criteria, true);
		// 在一定时间间隔或移动一定的距离后获取坐标
		if (bestProvider != null) {
			myLocationListener = new MyLocationListener();
			lm.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);
		}
	}


	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				// 精度
				double longitude = location.getLongitude();
				// 维度
				double latitude = location.getLatitude();
				// 发送短信
				SmsManager sms = SmsManager.getDefault();
				String cantact = SpUtils.getString(getApplicationContext(), ContentValue.CONTACT, "10086");
				try {
					ModifyOffset instance = ModifyOffset
							.getInstance(ModifyOffset.class.getResourceAsStream("axisoffset.dat"));
					PointDouble s2c = instance.s2c(new PointDouble(longitude, latitude));
					String contentMess = "http://uri.amap.com/marker?position=" + s2c.getX() + "," + s2c.getY();
					sms.sendTextMessage(cantact, null, contentMess, null, null);
					
				} catch (Exception ex) {
					// 出错就发送没转换的坐标
					String contentMess = "http://uri.amap.com/marker?position=" + longitude + "," + latitude
							+ "&name=手机位置&src=mypage&coordinate=gaode&callnative=0";
					sms.sendTextMessage(cantact, null, contentMess, null, null);
				} finally {
					if (myLocationListener != null) {
						lm.removeUpdates(myLocationListener);
					}
				}
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
