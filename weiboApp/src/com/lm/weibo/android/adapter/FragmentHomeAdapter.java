package com.lm.weibo.android.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lm.weibo.android.R;
import com.lm.weibo.android.bean.FragmentHomeBean;
import com.lm.weibo.android.utils.Util;

public class FragmentHomeAdapter extends BaseAdapter {
	private static final String TAG = "FragmentHomeAdapter";
	private Context context;
	private ArrayList<FragmentHomeBean> list;
	
	private FinalBitmap fb;
	
	public FragmentHomeAdapter(Context context, ArrayList<FragmentHomeBean> list) {
		this.context = context;
		this.list = list;
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
			convertView = inflater.inflate(R.layout.fragment_home_listitem, null);
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
			ViewHolder holder = new ViewHolder(home_avatar, home_name, home_sendfrom, home_content, home_img);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		fb.display(holder.home_avatar, list.get(position).user.profile_image_url);
		holder.home_name.setText(list.get(position).user.screen_name);
		holder.home_sendfrom.setText(Util.toNormalTime(list.get(position).created_at));
		holder.home_content.setText(list.get(position).text);
		
		if (Util.isValidate(list.get(position).thumbnail_pic)) {
			fb.display(holder.home_img, list.get(position).thumbnail_pic);
			holder.home_img.setVisibility(View.VISIBLE);
			holder.home_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(context, R.style.ImgDialog);
					ImageView img = new ImageView(context);
					LayoutParams params = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					if (Util.isValidate(list.get(position).original_pic)) {
						Log.d(TAG, "original_pic");
						fb.display(img, list.get(position).original_pic);
						dialog.addContentView(img, params);
					} else if (Util.isValidate(list.get(position).bmiddle_pic)) {
						Log.d(TAG, "bmiddle_pic");
						fb.display(img, list.get(position).bmiddle_pic);
						dialog.addContentView(img, params);
					} else {
						Log.d(TAG, "thumbnail_pic");
						fb.display(img, list.get(position).thumbnail_pic);
						dialog.addContentView(img, params);
					}
					img.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					dialog.show();
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

}
