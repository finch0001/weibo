package com.lm.weibo.android.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lm.weibo.android.R;
import com.lm.weibo.android.bean.FragmentHomeBean;
import com.lm.weibo.android.utils.TextUtil;

public class FragmentHomeAdapter extends BaseAdapter {
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
	public View getView(int position, View convertView, ViewGroup parent) {
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
//		holder.home_avatar.setImageBitmap(list.get(position).user.profile_image_url);
		holder.home_name.setText(list.get(position).user.screen_name);
		holder.home_sendfrom.setText(TextUtil.toNormalTime(list.get(position).created_at));
		holder.home_content.setText(list.get(position).text);
//		holder.home_img.setImageBitmap(bm);
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
