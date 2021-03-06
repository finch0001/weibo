package com.lm.weibo.android.utils;

import java.io.Closeable;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Build;

public class Util {
	// 屏幕宽度分辨率
	public static int widthPixels;
	// 屏幕高度分辨率
	public static int heightPixels;
	
	/**
	 * 检查字符串是否为空
	 * 
	 * @param content 字符串
	 * @return true 不为空; false 为空
	 */
	public static boolean isValidate(String content) {
		return (content != null) && (!("".equals(content.trim())));
	}

	/**
	 * 将国际时间转换为  xx月xx日 xx时xx分  格式 
	 * 
	 * @param date 国际时间
	 * @return xx月xx日 xx时xx分  格式 时间
	 */
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
	
    public static int length(String paramString) {
        int i = 0;
        for (int j = 0; j < paramString.length(); j++) {
            if (paramString.substring(j, j + 1).matches("[Α-￥]")) {
                i += 2;
            } else {
                i++;
            }
        }

        if (i % 2 > 0) {
            i = 1 + i / 2;
        } else {
            i = i / 2;
        }

        return i;
    }
    
    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }
}
