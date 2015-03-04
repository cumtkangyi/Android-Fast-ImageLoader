package com.leo.common;

import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.leo.cache.CacheLoader;

/**
 * The base listener for UI. You can handle Callback.
 * 
 * @author Kang, Leo
 */
public interface Callback {

	/**
	 * Call this method prepare download.
	 * 
	 * @param url
	 * @param imageView
	 */
	void onPreStart(final ImageView imageView, final String url);

	/**
	 * Call this method begin download.
	 */
	void onStart();

	/**
	 * Call this method while downloading.
	 * 
	 * @param i
	 *            percentage. (0-100)
	 */
	void onProgress(int i);

	/**
	 * Set image at here.
	 * 
	 * @param imageView
	 * @param drawable
	 * @param cacheParams
	 * @param isCached
	 */
	void callback(final ImageView imageView, final BitmapDrawable drawable,
                  final CacheLoader.Builder cacheParams, final boolean isCached);

	/**
	 * Encounter problems
	 */
	void onError();

	/**
	 * If {@link com.leo.cache.CacheLoader.Builder#supportGIF(boolean)} set true. This method
	 * called when GIF is prepare to show.
	 * 
	 * @param filePath
	 *            GIF's file path.
	 */
	void onLoadGIF(String filePath);
}
