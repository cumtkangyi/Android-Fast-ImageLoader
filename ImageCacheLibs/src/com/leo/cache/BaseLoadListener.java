package com.leo.cache;

import java.lang.ref.SoftReference;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.leo.util.ImageUtil;

/**
 * base listener.
 * 
 * @author yi.kang
 */
public class BaseLoadListener implements OnSetImageListener {
	SoftReference<ImageView> view;
	ProgressBar progressBar;
	Handler handler = null;

	public BaseLoadListener(ImageView view) {
		handler = new Handler(Looper.getMainLooper());
		this.view = new SoftReference<ImageView>(view);
	}

	public BaseLoadListener(ImageView view, ProgressBar progressBar) {
		this(view);
		this.progressBar = progressBar;
	}

	@Override
	public void onProgress(int i) {
	}

	@Override
	public void onFinish(ImageView imageView, final BitmapDrawable drawable,
			CacheWorker.Builder cacheParams, boolean isCached) {
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
	public void onStartDownloading() {
	}

	@Override
	public void onStart(ImageView imageView, String url) {
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