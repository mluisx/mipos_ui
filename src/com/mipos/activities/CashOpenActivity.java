package com.mipos.activities;

import java.math.BigDecimal;
import java.util.Date;
import com.mipos.asyncs.CashOpenAsync;
import com.mipos.pojos.Cash;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CashOpenActivity extends Activity {

	TextView date;
	EditText amount;
	Button openCash;
	Activity cashOpenActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cash_open);

		openCash = (Button) findViewById(R.id.cash_open_button);
		amount = (EditText) findViewById(R.id.cash_open_editText);
		date = (TextView) findViewById(R.id.cash_open_textView);
		date.setText(new Date().toString());
		cashOpenActivity = this;
		
		openCash.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(openCash.getApplicationWindowToken(), 0);
				CashOpenAsync cashOpenAsync = new CashOpenAsync(cashOpenActivity);
				Cash cash = new Cash();
				cash.setAmount(new BigDecimal(Double.parseDouble(amount.getText().toString())));
				cashOpenAsync.setCash(cash);
				cashOpenAsync.execute();
			}			
		});
	}

	public void cashSuccessfullyOpened() {
        Toast toast = Toast.makeText(this, "The Cash With $" + amount.getText().toString()
        		+" Was Opened", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
        toast.show();
        finish();
	} 
}
