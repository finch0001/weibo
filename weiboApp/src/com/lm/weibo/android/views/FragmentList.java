package com.lm.weibo.android.views;

import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.lm.weibo.android.R;
import com.lm.weibo.android.utils.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public abstract class FragmentList extends Fragment implements
		OnRefreshListener<ListView> {
	private boolean mRefreshSound = false;

	protected Oauth2AccessToken mAccessToken;
	protected PullToRefreshListView mPullRefreshListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = getConvertView(inflater);
		mPullRefreshListView = getListView();

		mPullRefreshListView.setMode(Mode.BOTH);
		// 上拉时提示文字
		mPullRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.pull_down_load));
		// 下拉时提示文字
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_up_load));
		mPullRefreshListView.getLoadingLayoutProxy().setRefreshingLabel(getString(R.string.loading));
		mPullRefreshListView.getLoadingLayoutProxy().setReleaseLabel(getString(R.string.release_to_load));  
		mPullRefreshListView.setOnRefreshListener(this);

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						Toast.makeText(getActivity(), R.string.pullup_loadmore,
								Toast.LENGTH_SHORT).show();
					}
				});

		/**
		 * Add Sound Event Listener
		 */
		if (mRefreshSound) {
			SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(getActivity());
			soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
			soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
			soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
			mPullRefreshListView.setOnPullEventListener(soundListener);
		}

		setAdapter(mPullRefreshListView);

		return convertView;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		
		refreshOptions(mPullRefreshListView);
	}

	abstract View getConvertView(LayoutInflater inflater);

	abstract void setAdapter(PullToRefreshListView listview);

	abstract void refreshOptions(PullToRefreshListView listview);

	public FragmentList setListView(View view) {
		this.mPullRefreshListView = (PullToRefreshListView) view;
		return this;
	}

	public PullToRefreshListView getListView() {
		return mPullRefreshListView;
	}

}
