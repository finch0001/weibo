package com.lm.weibo.android.net.callback;

public abstract class PathCallback extends AbstractCallback<String> {
	@Override
	protected String bindData(String content) {
		return path;
	}
}
