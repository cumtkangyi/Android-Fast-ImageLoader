package com.sunny.cache;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.sunny.util.MD5Util;

/**
 * 1. Keep Clean away from Download. 2. More timely recycle. 3. Priority of
 * download.
 * 
 * @author Kang, Leo
 */
public class ThumbnailLoader extends CacheWorker {

	/**
	 * Memory cache!
	 */
	// public static ArrayList<ThumbnailLoader> instances = new
	// ArrayList<ThumbnailLoader>();
	private static ThumbnailLoader instance;
	private String cacheDir = null;

	public synchronized static ThumbnailLoader getInstance(Context _context,
			String loaderTag, String cachedir) {
		// ThumbnailLoader result = null;
		// for (ThumbnailLoader loader : instances) {
		// if (loader.tag.equalsIgnoreCase(loaderTag)) {
		// result = loader;
		// }
		// }
		if (instance == null) {
			final String path = getCacheFolder(_context, cachedir);

			// judge static cover picture
			instance = new ThumbnailLoader(_context, loaderTag, path, true);
			// TODO can enable auto clean cache file.
			instance.cleanCache = false;
			// instances.add(result);
		}
		// result.restartThreadPool();
		return instance;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public ThumbnailLoader(Context _context, String tag, String cachePath,
			boolean sortASC) {
		super(_context, cachePath, sortASC);
		this.tag = tag;
		this.cacheDir = cachePath;
	}

	// public static void hardEvict() {
	// for (ThumbnailLoader self : instances) {
	// self.cleanMemoryCache();
	// self.searchThreadPool.shutdownNow();
	// self.downloadThreadQueue.cancelQueueByCategory(self.tag);
	// self.restartThreadPool();
	// }
	// }

	/**
	 * 获取缓存目录
	 * 
	 * @return
	 */
	public String getCacheDir() {
		return this.cacheDir;
	}

	/**
	 * 获取缓存文件路径
	 * 
	 * @param url
	 * @return
	 */
	public String getFilePath(String url) {
		return cacheDir + "/" + MD5Util.getStringMD5(url);
	}

	public synchronized void loadRemoteImage(String url, ImageView view,
			Builder cacheParams, OnSetImageListener setImageListener) {
		if (URLUtil.isValidUrl(url)) {
			doLoadRemoteImage(url, view, cacheParams, setImageListener);
		}

	}

	public synchronized void loadLocalImage(String filename, ImageView view,
			Builder cacheParams, Bitmap loadingBitmap,
			OnSetImageListener setImageListener) {
		if (URLUtil.isFileUrl(filename)) {
			doLoadLocalImage(filename, view, cacheParams, loadingBitmap,
					setImageListener);
		}
	}

}
