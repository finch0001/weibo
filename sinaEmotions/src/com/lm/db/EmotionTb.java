package com.lm.db;

public class EmotionTb {
	public static final String EMOTIONTB = "t_emotion";

	public static final String ID = "id";
	public static final String EMOTIONNAME = "emotionname";
	public static final String EMOTIONURL = "emotionurl";
	public static final String EMOTIONIMGNAME = "emotionimgname";

	public static final String CREATE_EMOTION_TB = "CREATE TABLE IF NOT EXISTS "
			+ EMOTIONTB
			+ " ("
			+ ID
			+ " integer primary key autoincrement, "
			+ EMOTIONNAME
			+ " varchar(20), "
			+ EMOTIONURL
			+ " varchar(200), "
			+ EMOTIONIMGNAME + " varchar(100))";

	public static final String DROP_EMOTION_TB = "DROP TABLE IF EXISTS "
			+ EMOTIONTB;

}
