package com.mnker.crm.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mnker.crm.widget.calendar.Constant.Common;

public class HttpXUtils {
	public interface IOAuthCallBack {
		public void succesCallBack(String result);

		public void failureCallBack(String result);
	}

	public static void getJson(String url, String paramsJson, final IOAuthCallBack iOAuthCallBack) {

		RequestParams params = new RequestParams();
		if (TextUtils.isEmpty(paramsJson)) {
			iOAuthCallBack.failureCallBack("params null");
			return;
		}

		try {
			System.out.println(paramsJson);
			StringEntity entity = new StringEntity(paramsJson, HTTP.UTF_8);
			entity.setContentType("application/json");
			params.setBodyEntity(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);// ���ó�ʱʱ��
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {// �ӿڻص�

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						iOAuthCallBack.failureCallBack(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						// TODO Auto-generated method stub
						iOAuthCallBack.succesCallBack(info.result);// ���ýӿڻص����ݴ���
					}
				});
	}

	public static void getJson(String url, final IOAuthCallBack iOAuthCallBack) {
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);// ���ó�ʱʱ��
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {// �ӿڻص�

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						iOAuthCallBack.failureCallBack(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						// TODO Auto-generated method stub
						iOAuthCallBack.succesCallBack(info.result);// ���ýӿڻص����ݴ���
					}
				});
	}

	public static void getCataJson(int cityId, IOAuthCallBack iOAuthCallBack) {// �ⲿ�ӿں���
		String url = "http://xxxxxxxxxx";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("currentCityId", cityId + "");
		// getJson(url, params, iOAuthCallBack);
	}

	public static void doPost(String url, String paramsJson, final IOAuthCallBack iOAuthCallBack) {
		RequestParams params = new RequestParams();
		if (TextUtils.isEmpty(paramsJson)) {
			iOAuthCallBack.failureCallBack("params null");
			return;
		}
		try {
			System.out.println(paramsJson);
			StringEntity entity = new StringEntity(paramsJson, HTTP.UTF_8);
			entity.setContentType("application/json");
			params.setBodyEntity(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				iOAuthCallBack.failureCallBack(arg1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				// TODO Auto-generated method stub
				iOAuthCallBack.succesCallBack(info.result);
			}
		});
	}

	public static void doPostLogin(int cityId, IOAuthCallBack iOAuthCallBack) {
		String url = "http://xxxxxxxxxxxx";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("currentCityId", cityId + "");
		params.addBodyParameter("path", "/apps/postCatch");
		// doPost(url, params, iOAuthCallBack);
	}

	public static String getInfoByPostObject(String url, Object object, String token) {

		GsonBuilder builder = new GsonBuilder();
		String json = builder.create().toJson(object);
		// String json =(String)object;
		Log.d("zaokun", "getInfoByPostObject==========sendurl=====================" + url);
		Log.d("zaokun", "getInfoByPostObject==========send=====================" + json);
		StringEntity entity;
		String jsonOut = "-1";
		try {
			// if (json.contains("signTime")) {
			// json = "[" + json + "]";
			// }
			entity = new StringEntity(json, HTTP.UTF_8);

			entity.setContentType("application/json");
			DefaultHttpClient client = new DefaultHttpClient();
			client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			HttpPost httpPost = new HttpPost(url);
			if (token != null) {
				httpPost.addHeader("token", token);
			}
			httpPost.setEntity(entity);

			HttpResponse response = client.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();
			StringBuffer buffer = new StringBuffer();
			InputStreamReader inputReader = new InputStreamReader(inputStream);
			BufferedReader bufferReader = new BufferedReader(inputReader);
			String str = new String("");
			while ((str = bufferReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferReader.close();
			jsonOut = buffer.toString();
			Log.d("sys", "getInfoByPostObject==========received=====================" + jsonOut);
			System.out.println(jsonOut);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.d("sys", url + "-----UnsupportedEncodingException");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.d("sys", url + "-----ClientProtocolException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("sys", url + "-----IOException");
			e.printStackTrace();
		}

		return jsonOut;
	};
}
