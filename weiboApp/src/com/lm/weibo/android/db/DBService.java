package com.lm.weibo.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBService {
	private DBOpenHelper dbHelper;
	private SQLiteDatabase db;

	public DBService(Context context) {
		dbHelper = new DBOpenHelper(context);
		db = dbHelper.getWritableDatabase();
	}

	public void beginTransaction() {
		db.beginTransaction();
	}

	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}

	public void endTransaction() {
		db.endTransaction();
	}

	public void dropTable(String tablename) {
		db.execSQL("DROP TABLE IF EXISTS " + tablename);
	}

	public void close() {
		db.close();
		dbHelper.close();
	}

	public long insertEmotionTb(EmotionItem item) {
		ContentValues cv = new ContentValues();
		cv.put(EmotionTb.EMOTIONNAME, item.emotionname);
		cv.put(EmotionTb.EMOTIONURL, item.emotionurl);
		cv.put(EmotionTb.EMOTIONIMGNAME, item.emotionimgname);
		return db.replace(EmotionTb.EMOTIONTB, null, cv);
	}

	public EmotionItem findEmotionItem(String emotionname) {
		Cursor cursor = db.rawQuery(EmotionTb.FIND_EMOTION_BY_NAME,
				new String[] { emotionname });
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			EmotionItem item = new EmotionItem();
			item.emotionname = cursor.getString(1);
			item.emotionurl = cursor.getString(2);
			item.emotionimgname = cursor.getString(3);
			cursor.close();
			return item;
		} else {
			cursor.close();
			return null;
		}
	}

}
