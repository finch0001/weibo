package com.lm.weibo.android.net;

public class Urls {
	public static final int DEFAULT_COUNT = 30;
	public static final int LOADMORE_COUNT = 10;

	// 获取当前登录用户及其所关注用户的最新微博
	public static final String url_home_timeline = "https://api.weibo.com/2/statuses/home_timeline.json"
			+ "?access_token=";

	// 根据ID获取单条微博信息
	public static final String url_show = "https://api.weibo.com/2/statuses/show.json"
			+ "?access_token=";
}
