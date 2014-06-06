package com.lm.weibo.android.views;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lm.weibo.android.R;
import com.lm.weibo.android.adapter.FragmentHomeAdapter;
import com.lm.weibo.android.bean.FragmentHomeBean;
import com.lm.weibo.android.net.JsonCallback;
import com.lm.weibo.android.net.Request;
import com.lm.weibo.android.net.Urls;

/**
 * 首页
 * 
 * @author liumeng
 */
public class FragmentHome extends FragmentList {
	private Context context;
	private FragmentHomeAdapter adapter;
	private ArrayList<FragmentHomeBean> resultlist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getActivity();
		resultlist = new ArrayList<FragmentHomeBean>();
		adapter = new FragmentHomeAdapter(context, resultlist);

		loadWeiboMsg(Urls.DEFAULT_COUNT, true, 0);
	}

	@Override
	void setAdapter(PullToRefreshListView listview) {
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(context, "TODO 跳转到新界面并详细展示微博信息", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	void refreshOptions(PullToRefreshListView listview) {
		if (mPullRefreshListView.isHeaderShown()) {
			// 下拉刷新
			loadWeiboMsg(Urls.DEFAULT_COUNT, true, resultlist.get(0).id);
		} else if (mPullRefreshListView.isFooterShown()) {
			// 上拉加载更多
			loadWeiboMsg(Urls.LOADMORE_COUNT, false,
					resultlist.get(resultlist.size() - 1).id);
		}
	}

	@Override
	View getConvertView(LayoutInflater inflater) {
		View convertView = inflater.inflate(R.layout.fragment_list, null);

		PullToRefreshListView mPullRefreshListView = (PullToRefreshListView) convertView
				.findViewById(R.id.pull_refresh_list);
		setListView(mPullRefreshListView);
		return convertView;
	}

	/**
	 * 获取微博信息并显示
	 * 
	 * @param count
	 *            要返回的微博数
	 * @param later
	 *            返回比当前ID更晚(新)的微博还是更早(旧)的微博
	 * @param id
	 *            微博ID
	 */
	private void loadWeiboMsg(int count, final boolean later, long id) {
		String url = Urls.url_home_timeline + mAccessToken.getToken();
		url += "&count=" + count;
		
		// 载入20条旧微博
		if (id != 0 && !later) {
			url += "&max_id=" + id;
		}
		// 载入新微博
		if (id != 0 && later) {
			url += "&since_id" + id;
		}
		
		Request request = new Request(url);
		request.setCallback(new JsonCallback<ArrayList<FragmentHomeBean>>() {
			@Override
			public void onFailure(Exception result) {
				result.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<FragmentHomeBean> result) {
				if (resultlist != null && resultlist.size() > 0) {
					if (later) {
						// TODO 对新获取的新微博做处理
						resultlist = result;
					} else {
						result.remove(0);
						resultlist.addAll(result);
					}
				} else {
					resultlist = result;
				}
				adapter.refresh(resultlist);
				mPullRefreshListView.onRefreshComplete();
			}
		}.setReturnType(new TypeToken<ArrayList<FragmentHomeBean>>() {
		}.getType()));
		request.execute();
	}
}
