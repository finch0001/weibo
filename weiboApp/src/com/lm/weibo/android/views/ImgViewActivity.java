package com.lm.weibo.android.views;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lm.weibo.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImgViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home_listitem_img_dialog);

		Intent intent = getIntent();
		String url = intent.getStringExtra("imgurl");

		PhotoView img = (PhotoView) findViewById(R.id.img_photo);
		if (!ImageLoader.getInstance().isInited()) {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					getApplicationContext()).build();
			ImageLoader.getInstance().init(config);
		}

		ImageLoader.getInstance().displayImage(url, img);
	}

	public void close(View v) {
		finish();
	}

}
