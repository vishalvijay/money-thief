package com.v4creations.moneythief.view.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.v4creations.moneythief.R;
import com.v4creations.moneythief.utils.GoogleAnalyticsManager;
import com.v4creations.moneythief.view.fragments.AboutFragment;
import com.v4creations.moneythief.view.fragments.AboutMeFragment;
import com.v4creations.moneythief.view.fragments.NavigationDrawerFragment;
import com.v4creations.moneythief.view.fragments.WebFragment;

public class MoneyThiefMainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	// private static final String STATE_WEB_FRAGMENT = "web_fragament";
	// private static final String STATE_ABOUT_ME_FRAGMENT =
	// "about_me_fragament";
	// private static final String STATE_ABOUT_FRAGMENT = "about_fragament";

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private WebFragment webFragment;

	// private AboutMeFragment aboutMeFragment;
	// private AboutFragment aboutFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_money_thief_main);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		initWebFragment(savedInstanceState);
	}

	private void initWebFragment(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			webFragment = new WebFragment();
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().add(R.id.container, webFragment)
					.commit();
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		switch (position) {
		case 0:
			webFragment.loadHome();
			break;
		case 1:
			showNextFragment(new AboutMeFragment());
			break;
		case 2:
			showNextFragment(new AboutFragment());
			break;
		}
	}

	private void showNextFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_left,
						R.anim.slide_out_right, R.anim.slide_in_right,
						R.anim.slide_out_left)
				.replace(R.id.container, fragment).addToBackStack(null)
				.commit();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(R.string.app_name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	public NavigationDrawerFragment getNavigationDrawerFragment() {
		return mNavigationDrawerFragment;
	}

	@Override
	protected void onStart() {
		super.onStart();
		GoogleAnalyticsManager.startGoogleAnalyticsForActivity(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		GoogleAnalyticsManager.stopGoogleAnalyticsForActivity(this);
	}

}
