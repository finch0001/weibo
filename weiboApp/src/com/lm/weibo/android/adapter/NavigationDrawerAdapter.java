package com.lm.weibo.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lm.weibo.android.R;
import com.lm.weibo.android.bean.NavigationDrawerItem;

public class NavigationDrawerAdapter extends BaseAdapter {
	private Context context;
	private List<NavigationDrawerItem> list;

	public NavigationDrawerAdapter(Context context, List<NavigationDrawerItem> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public NavigationDrawerItem getItem(int position) {
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
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem_navigationdrawer, null);
			ImageView img = (ImageView) convertView.findViewById(R.id.drawer_item_img);
			TextView name = (TextView) convertView.findViewById(R.id.drawer_item_text);
			convertView.setTag(new ViewHolder(img, name));
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.img.setImageDrawable(list.get(position).img);
		holder.name.setText(list.get(position).name);
		return convertView;
	}

	class ViewHolder {
		private ImageView img;
		private TextView name;
		
		ViewHolder(ImageView img, TextView name) {
			this.img = img;
			this.name = name;
		}
	}
}
