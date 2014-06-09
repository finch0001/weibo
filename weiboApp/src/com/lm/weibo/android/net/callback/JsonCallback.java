package com.lm.weibo.android.net.callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.lm.weibo.android.net.AppException;
import com.lm.weibo.android.net.AppException.EnumException;
import com.lm.weibo.android.net.utilities.IOUtilities;
import com.lm.weibo.android.net.utilities.JsonParser;
import com.lm.weibo.android.net.utilities.TextUtil;

public abstract class JsonCallback<T> extends AbstractCallback<T> {

	@Override
	protected T bindData(String content) throws AppException {
		if (TextUtil.isValidate(path)) {
			content = IOUtilities.readFromFile(path);
		}
		if (returnClass != null) {
			return JsonParser.deserializeByJson(content, returnClass);
		} else if (returnType != null) {
			try {
				JSONTokener jsonParser = new JSONTokener(content);
				JSONObject weibo = (JSONObject) jsonParser.nextValue();
				JSONArray items = weibo.getJSONArray("statuses");
				content = items.toString();
				return JsonParser.deserializeByJson(content, returnType);
			} catch (JSONException e) {
				throw new AppException(EnumException.JSONException, e.getMessage());
			}
		}
		return null;
	}

}
