package com.lm.weibo.android.views;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lm.weibo.android.R;
import com.lm.weibo.android.utils.AccessTokenKeeper;
import com.lm.weibo.android.utils.Util;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class WriteWeiboActivity extends Activity {
	private Button btn_send;
	private EditText edt_content;
	private Oauth2AccessToken mAccessToken;

	private static final int SEND_SUCCESS = 101;
	private static final int SEND_FAILED = 102;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SEND_SUCCESS:
				Toast.makeText(WriteWeiboActivity.this, "微博发送成功",
						Toast.LENGTH_SHORT).show();
				WriteWeiboActivity.this.finish();
				break;
			case SEND_FAILED:
				Toast.makeText(WriteWeiboActivity.this, "微博发送失败",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeweibo);

		mAccessToken = AccessTokenKeeper
				.readAccessToken(getApplicationContext());

		btn_send = (Button) findViewById(R.id.writeweibo_send);
		edt_content = (EditText) findViewById(R.id.writeweibo_edt);

		btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Util.isValidate(edt_content.getText().toString())) {
					new Thread() {
						@Override
						public void run() {
							super.run();
							String access_token = mAccessToken.getToken();
							sendWeiBoByPost(
									"https://api.weibo.com/2/statuses/update.json",
									access_token, edt_content.getText()
											.toString());
						}
					}.start();
				}
			}
		});
	}

	private void sendWeiBoByPost(String requesturl, String token, String status) {
		HttpPost httprequest = new HttpPost(requesturl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", token));
		params.add(new BasicNameValuePair("status", status));
		try {
			HttpEntity httpentity = new UrlEncodedFormEntity(params, "UTF-8");
			httprequest.setEntity(httpentity);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpresponse = httpclient.execute(httprequest);
			if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strresult = EntityUtils.toString(httpresponse
						.getEntity());
				handler.sendEmptyMessage(SEND_SUCCESS);
				Log.d("lm", "success");
			} else {
				handler.sendEmptyMessage(SEND_FAILED);
				Log.d("lm", "fail");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
