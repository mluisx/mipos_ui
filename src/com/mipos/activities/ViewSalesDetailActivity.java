package com.mipos.activities;

import java.util.Calendar;

import com.mipos.pojos.DateSelection;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewSalesDetailActivity extends ListActivity {
	
	TextView textDate;
	ListView listOfSales;
	ListActivity viewSalesDetailActivity;
	
    final String[] GENRES = new String[] { "Efectivo", "Tarjeta de Crédito", "Dispositivo NFC",
    		"Paypal", "MercadoPago" };
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_sales_detail);
	
		textDate = (TextView) findViewById(R.id.view_sales_detail_textView);
		listOfSales = getListView();
		viewSalesDetailActivity = this;
			
		listOfSales.setChoiceMode(ListView.CHOICE_MODE_NONE);
		listOfSales.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, GENRES));

		listOfSales.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {	

			}
		});
	}
	
	@Override
	protected void onResume() {
	    if (getIntent().hasExtra("dateSelected")) {
	    	DateSelection selectedDate = (DateSelection) getIntent().getSerializableExtra("dateSelected");
	    	textDate.setText("Listado de Ventas del día " + selectedDate.getDate());
	    }
	    super.onResume();
	}
}
