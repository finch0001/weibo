package com.lm.weibo.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TextUtil {
	public static boolean isValidate(String content) {
		return (content != null) && (!("".equals(content.trim())));
	}

	public static String toNormalTime(String date) {
		try {
			SimpleDateFormat f = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
			Date d = f.parse(date);
			SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH时mm分",
					Locale.CHINA);
			String time = format.format(d);
			return time;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
