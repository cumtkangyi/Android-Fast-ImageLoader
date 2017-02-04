package com.leo.imageloader.demo;

import com.leo.common.FastImageLoader;

import android.app.Application;

public class MyApp extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		/*如下代码可以放在调用CC.bin之前的代码中初始化*/
		FastImageLoader.init(getApplicationContext(), "FastImageLoader", R.drawable.ic_launcher);
	}

}
