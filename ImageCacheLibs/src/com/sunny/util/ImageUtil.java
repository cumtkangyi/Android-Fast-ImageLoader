package com.sunny.util;

import java.io.IOException;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.ECLAIR)
public class ImageUtil {

	public static final String TAG = "ImageUtil";

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap
				.createBitmap(width, height, Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	public static Bitmap adjustPhotoRotation(Bitmap bm,
			final int orientationDegree) {

		Matrix m = new Matrix();
		m.setRotate(orientationDegree, (float) bm.getWidth() / 2,
				(float) bm.getHeight() / 2);
		float targetX, targetY;
		if (orientationDegree == 90) {
			targetX = bm.getHeight();
			targetY = 0;
		} else {
			targetX = bm.getHeight();
			targetY = bm.getWidth();
		}

		final float[] values = new float[9];
		m.getValues(values);

		float x1 = values[Matrix.MTRANS_X];
		float y1 = values[Matrix.MTRANS_Y];

		m.postTranslate(targetX - x1, targetY - y1);

		Bitmap bitmap = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(),
				Bitmap.Config.ARGB_8888);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(bm, m, paint);

		return bitmap;
	}

	public Bitmap cutBitmap(Bitmap bitmap) {
		int cut_width;
		int cut_height;
		int cavas_width = 640;// 畫布大小
		int cavas_height = 480;
		int startx = 0;// 剪切的起始點
		int starty = 0;
		Bitmap resultBitmap = null;
		Matrix cut_matrix = new Matrix();
		if (bitmap.getWidth() > bitmap.getHeight()) {// 當width>height的時候，設置縮放比例
			cut_width = (bitmap.getHeight() * 4) / 3;
			cut_height = bitmap.getHeight();
			float xb = ((float) cavas_width) / cut_width;
			float yb = ((float) cavas_height) / cut_height;
			cut_matrix.postScale(xb, yb);
			startx = (bitmap.getWidth() - cut_width) / 2;
			resultBitmap = Bitmap.createBitmap(bitmap, startx, starty,
					cut_width, cut_height, cut_matrix, true);
		} else if (bitmap.getWidth() < bitmap.getHeight()) {// 當width <
															// height的時候，設置縮放比例
			cut_width = bitmap.getWidth();
			cut_height = (bitmap.getWidth() * 3) / 4;
			float xb = ((float) cavas_width) / cut_width;
			float yb = ((float) cavas_height) / cut_height;
			cut_matrix.postScale(xb, yb);
			starty = (bitmap.getHeight() - cut_height) / 2;
			resultBitmap = Bitmap.createBitmap(bitmap, 0, starty, cut_width,
					cut_height, cut_matrix, true);
		}
		return resultBitmap;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// System.out.println("angle2=" + angle);
		// 创建新的图片
		try {
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			return resizedBitmap;
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
			return bitmap;
		}

	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}