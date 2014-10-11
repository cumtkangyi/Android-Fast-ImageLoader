CoolCache
=========

A cool image cache

[Play Store Demo][1]

Sample Code
-----------
```java
ImageView imageView = new ImageView(mContext);
ThumbnailLoader loader = ThumbnailLoader.getInstance(context, "", Constant.CACHE_DIR);
loader.loadRemoteImage(url, imageView,
				new CacheWorker.Builder(0, 0)
						.setLoadingImage(R.drawable.load_default),
				new BaseLoadListener(imageView));
```


If you want to load with progress bar, please perform the following code:

```java
ImageView imageView = new ImageView(mContext);
ProgressBar progressBar = new ProgressBar(mContext);
ThumbnailLoader loader = ThumbnailLoader.getInstance(context, "", Constant.CACHE_DIR);
loader.loadRemoteImage(url, imageView,
				new CacheWorker.Builder(0, 0)
						.setLoadingImage(R.drawable.load_default),
				new BaseLoadListener(imageView, progressBar));
```


Developed By
------------
* Leo Kang, Ning Dai

License
-------

    Copyright 2012-2014 Leo.Kang  Ning.Dai
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://play.google.com/store/apps/details?id=com.pure.girls
