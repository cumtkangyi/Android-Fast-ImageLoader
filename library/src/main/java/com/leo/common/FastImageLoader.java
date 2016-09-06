package com.leo.common;

import android.content.Context;
import android.widget.ImageView;

import com.leo.cache.CacheLoader;
import com.leo.cache.ImageLoader;

/**
 * Created by kangyi on 15/3/3.
 * <p/>
 * Center Controller
 */
public class FastImageLoader {

    static Context mContext;
    static String mCacheDir;
    static int mDefaultLoadingImageResId;
    static boolean mNeedRotate;

    public static void bind(String url, ImageView imageView) {
        bind(url, imageView, false, null);
    }

    public static void bind(String url, ImageView imageView, int width, int height) {
        bind(url, imageView, false, width, height, null);
    }

    public static void bind(String url, ImageView imageView, Callback callback) {
        bind(url, imageView, false, callback);
    }

    public static void bind(String url, ImageView imageView, boolean isLocal) {
        bind(url, imageView, isLocal, null);
    }

    public static void bind(String url, ImageView imageView, boolean isLocal, Callback callback) {
        ImageLoader loader = ImageLoader.getInstance(mContext, "", mCacheDir);
        if (!isLocal) {
            loader.loadRemoteImage(url, imageView, new CacheLoader.Builder(0, 0),
                    callback != null ? callback : new DefaultCallback(imageView, mNeedRotate));
        } else {
            loader.loadLocalImage(url, imageView, new CacheLoader.Builder(0, 0), null,
                    callback != null ? callback : new DefaultCallback(imageView, mNeedRotate));
        }
    }

    public static void bind(String url, ImageView imageView, boolean isLocal, int width, int height, Callback callback) {
        ImageLoader loader = ImageLoader.getInstance(mContext, "", mCacheDir);
        if (!isLocal) {
            loader.loadRemoteImage(url, imageView, new CacheLoader.Builder(width, height),
                    callback != null ? callback : new DefaultCallback(imageView, mNeedRotate));
        } else {
            loader.loadLocalImage(url, imageView, new CacheLoader.Builder(width, height), null,
                    callback != null ? callback : new DefaultCallback(imageView, mNeedRotate));
        }
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static void setCacheDir(String cacheDir) {
        mCacheDir = cacheDir;
    }

    public static void setDefaultLoadingImageResId(int resId) {
        mDefaultLoadingImageResId = resId;
    }

    /**
     * 初始化图片缓存库
     *
     * @param context
     * @param cacheDir
     * @param defaultLoadingImageResId
     */
    public static void init(Context context, String cacheDir, int defaultLoadingImageResId) {
        init(context, cacheDir, defaultLoadingImageResId, true);
    }

    /**
     * 初始化图片缓存库
     *
     * @param context
     * @param cacheDir
     * @param defaultLoadingImageResId
     */
    public static void init(Context context, String cacheDir, int defaultLoadingImageResId, boolean needRotate) {
        mContext = context;
        mCacheDir = cacheDir;
        mDefaultLoadingImageResId = defaultLoadingImageResId;
        mNeedRotate = needRotate;
    }
}
