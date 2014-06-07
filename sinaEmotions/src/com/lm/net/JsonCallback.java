package com.lm.net;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.lm.utils.IOUtil;
import com.lm.utils.JsonParser;
import com.lm.utils.Util;

public abstract class JsonCallback<T> {
	public Type returnType;
	private boolean save = false;
	private String path;

	public JsonCallback<T> setReturnType(Type type) {
		this.returnType = type;
		return this;
	}

	public JsonCallback<T> setIfSaveData(boolean save, String path) {
		this.save = save;
		this.path = path;
		return this;
	}

	public Object handle(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		switch (response.getStatusLine().getStatusCode()) {
		case HttpStatus.SC_OK:
			try {
				String data = EntityUtils.toString(entity, Request.ENCODING);
				JSONTokener jsonParser = new JSONTokener(data);
//				JSONObject weibo = (JSONObject) jsonParser.nextValue();
//				JSONArray items = weibo.getJSONArray("statuses");
//				data = items.toString();
				if (save && Util.isValidate(path)) {
					IOUtil.writeToFile(data, path);
				}
				return JsonParser.deserializeByJson(data, returnType);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		default:
			return null;
		}
	}

	public abstract void onFailure(Exception result);

	public abstract void onSuccess(T result);
}
