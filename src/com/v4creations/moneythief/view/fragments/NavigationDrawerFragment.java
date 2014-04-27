package com.v4creations.moneythief.view.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.v4creations.moneythief.R;
import com.v4creations.moneythief.controllers.adapters.MenuListAdapter;
import com.v4creations.moneythief.controllers.adapters.MenuListAdapter.NavMenuItem;
import com.v4creations.moneythief.utils.SystemFeatureChecker;
import com.v4creations.moneythief.view.activities.MoneyThiefMainActivity;

public class NavigationDrawerFragment extends Fragment {
	private NavigationDrawerCallbacks mCallbacks;
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;
	private MoneyThiefMainActivity activity;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = (MoneyThiefMainActivity) getActivity();
		mDrawerListView = (ListView) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);
		mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						selectItem(position);
					}
				});
		Context context = getActionBar().getThemedContext();
		ArrayList<NavMenuItem> navMenuItems = new ArrayList<NavMenuItem>();
		NavMenuItem navMenuItem = new NavMenuItem(" "
				+ context.getString(R.string.home),
				R.drawable.ic_action_view_as_grid);
		navMenuItems.add(navMenuItem);
		navMenuItem = new NavMenuItem(" "
				+ context.getString(R.string.about_me),
				R.drawable.ic_action_person);
		navMenuItems.add(navMenuItem);
		navMenuItem = new NavMenuItem(" " + context.getString(R.string.about),
				R.drawable.ic_action_about);
		navMenuItems.add(navMenuItem);
		mDrawerListView.setAdapter(new MenuListAdapter(context, navMenuItems));
		return mDrawerListView;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
				R.drawable.ic_navigation_drawer,
				R.string.navigation_drawer_open,
				R.string.navigation_drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}
				getActivity().supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}
				getActivity().supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		activity.getSupportFragmentManager().addOnBackStackChangedListener(
				new FragmentManager.OnBackStackChangedListener() {
					@Override
					public void onBackStackChanged() {
						setActionBarArrowDependingOnFragmentsBackStack();
					}
				});
	}

	private void selectItem(int position) {
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.isDrawerIndicatorEnabled()
				&& mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		} else if (item.getItemId() == android.R.id.home
				&& activity.getSupportFragmentManager().popBackStackImmediate()) {
			return true;
		} else if (item.getItemId() == R.id.menu_rate) {
			SystemFeatureChecker.rateAppOnPlayStore(activity);
			return true;
		} else if (item.getItemId() == R.id.menu_feedback) {
			SystemFeatureChecker.sendFeedback(activity);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.menu);
	}

	private ActionBar getActionBar() {
		return activity.getSupportActionBar();
	}

	private void setActionBarArrowDependingOnFragmentsBackStack() {
		int backStackEntryCount = ((ActionBarActivity) getActivity())
				.getSupportFragmentManager().getBackStackEntryCount();
		mDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
	}

	public static interface NavigationDrawerCallbacks {
		void onNavigationDrawerItemSelected(int position);
	}

}
