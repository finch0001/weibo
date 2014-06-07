package com.lm.net;

public class Request {
	public String url;
	public static final String ENCODING = "UTF-8";

	public JsonCallback callback;

	public Request(String url) {
		this.url = url;
	}

	public void setCallback(JsonCallback callback) {
		this.callback = callback;
	}

	public void execute() {
		RequestTask task = new RequestTask(this);
		task.execute();
	}
}
