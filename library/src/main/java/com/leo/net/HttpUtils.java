package com.leo.net;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtils {

	public void downloadInFile(String urlString, File file, Context context)
			throws IOException {

		HttpURLConnection urlConnection = null;
		FileOutputStream out = null;
		File cacheFile = file;
		if (!cacheFile.exists()) {
			cacheFile.getParentFile().mkdirs();
		}
		URL url;
		InputStream is = null;
		try {
			url = new URL(urlString);
			urlConnection = (HttpURLConnection) url
					.openConnection();
			if (urlConnection instanceof HttpsURLConnection) {
				SSLSocketFactory sslSocketFactory = getTrustAllSSLSocketFactory();
				if (sslSocketFactory != null) {
					((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
				}
			}

			urlConnection.setConnectTimeout(20000);
			urlConnection.setReadTimeout(20000);
			is = urlConnection.getInputStream();
			out = new FileOutputStream(cacheFile);
			byte[] buffer = new byte[4 * 1024];
			int b = -1;
			while ((b = is.read(buffer)) != -1) {
				out.write(buffer, 0, b);
			}
			if (cacheFile != null
					&& cacheFile.length() < urlConnection.getContentLength()) {
				if (cacheFile.exists()) {
					cacheFile.delete();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		cacheFile.setLastModified(System.currentTimeMillis());
	}

	public InputStream downloadInMemory(String urlString, Context context)
			throws IOException {

		HttpURLConnection urlConnection = null;
		URL url;
		InputStream is = null;
		try {
			url = new URL(urlString);
			urlConnection = (HttpURLConnection) url
					.openConnection();
			if (urlConnection instanceof HttpsURLConnection) {
				SSLSocketFactory sslSocketFactory = getTrustAllSSLSocketFactory();
				if (sslSocketFactory != null) {
					((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
				}
			}
			urlConnection.setConnectTimeout(20000);
			urlConnection.setReadTimeout(20000);
			is = urlConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return is;
	}

	private static SSLSocketFactory trustAllSSlSocketFactory;

	public static SSLSocketFactory getTrustAllSSLSocketFactory() {
		if (trustAllSSlSocketFactory == null) {
			synchronized (HttpUtils.class) {
				if (trustAllSSlSocketFactory == null) {

					// 信任所有证书
					TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
						@Override
						public X509Certificate[] getAcceptedIssuers() {
							return null;
						}

						@Override
						public void checkClientTrusted(X509Certificate[] certs, String authType) {
						}

						@Override
						public void checkServerTrusted(X509Certificate[] certs, String authType) {
						}
					}};
					try {
						SSLContext sslContext = SSLContext.getInstance("TLS");
						sslContext.init(null, trustAllCerts, null);
						trustAllSSlSocketFactory = sslContext.getSocketFactory();
					} catch (Throwable ex) {
						//LogUtil.e(ex.getMessage());
					}
				}
			}
		}

		return trustAllSSlSocketFactory;
	}
}
