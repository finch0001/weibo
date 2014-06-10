package com.lm.weibo.android.views;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lm.weibo.android.R;
import com.lm.weibo.android.bean.FragmentHomeBean;
import com.lm.weibo.android.net.AppException;
import com.lm.weibo.android.net.Request;
import com.lm.weibo.android.net.Request.RequestMethod;
import com.lm.weibo.android.net.Request.RequestTool;
import com.lm.weibo.android.net.Urls;
import com.lm.weibo.android.net.callback.JsonCallback;
import com.lm.weibo.android.utils.AccessTokenKeeper;
import com.lm.weibo.android.utils.Util;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class WeiboDetailActivity extends Activity {
	private long id;
	private Oauth2AccessToken mAccessToken;
	private Context context;
	private FinalBitmap fb;

	private View wd_weibomessage;
	private RelativeLayout wd_userinfo;
	private ImageView wd_user_avatar;
	private TextView wd_user_name, wd_user_sendfrom;
	private TextView wd_content;
	private ImageView wd_content_img;

	private FrameLayout wd_weibomessage_retweeted_layout;
	private View wd_weibomessage_retweeted;
	private TextView wd_retweeted_content;
	private ImageView wd_retweeted_content_img;

	private TextView wd_reposts_count, wd_comments_count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weidodetail);
		context = WeiboDetailActivity.this;
		mAccessToken = AccessTokenKeeper.readAccessToken(context);

		initViews();

		fb = FinalBitmap.create(context);
		Intent intent = getIntent();
		id = intent.getLongExtra("id", 0);
		loadWeiboMsg(id);
	}

	private void initViews() {
		wd_weibomessage = findViewById(R.id.wd_weibomessage);
		wd_userinfo = (RelativeLayout) wd_weibomessage
				.findViewById(R.id.wd_userinfo);
		wd_userinfo.setVisibility(View.VISIBLE);
		wd_user_avatar = (ImageView) wd_weibomessage
				.findViewById(R.id.wd_user_avatar);
		wd_user_name = (TextView) wd_weibomessage
				.findViewById(R.id.wd_user_name);
		wd_user_sendfrom = (TextView) wd_weibomessage
				.findViewById(R.id.wd_user_sendfrom);
		wd_content = (TextView) wd_weibomessage.findViewById(R.id.wd_content);
		wd_content_img = (ImageView) wd_weibomessage
				.findViewById(R.id.wd_content_img);

		wd_weibomessage_retweeted_layout = (FrameLayout) findViewById(R.id.wd_weibomessage_retweeted_layout);
		wd_weibomessage_retweeted = findViewById(R.id.wd_weibomessage_retweeted);
		wd_retweeted_content = (TextView) wd_weibomessage_retweeted
				.findViewById(R.id.wd_content);
		wd_retweeted_content_img = (ImageView) wd_weibomessage_retweeted
				.findViewById(R.id.wd_content_img);

		wd_reposts_count = (TextView) findViewById(R.id.wd_reposts_count);
		wd_comments_count = (TextView) findViewById(R.id.wd_comments_count);
	}

	private void loadWeiboMsg(long id) {
		String url = Urls.url_show + mAccessToken.getToken();
		url += "&id=" + id;

		Request request = new Request(url, RequestMethod.GET,
				RequestTool.HTTPCLIENT);
		request.setCallback(new JsonCallback<FragmentHomeBean>() {
			@Override
			public void onSuccess(FragmentHomeBean result) {
				wd_userinfo.setVisibility(View.VISIBLE);
				fb.display(wd_user_avatar, result.user.profile_image_url);
				wd_user_name.setText(result.user.screen_name);
				wd_user_sendfrom.setText(Util.toNormalTime(result.created_at));
				wd_content.setText(result.text);

				if (Util.isValidate(result.original_pic)) {
					fb.display(wd_content_img, result.original_pic);
					wd_content_img.setVisibility(View.VISIBLE);
				}

				if (result.retweeted_status != null) {
					wd_weibomessage_retweeted_layout.setVisibility(View.VISIBLE);
					FragmentHomeBean retweeted = result.retweeted_status;
					wd_retweeted_content.setText(retweeted.text);
					if (Util.isValidate(retweeted.original_pic)) {
						fb.display(wd_retweeted_content_img,
								retweeted.original_pic);
						wd_retweeted_content_img.setVisibility(View.VISIBLE);
					}
				}

				wd_reposts_count.setText("转发数  " + result.reposts_count);
				wd_comments_count.setText("评论数  " + result.comments_count);
			}

			@Override
			protected String onJsonPreHandle(String content) {
				return content;
			}

			@Override
			public void onFailure(AppException result) {
				result.printStackTrace();
			}
		}.setReturnType(new TypeToken<FragmentHomeBean>() {
		}.getType()));
		request.execute();
	}
}
