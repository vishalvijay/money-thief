package com.v4creations.moneythief.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v4creations.moneythief.R;
import com.v4creations.moneythief.utils.SystemFeatureChecker;

public class AboutFragment extends Fragment {

	private TextView versionTextView;

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);
		initViews(rootView);
		return rootView;
	}

	private void initViews(View rootView) {
		versionTextView = (TextView) rootView.findViewById(R.id.tvVersion);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		versionTextView.setText(getString(R.string.version,
				SystemFeatureChecker.getAppVersionName(getActivity())));
	}
}
