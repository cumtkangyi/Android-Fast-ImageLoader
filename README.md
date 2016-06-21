# Android Fast ImageLoader

A cool image cache for Android platform.

[Demo][1]

## Android Permission

```java
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

## Sample Code

```java
ImageView imageView = new ImageView(mContext);
/*如下代码可以放在application中初始化*/
FastImageLoader.init(getApplicationContext(), "Test", R.drawable.ic_launcher);
        
String url = "http://h.hiphotos.baidu.com/image/pic/item/b8014a90f603738d26724c24b11bb051f819ecf8.jpg";
FastImageLoader.bind(url, imageView);
```


If you want to load with progress bar, please perform the following code:

```java
ImageView imageView = new ImageView(mContext);
ProgressBar progressBar = new ProgressBar(mContext);
/*如下代码可以放在application中初始化*/
FastImageLoader.init(getApplicationContext(), "Test", R.drawable.ic_launcher);
        
String url = "http://h.hiphotos.baidu.com/image/pic/item/b8014a90f603738d26724c24b11bb051f819ecf8.jpg";
FastImageLoader.bind(url, imageView, new DefaultCallback(imageView, progressBar));
```

## Alipay Account

kangyi888cumt@163.com

## Developed By

* Leo Kang, Ning Dai
* Email:  cumtkangyi@gmail.com
 
## License


    Copyright 2012-2015 Leo.Kang  Ning.Dai
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: http://kangvp.apps.cn/
