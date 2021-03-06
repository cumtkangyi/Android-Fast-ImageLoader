package com.leo.cache;

import java.lang.ref.SoftReference;
import java.util.HashSet;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
//import android.util.LruCache;

import com.leo.util.Utils;

public class MemoryCache {

	/**
	 * parameters import android.support.v4.util.LruCache;
	 * 
	 * <p>
	 * This class appeared in Android 3.1 (Honeycomb MR1); it's available as
	 * part of <a
	 * href="http://developer.android.com/sdk/compatibility-library.html"
	 * >Android's Support Package</a> for earlier releases.
	 */
	private LruCache<String, BitmapDrawable> mMemoryCache;

	private HashSet<SoftReference<Bitmap>> mReusableBitmaps;

	public MemoryCache(int cacheSize) {
		if (Utils.hasHoneycomb()) {
			mReusableBitmaps = new HashSet<SoftReference<Bitmap>>();
		}
		mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {

			/**
			 * Notify the removed entry that is no longer being cached
			 */
			@Override
			protected void entryRemoved(boolean evicted, String key,
					BitmapDrawable oldValue, BitmapDrawable newValue) {
				if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
					// The removed entry is a recycling drawable, so notify it
					// that it has been removed from the memory cache
					((RecyclingBitmapDrawable) oldValue).setIsCached(false);
				} else {
					// The removed entry is a standard BitmapDrawable

					if (Utils.hasHoneycomb()) {
						// We're running on Honeycomb or later, so add the
						// bitmap
						// to a SoftRefrence set for possible use with inBitmap
						// later
						mReusableBitmaps.add(new SoftReference<Bitmap>(oldValue
								.getBitmap()));
					}
				}
			}

			/**
			 * Measure item size in kilobytes rather than units which is more
			 * practical for a bitmap cache
			 */
			@Override
			protected int sizeOf(String key, BitmapDrawable value) {
				final int bitmapSize = getBitmapSize(value) / 1024;
				return bitmapSize == 0 ? 1 : bitmapSize;
			}
		};
	}

	/**
	 * Get the size in bytes of a bitmap in a BitmapDrawable.
	 * 
	 * @param value
	 * @return size in bytes
	 */
	@TargetApi(12)
	public static int getBitmapSize(BitmapDrawable value) {
		Bitmap bitmap = value.getBitmap();

		if (Utils.hasHoneycombMR1()) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * Try to get image from memory cache.
	 * 
	 * @param key
	 * @return May be null.
	 */
	public BitmapDrawable exist(String key) {
		BitmapDrawable memValue = null;

		if (mMemoryCache != null) {
			memValue = mMemoryCache.get(key);
		}
		return memValue;
	}

	public void put(String data, BitmapDrawable value) {
		// Add to memory cache
		if (mMemoryCache != null) {
			if (RecyclingBitmapDrawable.class.isInstance(value)) {
				// The removed entry is a recycling drawable, so notify it
				// that it has been added into the memory cache
				((RecyclingBitmapDrawable) value).setIsCached(true);
			}
			mMemoryCache.put(data, value);
		}
	}

	public void cleanCache() {
		if (mMemoryCache != null) {
			mMemoryCache.evictAll();
		}
	}
}
