package com.mipos.adapters;

import java.util.ArrayList;

import com.mipos.activities.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class PaymentItemsAdapter extends BaseAdapter {
	String[] items;
	ArrayList<String> listItems;
	Activity activity;

	public PaymentItemsAdapter(Context context, String[] item, Activity activity) {
		this.items = item;
		this.activity = activity;
	}

	public PaymentItemsAdapter(Activity activity2, ArrayList<String> list1,
			Activity activity3) {
		this.listItems = list1;
		this.activity = activity3;
	}

	// @Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_payment_types, null);
		} 
		CheckedTextView post = (CheckedTextView) v.findViewById(R.id.sales_payments_checkedTextView_way_of_payment_item);
		post.setText(listItems.get(position));
		
		v.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

            	CheckedTextView selection = (CheckedTextView) v.findViewById(R.id.sales_payments_checkedTextView_way_of_payment_item);
            	
            	selection.setChecked(!selection.isChecked());

            }
        });
	
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

