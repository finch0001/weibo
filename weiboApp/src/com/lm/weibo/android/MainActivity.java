package com.lm.weibo.android;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lm.weibo.android.utils.Util;
import com.lm.weibo.android.views.FragmentHome;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	private int select_item_number = -1;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPixels();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        onNavigationDrawerItemSelected(0);
    }

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		onSectionAttached(position);
		if (select_item_number != position) {
			select_item_number = position;
			FragmentManager fragmentManager = getFragmentManager();
			switch (select_item_number) {
			case 0:
				fragmentManager.beginTransaction()
						.replace(R.id.container, new FragmentHome()).commit();
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			default:
				break;
			}
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 0:
			mTitle = getString(R.string.title_section1);
			break;
		case 1:
			mTitle = getString(R.string.title_section2);
			break;
		case 2:
			mTitle = getString(R.string.title_section3);
			break;
		case 3:
			mTitle = getString(R.string.title_section4);
			break;
		case 4:
			mTitle = getString(R.string.title_section5);
			break;
		default:
			mTitle = getString(R.string.app_name);
			break;
		}
	}

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_example:
			Toast.makeText(MainActivity.this, "Write weibo", Toast.LENGTH_SHORT).show();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getPixels() {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		Util.widthPixels = mDisplayMetrics.widthPixels;
		Util.heightPixels = mDisplayMetrics.heightPixels;
	}
}
