package com.mipos.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CashCloseActivity extends Activity {

	TextView dateCash, totalAmount;
	EditText totalCash, totalCreditCard, totalOtherPayments;
	Button closeCash;
	Activity cashCloseActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cash_close);
		
		closeCash = (Button) findViewById(R.id.cash_close_action_button);
		totalCash = (EditText) findViewById(R.id.cash_close_total_cash_editText);
		totalCreditCard = (EditText) findViewById(R.id.cash_close_total_credit_card_editText);
		totalOtherPayments = (EditText) findViewById(R.id.cash_close_total_other_payments_editText);
		dateCash = (TextView) findViewById(R.id.cash_close_date_textView);
		totalAmount = (TextView) findViewById(R.id.cash_close_total_amount_textView);
		
	}

}
