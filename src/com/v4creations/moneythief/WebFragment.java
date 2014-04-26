package com.v4creations.moneythief;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebFragment extends Fragment {

	String TAG = "WebFragment";
	private WebView mWebView;
	private boolean isForwardEnabled = false, isBackEnabled = true,
			isRefresh = true;
	private ProgressBar loadingProgressBar;
	private TextView progressTextView;
	private String currentUrl;
	private MoneyThiefMainActivity activity;

	public WebFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentUrl = Constance.WEB_URL;
		View rootView = inflater.inflate(R.layout.fragment_web, container,
				false);
		initViews(rootView);
		activity = (MoneyThiefMainActivity) getActivity();
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mWebView.loadUrl(currentUrl);
	}

	private void initViews(View rootView) {
		loadingProgressBar = (ProgressBar) rootView
				.findViewById(R.id.loadingProgressBar);
		progressTextView = (TextView) rootView
				.findViewById(R.id.progressTextView);
		hideLodingProgressBar();
		mWebView = (WebView) rootView.findViewById(R.id.siteWebView);
		initWebView();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initWebView() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new JavaScriptInterface(), "HTMLOUT");

		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				loadingProgressBar.setProgress(progress);
			}
		});

		mWebView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Utils.showInfoToast(getActivity(), description);
				showReload();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				isRefresh = false;
				isBackEnabled = mWebView.canGoBack();
				isForwardEnabled = mWebView.canGoForward();
				changeSubTitle(getString(R.string.loading));
				showLoadingProgressBar();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				currentUrl = url;
				mWebView.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
				isRefresh = true;
				isBackEnabled = mWebView.canGoBack();
				isForwardEnabled = mWebView.canGoForward();
				changeSubTitle(null);
				hideLodingProgressBar();
			}
		});
		mWebView.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				getActivity().startActivity(i);
				Utils.showInfoToast(getActivity(), R.string.downloading_started);
			}
		});
	}

	class JavaScriptInterface {
		public void showHTML(String html) {
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.web_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (!activity.getNavigationDrawerFragment().isDrawerOpen()) {
			MenuItem forwardMenuItem = menu.findItem(R.id.menu_forward);
			MenuItem backMenuItem = menu.findItem(R.id.menu_back);
			MenuItem refreshMenuItem = menu.findItem(R.id.menu_refresh);
			if (isForwardEnabled) {
				forwardMenuItem.setEnabled(true);
				forwardMenuItem.setIcon(R.drawable.ic_action_next_item);

			} else {
				forwardMenuItem.setEnabled(false);
				forwardMenuItem
						.setIcon(R.drawable.ic_action_next_item_disabled);
			}
			if (isBackEnabled) {
				backMenuItem.setEnabled(true);
				backMenuItem.setIcon(R.drawable.ic_action_previous_item);
			} else {
				backMenuItem.setEnabled(false);
				backMenuItem
						.setIcon(R.drawable.ic_action_previous_item_disabled);
			}
			if (isRefresh)
				refreshMenuItem.setIcon(R.drawable.ic_action_refresh);
			else
				refreshMenuItem.setIcon(R.drawable.ic_action_cancel);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_back:
			mWebView.goBack();
			return true;
		case R.id.menu_forward:
			mWebView.goForward();
			return true;
		case R.id.menu_refresh:
			if (isRefresh)
				mWebView.reload();
			else {
				isRefresh = true;
				activity.supportInvalidateOptionsMenu();
				mWebView.stopLoading();
			}
			return true;
		}
		return false;
	}

	private void hideLodingProgressBar() {
		loadingProgressBar.setVisibility(View.GONE);
	}

	private void showLoadingProgressBar() {
		loadingProgressBar.setProgress(0);
		loadingProgressBar.setVisibility(View.VISIBLE);
		progressTextView.setVisibility(View.GONE);
	}

	private void showReload() {
		hideLodingProgressBar();
		progressTextView.setVisibility(View.VISIBLE);
	}

	private void changeSubTitle(String subTitle) {
		activity.getSupportActionBar().setSubtitle(subTitle);
		activity.supportInvalidateOptionsMenu();
	}
}
