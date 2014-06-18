package com.lm.weibo.android.views;

import net.tsz.afinal.FinalBitmap;
import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.lm.weibo.android.R;

public class ImgViewActivity extends Activity {
	private FinalBitmap fb;
	private Bitmap loadingBitmap;
	private Bitmap laodfailBitmap;
	private Resources res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home_listitem_img_dialog);
		fb = FinalBitmap.create(ImgViewActivity.this);

		Intent intent = getIntent();
		String url = intent.getStringExtra("imgurl");

		PhotoView img = (PhotoView) findViewById(R.id.img_photo);
//		if (!ImageLoader.getInstance().isInited()) {
//			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//					getApplicationContext()).build();
//			ImageLoader.getInstance().init(config);
//		}
//
//		ImageLoader.getInstance().displayImage(url, img);

		res = getResources();
		loadingBitmap = BitmapFactory.decodeResource(res,
				R.drawable.url_image_loading);
		laodfailBitmap = BitmapFactory.decodeResource(res,
				R.drawable.url_image_failed);

		fb.display(img, url);//, loadingBitmap, laodfailBitmap);
	}

	public void close(View v) {
		finish();
	}

}
