package com.lm.weibo.android.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientUtil {
	public static HttpResponse execute(Request request) throws Exception {
		return get(request);
	}
	
	public static HttpResponse get(Request request) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(request.url);
		HttpResponse response = client.execute(get);
		return response;
	}
}
