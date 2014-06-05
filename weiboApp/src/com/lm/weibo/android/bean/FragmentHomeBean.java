package com.lm.weibo.android.bean;

public class FragmentHomeBean {
	public String created_at;
	public long id;
	public String mid;
	public String idstr;
	public String text;
	public String source;
	
	public User user;
	
	public class User {
		public String id;
		public String idstr;
		public String screen_name;
		public String name;
		public String profile_image_url;
	}
}
