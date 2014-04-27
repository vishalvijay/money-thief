package com.v4creations.moneythief;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.v4creations.moneythief.MenuListAdapter.NavMenuItem;

public class MenuListAdapter extends ArrayAdapter<NavMenuItem> {

	public MenuListAdapter(Context context, List<NavMenuItem> navMenuItems) {
		super(context, android.R.layout.simple_list_item_1, navMenuItems);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = (TextView) super.getView(position, convertView,
				parent);
		textView.setCompoundDrawablesWithIntrinsicBounds(
				getItem(position).mIconRid, 0, 0, 0);
		int viewPadding = (int) getContext().getResources().getDimension(
				R.dimen.common_padding);
		textView.setPadding(viewPadding, viewPadding, viewPadding, viewPadding);
		return textView;
	}

	public static class NavMenuItem {
		public String mTitle;
		public int mIconRid;

		public NavMenuItem(String title, int iconRId) {
			mTitle = title;
			mIconRid = iconRId;
		}

		@Override
		public String toString() {
			return mTitle;
		}
	}
}
