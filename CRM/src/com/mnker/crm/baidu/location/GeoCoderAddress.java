package com.mnker.crm.baidu.location;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class GeoCoderAddress implements OnGetGeoCoderResultListener {

	private Handler mGeoResultHandler;
	private CurrentLocation location;
	private GeoCoder mSearch = null;
	private BDLocationListener locListener = new BDLocationListener() {

		@Override
		public void onReceivePoi(BDLocation arg0) {
		}

		@Override
		public void onReceiveLocation(BDLocation loc) {
			if (loc == null || location == null || mSearch == null) {
				Log.d("liujin", "GeoCoderAddress locListener return null.....");
				return;
			}
			LatLng ptCenter = new LatLng(loc.getLatitude(), loc.getLongitude());
			if (mSearch != null) {
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
			}
			location.stop();
		}
	};

	public GeoCoderAddress(Context con, Handler handler) {
		this.mGeoResultHandler = handler;
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		this.location = new CurrentLocation(con, locListener);
	}

	public void start() {
		this.location.start();
	}

	public void finish() {
		location.destroy();
		location = null;
		mSearch = null;
	}

	/**
	 * 地理编码查询结果回调函数(地理位置--->经纬度)
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult res) {

	}

	/**
	 * 反地理编码查询结果回调函数(经纬度--->地理位置)
	 */
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Log.d("liujin", "抱歉，未能找到结果");
			return;
		}
		Message msg = Message.obtain();
		msg.what = 200;
		// msg.obj = result.getAddress();
		msg.obj = result.getLocation();
		Bundle bundle = new Bundle();
		bundle.putString("address", result.getAddress());
		msg.setData(bundle);
		if (mGeoResultHandler != null) {
			mGeoResultHandler.sendMessage(msg);
		}
	}
}
