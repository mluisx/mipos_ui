package com.mipos.adapters;

import com.mipos.activities.R;
import com.mipos.activities.R.id;
import com.mipos.activities.R.layout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class ItemsAdapter extends BaseAdapter {
	String[] items;
	Activity activity;

	public ItemsAdapter(Context context, String[] item, Activity activity) {
		this.items = item;
		this.activity = activity;
	}

	// @Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_categories, null);
		} 
		CheckedTextView post = (CheckedTextView) v.findViewById(R.id.list_checkedTextView);
		post.setText(items[position]);
		return v;
	}
	
	public int getCount() {
		return items.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public void toggle(CheckedTextView v)
	{
		if (v.isChecked())
		{
			v.setChecked(false);
		}
		else
		{
			v.setChecked(true);
		}
	}
}
