package com.mipos.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.mipos.activities.R;
import com.mipos.pojos.ClientHistory;

public class ClientHistoryItemsAdapter extends BaseAdapter {
	
	String[] items;
	ArrayList<String> listItems = new ArrayList<String>();
	Activity activity;

	public ClientHistoryItemsAdapter(Context context, String[] item, Activity activity) {
		this.items = item;
		this.activity = activity;
	}

	public ClientHistoryItemsAdapter(Context context, ArrayList<ClientHistory> list1, Activity activity) {
		for (ClientHistory clientHistory : list1) {
			this.listItems.add(clientHistory.toString());
		}
		this.activity = activity;
	}
	
	public ClientHistoryItemsAdapter(Activity activity2, ArrayList<String> list1,
			Activity activity3) {
		this.listItems = list1;
		this.activity = activity3;
	}

	// @Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_clients_history, null);
		} 
		TextView post = (TextView) v.findViewById(R.id.clients_list_history_item_textView);
		post.setText((CharSequence) listItems.get(position));
		return v;
	}

	public int getCount() {
		return listItems.size();
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
