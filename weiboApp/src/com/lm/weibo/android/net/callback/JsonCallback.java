package com.lm.weibo.android.net.callback;

import com.lm.weibo.android.net.utilities.IOUtilities;
import com.lm.weibo.android.net.utilities.JsonParser;
import com.lm.weibo.android.net.utilities.TextUtil;

public abstract class JsonCallback<T> extends AbstractCallback<T> {
	
	protected String onJsonPreHandle(String content) {
		return content;
	}

	@Override
	protected T bindData(String content) {
		if (TextUtil.isValidate(path)) {
			content = IOUtilities.readFromFile(path);
		}
		if (returnClass != null) {
			return JsonParser.deserializeByJson(content, returnClass);
		} else if (returnType != null) {
			// JSONTokener jsonParser = new JSONTokener(content);
			// JSONObject weibo = (JSONObject) jsonParser.nextValue();
			// JSONArray items = weibo.getJSONArray("statuses");
			// content = items.toString();
			// return JsonParser.deserializeByJson(content, returnType);
			return JsonParser.deserializeByJson(onJsonPreHandle(content), returnType);
		}
		return null;
	}

}
