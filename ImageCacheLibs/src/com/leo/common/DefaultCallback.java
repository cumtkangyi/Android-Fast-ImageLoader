package com.leo.common;

import java.lang.ref.SoftReference;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.leo.cache.CacheLoader;
import com.leo.common.Callback;
import com.leo.util.ImageUtil;

/**
 * base listener.
 * 
 * @author yi.kang
 */
public class DefaultCallback implements Callback {
	SoftReference<ImageView> view;
	ProgressBar progressBar;
	Handler handler = null;

	public DefaultCallback(ImageView view) {
		handler = new Handler(Looper.getMainLooper());
		this.view = new SoftReference<ImageView>(view);
	}

	public DefaultCallback(ImageView view, ProgressBar progressBar) {
		this(view);
		this.progressBar = progressBar;
	}

	@Override
	public void onProgress(int i) {
	}

	@Override
	public void callback(ImageView imageView, final BitmapDrawable drawable,
                         CacheLoader.Builder cacheParams, boolean isCached) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
				final ImageView v = view.get();
				if (v != null) {
					// v.setImageDrawable(drawable);
					if (drawable.getBitmap().getWidth() > drawable.getBitmap()
							.getHeight()) {
						v.setImageBitmap(ImageUtil.rotaingImageView(90,
								drawable.getBitmap()));
					} else {
						v.setImageBitmap(drawable.getBitmap());
					}
				}
			}
		});
	}

	@Override
	public void onError() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void onLoadGIF(String filePath) {

	}

	@Override
	public void onStart() {
	}

	@Override
	public void onPreStart(ImageView imageView, String url) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
			}
		});
	}

}