package com.mipos.activities;

import java.util.Calendar;

import com.mipos.pojos.DateSelection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class ViewSalesActivity extends Activity {
	
	Activity activity;
	CalendarView calendar;
	Button currentCash;
	private int mYear, mMonth, mDay;
	DateSelection selectedDate = new DateSelection();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_sales);
		
		currentCash = (Button) findViewById(R.id.view_sales_current_cash_button);
		calendar = (CalendarView) findViewById(R.id.view_sales_calendarView);
		calendar.setOnDateChangeListener(mDateSetListener);
			
		currentCash.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				openSalesList(selectedDate);			
			}

		});		
//		Bundle bundle = data.getExtras();
//		editT1.setText(bundle.getString("dateSelected"));
	}
	
	// the callback received when the user "sets" the date in the dialog
	private OnDateChangeListener mDateSetListener =
			new OnDateChangeListener() {
		public void onSelectedDayChange(CalendarView view, int year,
				int monthOfYear, int dayOfMonth) {
			if (mYear!=year || mMonth!=monthOfYear || mDay!=dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				selectDate();
				openSalesList(selectedDate);
			}
//			b = new Bundle();
//			b.putString("dateSelected", selectedDate);
			//Add the set of extended data to the intent and start it
//			Intent intent = new Intent();
//			intent.putExtras(b);
//			setResult(RESULT_OK,intent);      
//			finish();
		}
	};
	
	@Override
	protected void onResume() {
		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		selectDate();
		super.onResume();
	}

	private void selectDate() {
		selectedDate.setDate(new StringBuilder().append(mDay).append("-").append(mMonth + 1).append("-")
				.append(mYear).append(" ").toString());
		selectedDate.setYear(mYear);
		selectedDate.setMonth(mMonth);
		selectedDate.setDay(mDay);
	}
	
	private void openSalesList(DateSelection selectedDate) {
		Log.i("View Sales Activity", selectedDate.getDate());
		Intent intent = new Intent();
		intent.putExtra("dateSelected", selectedDate);
		intent.setClass(getBaseContext(), ViewSalesDetailActivity.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	

}
