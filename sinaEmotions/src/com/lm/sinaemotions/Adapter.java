package com.lm.sinaemotions;

import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter extends BaseAdapter {
	private ArrayList<Emotions> list;
	private Context context;

	public Adapter(Context context, ArrayList<Emotions> list) {
		this.context = context;
		this.list = list;
	}

	public void refresh(ArrayList<Emotions> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Emotions getItem(int position) {
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
			convertView = inflater.inflate(R.layout.emotions_item, null);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			ImageView img = (ImageView) convertView.findViewById(R.id.img);
			ViewHolder holder = new ViewHolder(name, img);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		String emotionname = list.get(position).value;
		String emotionurl = list.get(position).url;
		String emotionimgname = emotionurl.substring(emotionurl.lastIndexOf('/') + 1,
				emotionurl.length());
		holder.name.setText("name = " + emotionname + "\n url = " + emotionurl
				+ "\n imgname = " + emotionimgname);
		return convertView;
	}

	private class ViewHolder {
		public TextView name;
		public ImageView img;

		ViewHolder(TextView name, ImageView img) {
			this.name = name;
			this.img = img;
		}
	}
}
