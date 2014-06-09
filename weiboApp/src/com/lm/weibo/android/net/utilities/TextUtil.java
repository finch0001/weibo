package com.lm.weibo.android.net.utilities;

public class TextUtil {
	public static boolean isValidate(String content) {
		return (content != null) && (!("".equals(content.trim())));
	}
}
