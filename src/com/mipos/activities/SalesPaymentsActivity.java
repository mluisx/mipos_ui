package com.mipos.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.mipos.adapters.CartItemsAdapter;
import com.mipos.adapters.PaymentItemsAdapter;
import com.mipos.adapters.PaymentsReceivedAdapter;
import com.mipos.pojos.Client;
import com.mipos.pojos.PaymentMethod;
import com.mipos.pojos.Sale;
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
	PaymentItemsAdapter adapter;
	PaymentsReceivedAdapter paymentsReceivedAdapter;
	ListView listViewPaymentsReceived, listViewWayOfPayments;
	ArrayList<PaymentMethod> paymentMethodList = new ArrayList<PaymentMethod>();
	Activity activity;
    final String[] GENRES = new String[] { "Efectivo", "Tarjeta de Crédito", "Dispositivo NFC",
    		"Paypal", "MercadoPago" };
    Bundle extras;
    Client clientSelected = null;
	
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
		listViewWayOfPayments = getListView();
        listViewPaymentsReceived = (ListView) findViewById(R.id.sale_payments_received_payment_listView);
        activity = this;
        
		extras = getIntent().getExtras();
		
		if (extras != null) {
			totalAmountText.setText("Total de Venta $" + ((Sale) extras.getSerializable("SaleData")).
					getTotalCartAmount().toString());
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
				openNextActivity();
			}

		});
		              
	       listViewWayOfPayments.setItemsCanFocus(false);
	       listViewWayOfPayments.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

//	       adapter = new PaymentItemsAdapter(this, list1, this);
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
	       
	    ArrayList<String> list1 = new ArrayList<String>();   
	    paymentsReceivedAdapter = new PaymentsReceivedAdapter(this, list1, this);	       
	    listViewPaymentsReceived.setChoiceMode(ListView.CHOICE_MODE_NONE);
	    listViewPaymentsReceived.setAdapter(paymentsReceivedAdapter);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == SALES_PAYMENTS_ACTIVITY_ID) {
	    if (data.hasExtra("clientSelected")) {
	    	clientSelected = (Client) data.getSerializableExtra("clientSelected");
	    	clientName.setText(clientSelected.getName());
	    }
	  }
	} 
        
    public void openCreditCardCheckoutActivity() {
 	   Intent intent = new Intent();
 	   Sale saleToSend = (Sale) extras.getSerializable("SaleData");
 	   saleToSend.setPaymentMethodList(paymentMethodList);
 	   if (clientSelected!=null) {
 		  saleToSend.setClient(clientSelected);
 	   }
	   intent.putExtra("SaleData", saleToSend);
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
		ArrayList<String> list1 = new ArrayList<String>();
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setPaymentType(GENRES[listViewWayOfPayments.getCheckedItemPosition()]);
		paymentMethod.setAmount(new BigDecimal(amountPaidEdit.getText().toString()));
		paymentMethodList.add(paymentMethod);
		for (int i=0 ; i<paymentMethodList.size() ; i++) {
			list1.add(paymentMethodList.get(i).getPaymentType() + " - $" + paymentMethodList.get(i).getAmount().toString());
		}
		paymentsReceivedAdapter = new PaymentsReceivedAdapter(activity, list1, activity);
		listViewPaymentsReceived.setAdapter(paymentsReceivedAdapter);
		listViewWayOfPayments.clearChoices();
		amountPaidEdit.setText("");
		amountPaidEdit.setHint(activity.getString(R.string.sale_payments_amount_paid_hint));
	}
	

	private void openNextActivity() {
		for (int i=0 ; i<paymentMethodList.size() ; i++) {
			PaymentMethod paymentMethod = paymentMethodList.get(i);
			if (paymentMethod.getPaymentType().equals(GENRES[2])) {
				openNfcActivity();
				break;
			} else if (paymentMethod.getPaymentType().equals(GENRES[1])) {
				openCreditCardCheckoutActivity();
				break;
			} else if (paymentMethod.getPaymentType().equals(GENRES[4])) {
				openMercadoPagoActivity();
				break;
			}	
		}		
	}
	
    public void openNfcActivity() {
  	   Intent intent = new Intent();
	   writeSaleDataIntoIntent(intent);
  	   intent.setClass(getBaseContext(), NfcActivity.class);
  	   startActivity(intent);
  	   this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
     }

    private void writeSaleDataIntoIntent(Intent intent) {
    	Sale saleToSend = (Sale) extras.getSerializable("SaleData");
    	if (clientSelected!=null) {
    		saleToSend.setClient(clientSelected);
    	}
    	saleToSend.setPaymentMethodList(paymentMethodList);
    	intent.putExtra("SaleData", saleToSend);
    }
    
    public void openMercadoPagoActivity() {
   	   Intent intent = new Intent();
 	   writeSaleDataIntoIntent(intent);
   	   intent.setClass(getBaseContext(), MercadoPagoActivity.class);
   	   startActivity(intent);
   	   this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
      }
}
