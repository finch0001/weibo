package com.lm.weibo.android.bean;

public class FragmentHomeBean {
	// 微博创建时间
	public String created_at;
	// 微博ID
	public long id;
	// 微博MID
	public String mid;
	// 字符串型的微博ID
	public String idstr;
	// 微博信息内容
	public String text;
	// 微博来源
	public String source;
	// 是否已收藏
	public boolean favorited;
	
	// 缩略图片地址
	public String thumbnail_pic;
	// 中等尺寸图片地址
	public String bmiddle_pic;
	// 原始图片地址
	public String original_pic;
	// 微博配图地址,多图时返回多图链接,无配图返回“[]”
	public Object pic_urls;
	
	// 微博作者的用户信息字段
	public User user;
	public class User {
		// 用户UID
		public long id;
		// 字符串型的用户UID
		public String idstr;
		// 用户昵称
		public String screen_name;
		// 友好显示名称
		public String name;
		// 用户所在省级ID
		public int province;
		// 用户所在城市ID
		public int city;
		// 用户所在地
		public String location;
		// 用户个人描述
		public String description;
		// 用户博客地址
		public String url;
		// 用户头像地址（中图）,50×50像素
		public String profile_image_url;
		// 用户的微博统一URL地址
		public String profile_url;
		// 用户的微号
		public String weihao;
		// 性别,m:男/f:女/n:未知
		public String gender;
		// 粉丝数
		public int followers_count;
		// 关注数
		public int friends_count;
		// 微博数
		public int statuses_count;
		// 收藏数
		public int favourites_count;
		// 用户创建（注册）时间
		public String created_at;
		// 用户头像地址（大图）,180×180像素
		public String avatar_large;
		// 用户头像地址（高清）,高清头像原图
		public String avatar_hd;
		// 认证原因
		public String verified_reason;
	}
	
	// 被转发的原微博信息字段,当该微博为转发微博时返回
	public FragmentHomeBean retweeted_status;
	
	// 转发数
	public int reposts_count;
	// 评论数
	public int comments_count;
}
