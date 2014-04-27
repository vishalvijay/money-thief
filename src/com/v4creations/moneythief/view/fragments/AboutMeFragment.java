package com.v4creations.moneythief.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v4creations.moneythief.R;

public class AboutMeFragment extends Fragment {

	public AboutMeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about_me, container,
				false);
		initViews(rootView);
		return rootView;
	}

	private void initViews(View rootView) {
		TextView textView = (TextView) rootView
				.findViewById(R.id.tvAboutAuthor);
		textView.setMovementMethod(new ScrollingMovementMethod());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

}
