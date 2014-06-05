package com.lm.weibo.android.net;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;

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
			HttpResponse response = HttpClientUtil.execute(request);
			return request.callback.handle(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (result instanceof Exception) {
			request.callback.onFailure((Exception) result);
		} else {
			request.callback.onSuccess(result);
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

}
