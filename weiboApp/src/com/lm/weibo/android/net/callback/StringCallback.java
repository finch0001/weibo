package com.lm.weibo.android.net.callback;

import com.lm.weibo.android.net.utilities.IOUtilities;
import com.lm.weibo.android.net.utilities.TextUtil;

public abstract class StringCallback extends AbstractCallback<Object> {

	@Override
	protected Object bindData(String content) {
		if (TextUtil.isValidate(path)) {
			return IOUtilities.readFromFile(path);
		}
		return content;
	}

}
