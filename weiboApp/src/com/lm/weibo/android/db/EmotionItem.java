package com.lm.weibo.android.db;

public class EmotionItem {
	public String emotionname;
	public String emotionurl;
	public String emotionimgname;
	
	public EmotionItem() {}

	public EmotionItem(String emotionname, String emotionurl,
			String emotionimgname) {
		this.emotionname = emotionname;
		this.emotionurl = emotionurl;
		this.emotionimgname = emotionimgname;
	}
	
	public void setEmotionName(String name) {
		this.emotionname = name;
	}
	
	public String getEmotionName() {
		return emotionname;
	}
	
	public void setEmotionUrl(String url) {
		this.emotionurl = url;
	}
	
	public String getEmotionUrl() {
		return emotionurl;
	}
	
	public void setEmotionImgName(String imgname) {
		this.emotionimgname = imgname;
	}
	
	public String getEmotionImgName() {
		return emotionimgname;
	}
}
