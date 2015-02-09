package com.mnker.crm.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.mnker.crm.widget.util.PreferencesService;

public class DemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		// 请在manifest配置正确的key(百度地图)
		SDKInitializer.initialize(this.getApplicationContext());
		PreferencesService.init(this);
	}

}
