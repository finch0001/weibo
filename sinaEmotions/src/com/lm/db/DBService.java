package com.lm.db;

import android.content.ContentValues;
import android.content.Context;
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

	public long insertUserTb(EmotionItem item) {
		ContentValues cv = new ContentValues();
		cv.put(EmotionTb.EMOTIONNAME, item.emotionname);
		cv.put(EmotionTb.EMOTIONURL, item.emotionurl);
		cv.put(EmotionTb.EMOTIONIMGNAME, item.emotionimgname);
		return db.replace(EmotionTb.EMOTIONTB, null, cv);
	}

	/*
	public List<CaseItem> findCasesOrderByCount(String[] scoreids) {
		List<CaseItem> caselist = new ArrayList<CaseItem>();
		StringBuffer sb = new StringBuffer();
		for (int i = scoreids.length; i >= 0; i--) {
			sb.append('?');
			if ((i - 1) >= 0) {
				sb.append(',');
			}
		}
		Cursor cursor = db.rawQuery(CaseTb.FIND_CASE_ORDERBY_COUNTORTIME
				+ " where scoreId in(" + sb.toString() + ")" + " and "
				+ CaseTb.COUNT + " > 0" + " order by " + CaseTb.COUNT
				+ " desc limit 5", scoreids);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				CaseItem caseitem = new CaseItem();
				caseitem.caseId = cursor.getLong(0);
				caseitem.caseName = cursor.getString(1);
				caseitem.caseGroupId = cursor.getLong(2);
				caseitem.scoreId = cursor.getString(3);
				caseitem.casedir = cursor.getString(4);
				caselist.add(caseitem);
			} while (cursor.moveToNext());
			cursor.close();
			return caselist;
		} else {
			cursor.close();
			return null;
		}
	}
	*/

}
