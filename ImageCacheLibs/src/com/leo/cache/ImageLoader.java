package com.leo.cache;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.leo.common.Callback;
import com.leo.util.MD5Util;

/**
 * 1. Keep Clean away from Download. 2. More timely recycle. 3. Priority of
 * download.
 * 
 * @author Kang, Leo
 */
public class ImageLoader extends CacheLoader {

	/**
	 * Memory cache!
	 */
	// public static ArrayList<ImageLoader> instances = new
	// ArrayList<ImageLoader>();
	private static ImageLoader instance;
	private String cacheDir = null;

	public synchronized static ImageLoader getInstance(Context context,
			String loaderTag, String cachedir) {
		// ImageLoader result = null;
		// for (ImageLoader loader : instances) {
		// if (loader.tag.equalsIgnoreCase(loaderTag)) {
		// result = loader;
		// }
		// }
		if (instance == null) {
			final String path = getCacheFolder(context, cachedir);

			// judge static cover picture
			instance = new ImageLoader(context, loaderTag, path, true);
			// TODO can enable auto clean cache file.
			instance.cleanCache = false;
			// instances.add(result);
		}
		// result.restartThreadPool();
		return instance;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public ImageLoader(Context context, String tag, String cachePath,
                       boolean sortASC) {
		super(context, cachePath, sortASC);
		this.tag = tag;
		this.cacheDir = cachePath;
	}

	// public static void hardEvict() {
	// for (ImageLoader self : instances) {
	// self.cleanMemoryCache();
	// self.searchThreadPool.shutdownNow();
	// self.downloadThreadQueue.cancelQueueByCategory(self.tag);
	// self.restartThreadPool();
	// }
	// }

	/**
	 * 获取缓存路径
	 * 
	 * @return
	 */
	public String getCacheDir() {
        return this.cacheDir;
	}

	/**
	 * 获取文件路径
	 * 
	 * @param url
	 * @return
	 */
	public String getFilePath(String url) {
		return cacheDir + "/" + MD5Util.getStringMD5(url);
	}


	public synchronized void loadRemoteImage(String url, ImageView view,
			Builder cacheParams, Callback callback) {
		if (URLUtil.isValidUrl(url)) {
			doLoadRemoteImage(url, view, cacheParams, callback);
		}

	}

	public synchronized void loadLocalImage(String filename, ImageView view,
			Builder cacheParams, Bitmap loadingBitmap,
			Callback callback) {
		if (URLUtil.isFileUrl(filename)) {
			doLoadLocalImage(filename, view, cacheParams, loadingBitmap,
					callback);
		}
	}

}
