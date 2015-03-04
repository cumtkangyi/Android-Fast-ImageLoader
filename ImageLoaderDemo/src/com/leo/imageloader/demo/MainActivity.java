package com.leo.imageloader.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.leo.common.CC;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		
		/*如下代码可以放在application中初始化*/
		// CC.init(getApplicationContext(), "Test", R.drawable.ic_launcher);
		
		String url = "http://h.hiphotos.baidu.com/image/pic/item/b8014a90f603738d26724c24b11bb051f819ecf8.jpg";
		CC.bind(url, imageView);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
