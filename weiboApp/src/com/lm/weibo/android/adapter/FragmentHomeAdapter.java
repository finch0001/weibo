package com.lm.weibo.android.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalBitmap;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lm.weibo.android.R;
import com.lm.weibo.android.bean.FragmentHomeBean;
import com.lm.weibo.android.db.DBService;
import com.lm.weibo.android.db.EmotionItem;
import com.lm.weibo.android.utils.Util;
import com.lm.weibo.android.views.ImgViewActivity;

public class FragmentHomeAdapter extends BaseAdapter {
	private static final String TAG = "FragmentHomeAdapter";
	private Context context;
	private DBService dbService;
	private ArrayList<FragmentHomeBean> list;

	private FinalBitmap fb;

	public FragmentHomeAdapter(Context context,
			ArrayList<FragmentHomeBean> list, DBService dbService) {
		this.context = context;
		this.list = list;
		this.dbService = dbService;
		fb = FinalBitmap.create(context);
	}

	public void refresh(ArrayList<FragmentHomeBean> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public FragmentHomeBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fragment_home_listitem,
					null);
			ImageView home_avatar = (ImageView) convertView
					.findViewById(R.id.home_avatar);
			TextView home_name = (TextView) convertView
					.findViewById(R.id.home_name);
			TextView home_sendfrom = (TextView) convertView
					.findViewById(R.id.home_sendfrom);
			TextView home_content = (TextView) convertView
					.findViewById(R.id.home_content);
			ImageView home_img = (ImageView) convertView
					.findViewById(R.id.home_img);
			ViewHolder holder = new ViewHolder(home_avatar, home_name,
					home_sendfrom, home_content, home_img);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		fb.display(holder.home_avatar,
				list.get(position).user.profile_image_url);
		holder.home_name.setText(list.get(position).user.screen_name);
		holder.home_sendfrom
				.setText(Util.toNormalTime(list.get(position).created_at));
		// holder.home_content.setText(list.get(position).text);
		setTextAndImg(holder.home_content, list.get(position).text);

		if (Util.isValidate(list.get(position).thumbnail_pic)) {
			fb.display(holder.home_img, list.get(position).thumbnail_pic);
			holder.home_img.setVisibility(View.VISIBLE);
			holder.home_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ImgViewActivity.class);
					String imgurl = "";
					if (Util.isValidate(list.get(position).original_pic)) {
						imgurl = list.get(position).original_pic;
					} else if (Util.isValidate(list.get(position).bmiddle_pic)) {
						imgurl = list.get(position).bmiddle_pic;
					} else {
						imgurl = list.get(position).thumbnail_pic;
					}
					intent.putExtra("imgurl", imgurl);
					context.startActivity(intent);
				}
			});
		} else {
			holder.home_img.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		public ImageView home_avatar;
		public TextView home_name;
		public TextView home_sendfrom;
		public TextView home_content;
		public ImageView home_img;

		ViewHolder(ImageView home_avatar, TextView home_name,
				TextView home_sendfrom, TextView home_content,
				ImageView home_img) {
			this.home_avatar = home_avatar;
			this.home_name = home_name;
			this.home_sendfrom = home_sendfrom;
			this.home_content = home_content;
			this.home_img = home_img;
		}
	}

	// TODO 需要优化的地方
	private void setTextAndImg(TextView view, String source) {
		SpannableString spanstr = new SpannableString(source);
		Pattern p = Pattern.compile("\\[.*?\\]");
		Matcher m = p.matcher(source);
		AssetManager assets = context.getAssets();
		while (m.find()) {
//			Log.d("ll", m.group() + " " + m.start() + " " + m.end());
			EmotionItem item = dbService.findEmotionItem(m.group());
			if (item != null) {
				InputStream assetFile = null;
				try {
					assetFile = assets.open("sina_emotions/"
							+ item.emotionimgname);
				} catch (IOException e) {
					e.printStackTrace();
				}
				ImageSpan img = new ImageSpan(
						BitmapFactory.decodeStream(assetFile));
				spanstr.setSpan(img, m.start(), m.end(),
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}

		view.setText(spanstr);
	}

}
