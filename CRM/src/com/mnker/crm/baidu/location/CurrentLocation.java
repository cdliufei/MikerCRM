package com.mnker.crm.baidu.location;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class CurrentLocation {
	private LocationClient mLocClient;

	public CurrentLocation(Context con, BDLocationListener listener) {
		mLocClient = new LocationClient(con);
		mLocClient.registerLocationListener(listener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
	}

	public void start() {
		mLocClient.start();
	}

	public void stop() {
		mLocClient.stop();
	}

	public void destroy() {
		mLocClient = null;
	}

}
