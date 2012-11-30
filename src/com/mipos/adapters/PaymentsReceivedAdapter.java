package com.mipos.adapters;

import java.util.ArrayList;

import com.mipos.activities.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class PaymentsReceivedAdapter extends BaseAdapter {
	String[] items;
	ArrayList<String> listItems;
	Activity activity;

	public PaymentsReceivedAdapter(Context context, String[] item, Activity activity) {
		this.items = item;
		this.activity = activity;
	}

	public PaymentsReceivedAdapter(Activity activity2, ArrayList<String> list1,
			Activity activity3) {
		this.listItems = list1;
		this.activity = activity3;
	}

	// @Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_payments_received, null);
		} 
		CheckedTextView post = (CheckedTextView) v.findViewById(R.id.sales_payments_payment_received_item);
		post.setText(listItems.get(position));
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


