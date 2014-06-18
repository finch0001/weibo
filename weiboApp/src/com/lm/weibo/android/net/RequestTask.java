package com.lm.weibo.android.net;

import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;

import com.lm.weibo.android.net.Request.RequestTool;
import com.lm.weibo.android.net.interfaces.IProgressListener;

public class RequestTask extends AsyncTask<Object, Integer, Object> {

	private Request request;

	public RequestTask(Request request) {
		this.request = request;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(Object... params) {
		try {
			Object object = request.callback.onPreRequest();
			if (object != null) {
				return object;
			}
			if (request.tool == RequestTool.HTTPCLIENT) {
				HttpResponse response = HttpClientUtil.execute(request);
				if (request.mProgressListener != null) {
					object = request.callback.handle(response,
							new IProgressListener() {
								@Override
								public void onProgressUpdate(int curPos,
										int contentLength) {
									publishProgress(curPos, contentLength);
								}
							});
				} else {
					object = request.callback.handle(response, null);
				}
			} else {
				HttpURLConnection connection = HttpUrlUtil.execute(request);
				if (request.mProgressListener != null) {
					object = request.callback.handle(connection, new IProgressListener() {
						@Override
						public void onProgressUpdate(int curPos, int contentLength) {
							publishProgress(curPos, contentLength);
						}
					});
				} else {
					object = request.callback.handle(connection, null);
				}
			}
			return request.callback.onPreHandle(object);
		} catch (AppException e) {
			return e;
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (request.mProgressListener != null) {
			request.mProgressListener.onProgressUpdate(values[0], values[1]);
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (result instanceof Exception) {
			request.callback.onFailure((AppException) result);
		} else {
			request.callback.onSuccess(result);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (request.callback != null) {
			request.callback.cancel();
		}
	}

}
