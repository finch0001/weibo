package com.lm.weibo.android.net.interfaces;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;

import com.lm.weibo.android.net.AppException;

public interface ICallback<T> {
	void onFailure(AppException result);
	void onSuccess(T result);
	T onPreRequest();
	T onPreHandle(T object);
	T handle(HttpResponse response, IProgressListener mProgressListener) throws AppException;
	void checkIfCancelled() throws AppException;
	void cancel();
	T handle(HttpURLConnection response, IProgressListener mProgressListener) throws AppException;
	boolean onPrepareParams(OutputStream out) throws AppException;
}
