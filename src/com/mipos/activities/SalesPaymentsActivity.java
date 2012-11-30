package com.mipos.activities;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.mipos.adapters.CartItemsAdapter;
import com.mipos.adapters.PaymentItemsAdapter;
import com.mipos.adapters.PaymentsReceivedAdapter;
import com.mipos.utils.TransferBigDecimalObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SalesPaymentsActivity extends ListActivity {

	private static final int SALES_PAYMENTS_ACTIVITY_ID = 1;
	Button paymentButton, nextStep, addClient;
	EditText amountPaidEdit;
	TextView amountPaidText, clientName, totalAmountText, wayOfPaymentText;
	BigDecimal totalAmount;
	PaymentItemsAdapter adapter;
	PaymentsReceivedAdapter paymentsReceivedAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sales_payments);       

		paymentButton = (Button) findViewById(R.id.sale_payments_add_way_of_payment_button);
		nextStep = (Button) findViewById(R.id.sale_payments_next_step_button);
		addClient = (Button) findViewById(R.id.sale_payments_button_add_client);
		amountPaidEdit = (EditText) findViewById(R.id.sale_payments_amount_paid_editText);
		amountPaidText = (TextView) findViewById(R.id.sale_payments_amount_paid_textView);
		clientName = (TextView) findViewById(R.id.sale_payments_client_name_textView);
		totalAmountText = (TextView) findViewById(R.id.sale_payments_total_amount_textView);
		wayOfPaymentText = (TextView) findViewById(R.id.sale_payments_way_of_payment_textView);
        final ListView listViewWayOfPayments = getListView();
      //final ListView listViewPaymentsReceived = getListView();
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			totalAmount = ((TransferBigDecimalObject) extras.getSerializable("TotalAmount")).getNumber();
			totalAmountText.setText("Total de Venta $" + totalAmount.toString());
		}
	
		paymentButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				addPaymentToList();
			}

		});
		
		addClient.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				openClientsListActivity();
			}

		});
		
		nextStep.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				openCreditCardCheckoutActivity();
			}

		});
		
	       ArrayList<String> list1 = new ArrayList<String>();
	       list1.add("Efectivo");
	       list1.add("Tarjeta de Crédito");
	       list1.add("Dispositivo NFC");
	       list1.add("Paypal");
	       list1.add("MercadoPago");

	       final String[] GENRES = new String[] {
		    	   "Efectivo", "Tarjeta de Crédito", "Dispositivo NFC", "Paypal", "MercadoPago"
		       };
		              
	       listViewWayOfPayments.setItemsCanFocus(false);
	       listViewWayOfPayments.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	       adapter = new PaymentItemsAdapter(this, list1, this);
	       listViewWayOfPayments.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//	       listViewWayOfPayments.setAdapter(adapter);
	       listViewWayOfPayments.setAdapter(new ArrayAdapter<String>(this,
	    		   android.R.layout.simple_list_item_single_choice, GENRES));
	 	   
//	 	   setListAdapter(new ArrayAdapter<String>(this, R.layout.list_categories, PRODUCT_CATEGORIES));

	       listViewWayOfPayments.setTextFilterEnabled(true);
	       listViewWayOfPayments.setClickable(true);
	       listViewWayOfPayments.setOnItemClickListener(new OnItemClickListener() {

	    	   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	    			   long arg3) {	
	    		  
	    	   }

	 	   });
	       
	  //     paymentsReceivedAdapter = new PaymentsReceivedAdapter(this, list1, this);	       
	    //   listViewPaymentsReceived.setChoiceMode(ListView.CHOICE_MODE_NONE);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == SALES_PAYMENTS_ACTIVITY_ID) {
	    if (data.hasExtra("clientName")) {
	    	clientName.setText(data.getStringExtra("clientName"));
	    }
	  }
	} 
        
    public void openCreditCardCheckoutActivity() {
 	   Intent intent = new Intent();
 	   intent.setClass(getBaseContext(), SalesCreditCardCheckoutActivity.class);
 	   startActivity(intent);
 	   this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
    }
    
	public void openClientsListActivity() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), ClientsListActivity.class);
		intent.putExtra("ParentActivity","Sales");
		startActivityForResult(intent, SALES_PAYMENTS_ACTIVITY_ID);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
	private void addPaymentToList() {
		// TODO Auto-generated method stub	
	}
	
}
