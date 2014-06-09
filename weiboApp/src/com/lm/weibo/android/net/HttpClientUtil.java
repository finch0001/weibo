package com.lm.weibo.android.net;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lm.weibo.android.net.AppException.EnumException;

public class HttpClientUtil {

	public static HttpResponse execute(Request request) throws AppException {
		switch (request.method) {
		case GET:
			return get(request);
		case POST:
			return post(request);
		default:
			throw new AppException(EnumException.NormallException, "the method "
					+ request.method.name() + " doesn't support");
		}
	}

	public static HttpResponse get(Request request) throws AppException {
		try {
			if (request.callback != null) {
				request.callback.checkIfCancelled();
			}
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(request.url);
			addHeader(get, request.headers);
			HttpResponse response = client.execute(get);
			return response;
		} catch (ClientProtocolException e) {
			throw new AppException(EnumException.ClientProtocolException, e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.IOException, e.getMessage());
		}
	}

	public static HttpResponse post(Request request) throws AppException {
		try {
			if (request.callback != null) {
				request.callback.checkIfCancelled();
			}
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(request.url);
			addHeader(post, request.headers);
			if (request.entity == null) {
				throw new AppException(EnumException.NormallException, "you forget to set post content to the HttpPost");
			} else {
				post.setEntity(request.entity);
			}
			HttpResponse response = client.execute(post);
			return response;
		} catch (ClientProtocolException e) {
			throw new AppException(EnumException.ClientProtocolException, e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.IOException, e.getMessage());
		}
	}

	public static void addHeader(HttpUriRequest request,
			Map<String, String> headers) {
		if (headers != null && headers.size() > 0) {
			for (Entry<String, String> entry : headers.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}
}
