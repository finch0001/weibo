package com.lm.sinaemotions;

import java.io.File;
import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lm.db.DBService;
import com.lm.db.EmotionItem;
import com.lm.net.JsonCallback;
import com.lm.net.Request;
import com.lm.net.Urls;

public class MainActivity extends Activity {
	private ListView list;
	private Button btn;
	private ArrayList<Emotions> emotions;
	private Adapter adapter;
	private DBService dbService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		dbService = new DBService(getApplicationContext());

		emotions = new ArrayList<Emotions>();
		adapter = new Adapter(getApplicationContext(), emotions);

		btn = (Button) findViewById(R.id.btn);
		list = (ListView) findViewById(R.id.list);

		list.setAdapter(adapter);

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Request request = new Request(Urls.url_emotions);
				request.setCallback(new JsonCallback<ArrayList<Emotions>>() {
					@Override
					public void onFailure(Exception result) {
						result.printStackTrace();
					}

					@Override
					public void onSuccess(final ArrayList<Emotions> result) {
						adapter.refresh(result);
						
						/*
						FinalHttp fh = new FinalHttp();
						String path = Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "sina_emotions"
								+ File.separator;
						
						for (Emotions emotion : result) {
							final String emotionurl = emotion.url;
							String emotionimgname = emotionurl.substring(
									emotionurl.lastIndexOf('/') + 1,
									emotionurl.length());

							HttpHandler handler = fh.download(
									emotionurl, path + emotionimgname,
									new AjaxCallBack<File>() {
										@Override
										public void onFailure(Throwable t,
												int errorNo, String strMsg) {
											super.onFailure(t, errorNo, strMsg);
											Log.d("ll", "failure");
										}

										@Override
										public void onStart() {
											super.onStart();
											Log.d("ll", "start --> " + emotionurl);
										}

										@Override
										public void onSuccess(File t) {
											super.onSuccess(t);
											Log.d("ll", "success");
										}
									});
						}
						*/
						
						new Thread() {
							@Override
							public void run() {
								super.run();
								dbService.beginTransaction();
								for (Emotions emotion : result) {
									String emotionname = emotion.value;
									String emotionurl = emotion.url;
									String emotionimgname = emotionurl.substring(
											emotionurl.lastIndexOf('/') + 1,
											emotionurl.length());

									EmotionItem item = new EmotionItem(
											emotionname, emotionurl,
											emotionimgname);
									dbService.insertUserTb(item);
								}
								dbService.setTransactionSuccessful();
								dbService.endTransaction();
								dbService.close();
							}
						}.start();
					}
				}.setReturnType(new TypeToken<ArrayList<Emotions>>() {
				}.getType()));
				request.execute();
			}
		});
	}
}
