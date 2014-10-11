CoolCache
=========

A cool image cache

ImageView imageView = new ImageView(mContext);
ThumbnailLoader loader = ThumbnailLoader.getInstance(context, "", Constant.CACHE_DIR);
loader.loadRemoteImage(url, imageView,
				new CacheWorker.Builder(0, 0)
						.setLoadingImage(R.drawable.load_default),
				new BaseLoadListener(imageView));
