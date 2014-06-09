package com.lm.weibo.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String name = "sina_weibo.db";
	private static final int version = 1;

	public DBOpenHelper(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(EmotionTb.CREATE_EMOTION_TB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(EmotionTb.DROP_EMOTION_TB);
		onCreate(db);
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		return SQLiteDatabase.openDatabase(
				Environment.getExternalStorageDirectory() + "/" + name, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		return SQLiteDatabase.openDatabase(
				Environment.getExternalStorageDirectory() + "/" + name, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

}
