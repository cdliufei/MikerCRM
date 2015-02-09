package com.mnker.crm.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class XutilHttp {
	public interface IOAuthCallBack {
		public void getIOAuthCallBack(String result);
	}

	public static void getJson(String url, RequestParams params, final IOAuthCallBack iOAuthCallBack) {

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);// 设置超时时间
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {// 接口回调

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						// TODO Auto-generated method stub
						iOAuthCallBack.getIOAuthCallBack(info.result);// 利用接口回调数据传输
					}
				});
	}

	public static void getCataJson(int cityId, IOAuthCallBack iOAuthCallBack) {// 外部接口函数
		String url = "http://xxxxxxxxxx";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("currentCityId", cityId + "");
		getJson(url, params, iOAuthCallBack);
	}

	public static void doPost(String url, RequestParams params, final IOAuthCallBack iOAuthCallBack) {

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				// TODO Auto-generated method stub
				iOAuthCallBack.getIOAuthCallBack(info.result);
			}
		});
	}

	public static void doPostLogin(int cityId, IOAuthCallBack iOAuthCallBack) {
		String url = "http://xxxxxxxxxxxx";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("currentCityId", cityId + "");
		params.addBodyParameter("path", "/apps/postCatch");
		doPost(url, params, iOAuthCallBack);
	}
}
