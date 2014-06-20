package com.lm.weibo.android.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import com.lm.weibo.android.net.AppException.EnumException;
import com.lm.weibo.android.net.utilities.TextUtil;

public class HttpUrlUtil {
	private static final int TIMEOUT_CONNECTION = 15 * 1000;
	private static final int TIMEOUT_READ = 15 * 1000;

	public static HttpURLConnection execute(Request request)
			throws AppException {
		switch (request.method) {
		case GET:
			return get(request);
		case POST:
			return post(request);
		default:
			throw new AppException(EnumException.NormallException, "the "
					+ request.method.name() + " doesn't support");
		}
	}

	private static HttpURLConnection get(Request request) throws AppException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(request.url);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(request.method.name());
			connection.setConnectTimeout(TIMEOUT_CONNECTION);
			connection.setReadTimeout(TIMEOUT_READ);
			addHeader(connection, request);
		} catch (MalformedURLException e) {
			throw new AppException(EnumException.ConnectionException,
					e.getMessage());
		} catch (ProtocolException e) {
			throw new AppException(EnumException.ConnectionException,
					e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.ConnectionException,
					e.getMessage());
		}
		return connection;
	}

	private static HttpURLConnection post(Request request) throws AppException {
		HttpURLConnection connection = null;
		OutputStream out = null;
		boolean isClosed = false;
		try {
			URL url = new URL(request.url);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(request.method.name());
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(TIMEOUT_CONNECTION);
			connection.setReadTimeout(TIMEOUT_READ);
			connection.setInstanceFollowRedirects(false);
			addHeader(connection, request);
			connection.connect();
			out = connection.getOutputStream();
			if (TextUtil.isValidate(request.urlParameters)) {
				out.write(getParams(request.urlParameters).getBytes("UTF-8"));
			}
			if (TextUtil.isValidate(request.postContent)) {
				out.write(request.postContent.getBytes("UTF-8"));
			}
			if (request.callback != null) {
				isClosed = request.callback.onPrepareParams(out);
			}
		} catch (MalformedURLException e) {
			throw new AppException(EnumException.ConnectionException,
					e.getMessage());
		} catch (ProtocolException e) {
			throw new AppException(EnumException.ConnectionException,
					e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.ConnectionException,
					e.getMessage());
		} finally {
			if (out != null && !isClosed) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return connection;
	}

	private static void addHeader(HttpURLConnection connection, Request request) {
		if (request.headers != null && request.headers.size() > 0) {
			for (Map.Entry<String, String> header : request.headers.entrySet()) {
				if (header.getKey().equalsIgnoreCase("contentLength")) {
					connection.setFixedLengthStreamingMode(Integer
							.parseInt(header.getValue()));
				} else {
					connection.addRequestProperty(header.getKey(),
							header.getValue());
				}
			}
		}
	}

	private static String getParams(String params) {
		return params + "=";
	}
}
