package com.lm.weibo.android.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;

import com.lm.weibo.android.net.interfaces.ICallback;
import com.lm.weibo.android.net.interfaces.IProgressListener;

public class Request {
	public enum RequestMethod {
		GET, POST, DELETE, PUT
	}

	public RequestMethod method;
	public String url;
	public String urlParameters;
	public String postContent;
	public Map<String, String> headers;
	public HttpEntity entity;
	public static final String ENCODING = "UTF-8";
	public ICallback callback;
	public IProgressListener mProgressListener;
	public RequestTask task;
	public RequestTool tool;
	public enum RequestTool {
		HTTPCLIENT, HTTPURLCONNECTION
	}

	public Request(String url, RequestMethod method, RequestTool tool) {
		this.url = url;
		this.method = method;
		this.tool = tool;
	}

	public void setEntity(ArrayList<NameValuePair> forms) {
		try {
			entity = new UrlEncodedFormEntity(forms, ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setEntity(String postContent) {
		try {
			entity = new StringEntity(postContent, ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setEntity(byte[] bytes) {
		entity = new ByteArrayEntity(bytes);
	}

	public void setCallback(ICallback callback) {
		this.callback = callback;
	}

	public void execute() {
		task = new RequestTask(this);
		task.execute();
	}

	public void setProgressListener(IProgressListener listener) {
		this.mProgressListener = listener;
	}

	public void cancel() {
		if (task != null) {
			task.cancel(true);
		}
//		if (callback != null) {
//			callback.cancel();
//		}
	}
}