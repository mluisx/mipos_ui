package com.mipos.activities;

import com.mipos.pojos.CreditCard;
import com.mipos.pojos.Sale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SalesCreditCardCheckoutActivity extends Activity {

	Bundle extras;
	EditText holdersName, cardNumber, cardExpirationDate, cardCvc;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_credit_card_checkout);
        
		Button addNewClientButton = (Button) findViewById(R.id.sales_begin_credit_card_checkout_process_button);
		holdersName = (EditText) findViewById(R.id.sales_credit_card_checkout_holders_name_editText);
		cardNumber = (EditText) findViewById(R.id.sales_credit_card_checkout_card_number_editText);
		cardExpirationDate = (EditText) findViewById(R.id.sales_credit_card_checkout_expiration_date_editText);
		cardCvc = (EditText) findViewById(R.id.sales_credit_card_checkout_cvc_editText);
		extras = getIntent().getExtras();
		
		addNewClientButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				openSignatureActivity();
			}

		});
	}  
	
	public void openSignatureActivity() {
		Sale saleToSend = null;
		Intent intent = new Intent();
		CreditCard creditCardData = new CreditCard(holdersName.getText().toString(), cardNumber.getText().toString(), 
				cardExpirationDate.getText().toString(), cardCvc.getText().toString());
		intent.setClass(getBaseContext(), SalesSignatureActivity.class);
		if (extras != null) {
			saleToSend = (Sale) extras.getSerializable("SaleData");
			saleToSend.setCreditCard(creditCardData);
		}
		intent.putExtra("SaleData", saleToSend);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
}
