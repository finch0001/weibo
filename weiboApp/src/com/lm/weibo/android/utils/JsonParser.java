package com.lm.weibo.android.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class JsonParser {
	public static Gson gson = new Gson();

	public static <T> T deserializeByJson(String data, Type type) {
		if (TextUtil.isValidate(data)) {
			return gson.fromJson(data, type);
		}
		return null;
	}

	public static <T> T deserializeByJson(String data, Class<T> clz) {
		if (TextUtil.isValidate(data)) {
			return gson.fromJson(data, clz);
		}
		return null;
	}

	public static <T> String serializeToJson(T t) {
		if (t == null) {
			return "";
		}
		return gson.toJson(t);
	}
}
