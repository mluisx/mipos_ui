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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SalesPaymentsActivity extends ListActivity {

	private static final int CLIENT_SELECT_ACTIVITY_ID = 1;
	private static final int NFC_ACTIVITY_ID = 2;
	private static final int CREDIT_CARD_ACTIVITY_ID = 3;
	private static final int MERCADO_PAGO_ACTIVITY_ID = 4;
	Button paymentButton, nextStep, addClient, deletePayment;
	EditText amountPaidEdit;
	TextView amountPaidText, clientName, totalAmountText, wayOfPaymentText, sellerText;
	PaymentItemsAdapter adapter;
	PaymentsReceivedAdapter paymentsReceivedAdapter;
	ListView listViewPaymentsReceived, listViewWayOfPayments, listViewSellers;
	ArrayList<PaymentMethod> paymentMethodList = new ArrayList<PaymentMethod>();
	ArrayList<String> paymentsListInUI = new ArrayList<String>();
	Activity activity;
    final String[] GENRES = new String[] { "Efectivo", "Tarjeta de Crédito", "Dispositivo NFC",
    		"Paypal", "MercadoPago" };
    final String[] SELLERS = new String[] { "Gabriel", "Thiago", "Lorena" };
    Bundle extras;
    Client clientSelected = null;
    BigDecimal totalAmount, leftToPay;
    boolean paymentsListViewChecked;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sales_payments);       

		paymentButton = (Button) findViewById(R.id.sale_payments_add_way_of_payment_button);
		nextStep = (Button) findViewById(R.id.sale_payments_next_step_button);
		addClient = (Button) findViewById(R.id.sale_payments_button_add_client);
		deletePayment = (Button) findViewById(R.id.sale_payments_delete_payment_button);
		amountPaidEdit = (EditText) findViewById(R.id.sale_payments_amount_paid_editText);
		amountPaidText = (TextView) findViewById(R.id.sale_payments_amount_paid_textView);
		clientName = (TextView) findViewById(R.id.sale_payments_client_name_textView);
		totalAmountText = (TextView) findViewById(R.id.sale_payments_total_amount_textView);
		wayOfPaymentText = (TextView) findViewById(R.id.sale_payments_way_of_payment_textView);
		listViewWayOfPayments = getListView();
        listViewPaymentsReceived = (ListView) findViewById(R.id.sale_payments_received_payment_listView);
        sellerText = (TextView) findViewById(R.id.sale_payments_seller_textView);
        listViewSellers = (ListView) findViewById(R.id.sale_payments_seller_listView);
        activity = this;
        deletePayment.setEnabled(false);
        paymentsListViewChecked = false;
        
		extras = getIntent().getExtras();
		
		if (extras != null) {
			totalAmount = ((Sale) extras.getSerializable("SaleData")).getTotalCartAmount();
			totalAmountText.setText("Total de Venta $" + totalAmount.toString());
			amountPaidEdit.setText(totalAmount.toString());
			leftToPay = new BigDecimal(totalAmount.toString());
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
		//	       adapter = new PaymentItemsAdapter(this, list1, this);
		listViewWayOfPayments.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//	       listViewWayOfPayments.setAdapter(adapter);
		listViewWayOfPayments.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, GENRES));

		//	 	   setListAdapter(new ArrayAdapter<String>(this, R.layout.list_categories, PRODUCT_CATEGORIES));

		listViewWayOfPayments.setTextFilterEnabled(true);
		listViewWayOfPayments.setClickable(true);
		
		deletePayment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				deletePayments(paymentMethodList, paymentsListInUI);
			}});

		listViewSellers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listViewSellers.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, SELLERS));

//		ArrayList<String> list1 = new ArrayList<String>();   
//		paymentsReceivedAdapter = new PaymentsReceivedAdapter(this, list1, this);	
		listViewPaymentsReceived.setAdapter(new ArrayAdapter<String>(this,
	    		android.R.layout.simple_list_item_single_choice, paymentsListInUI));
		listViewPaymentsReceived.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listViewPaymentsReceived.setAdapter(paymentsReceivedAdapter);
		
		listViewPaymentsReceived.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!paymentsListViewChecked) {
					paymentsListViewChecked = true;
				}
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == CLIENT_SELECT_ACTIVITY_ID) {
	    if (data.hasExtra("clientSelected")) {
	    	clientSelected = (Client) data.getSerializableExtra("clientSelected");
	    	clientName.setText(clientSelected.getName());
	    }
	  } else if (resultCode == RESULT_OK && (requestCode == NFC_ACTIVITY_ID ||
			  requestCode == CREDIT_CARD_ACTIVITY_ID || requestCode == MERCADO_PAGO_ACTIVITY_ID)) {
		  openTicketActivity(data);
	  } 
	}

	private void openTicketActivity(Intent data) {
		Intent intent = new Intent();
		  Bundle extras = data.getExtras();
		  intent.setClass(getBaseContext(), SalesTicketActivity.class);
		  if (extras != null) {
			  intent.putExtra("SaleData", (Sale) extras.getSerializable("SaleData"));
		  }
		  startActivity(intent);
		  this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	} 
            
	public void openClientsListActivity() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), ClientsListActivity.class);
		intent.putExtra("ParentActivity","Sales");
		startActivityForResult(intent, CLIENT_SELECT_ACTIVITY_ID);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
	private void addPaymentToList() {
		if (!amountPaidEdit.getText().toString().isEmpty()) {
			if (listViewWayOfPayments.getCheckedItemPosition()>=0) {
				BigDecimal amountReceived = new BigDecimal(amountPaidEdit.getText().toString());
				if (leftToPay.compareTo(amountReceived)>=0) {
					PaymentMethod paymentMethod = new PaymentMethod();
					paymentMethod.setPaymentType(GENRES[listViewWayOfPayments.getCheckedItemPosition()]);
					paymentMethod.setAmount(amountReceived);
					paymentMethodList.add(paymentMethod);
					paymentsListInUI.add(paymentMethod.getPaymentType() + " - $" + paymentMethod.getAmount().toString());
					listViewPaymentsReceived.setAdapter(new ArrayAdapter<String>(activity,
							android.R.layout.simple_list_item_single_choice, paymentsListInUI));
					listViewWayOfPayments.clearChoices();
					leftToPay = leftToPay.subtract(amountReceived);
					amountPaidEdit.setText(leftToPay.toString());
					amountPaidEdit.setHint(activity.getString(R.string.sale_payments_amount_paid_hint));
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(amountPaidEdit.getApplicationWindowToken(), 0);
					deletePayment.setEnabled(true);
				} else {
					Toast toast = Toast.makeText(this, "El monto recibido es superior al monto a pagar", Toast.LENGTH_LONG);
					toast.show();
				}
			} else {
				Toast toast = Toast.makeText(this, R.string.sale_payments_payment_list_not_selected, Toast.LENGTH_LONG);
				toast.show();
			}		
		} else {
			Toast toast = Toast.makeText(this, R.string.sale_payments_payment_amount_null_value, Toast.LENGTH_LONG);
			toast.show();
		}
	}
	

	private void openNextActivity() {
		for (int i=0 ; i<paymentMethodList.size() ; i++) {
			PaymentMethod paymentMethod = paymentMethodList.get(i);
			if (paymentMethod.getPaymentType().equals(GENRES[0])) {
		    	Intent intent = new Intent();
		    	writeSaleDataIntoIntent(intent);
				openTicketActivity(intent);
				break;
			} else if (paymentMethod.getPaymentType().equals(GENRES[2])) {
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
	   startActivityForResult(intent, NFC_ACTIVITY_ID);
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
    	startActivityForResult(intent,MERCADO_PAGO_ACTIVITY_ID);
    	this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
    }

    public void openCreditCardCheckoutActivity() {
    	Intent intent = new Intent();
    	writeSaleDataIntoIntent(intent);
    	intent.setClass(getBaseContext(), SalesCreditCardCheckoutActivity.class);
    	startActivityForResult(intent,CREDIT_CARD_ACTIVITY_ID);
    	this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
    }
    
	private void deletePayments(ArrayList<PaymentMethod> paymentMethodList, ArrayList<String> paymentsListInUI) {
		if (paymentsListViewChecked) {
			SparseBooleanArray checkedItems = listViewPaymentsReceived.getCheckedItemPositions();
			int deletedPayments = 0;
			for (int i=0; i<checkedItems.size(); i++) {
				if (checkedItems.valueAt(i)) {
					Log.d("SalesPaymentsActivity","checked item: " + checkedItems.keyAt(i));
					leftToPay = leftToPay.add(paymentMethodList.get(checkedItems.keyAt(i)-deletedPayments).getAmount());
					amountPaidEdit.setText(leftToPay.toString());
					paymentMethodList.remove(checkedItems.keyAt(i)-deletedPayments);
					paymentsListInUI.remove(checkedItems.keyAt(i)-deletedPayments);
					deletedPayments++;
				}
			}
			if (deletedPayments>0) {
				listViewPaymentsReceived.setAdapter(new ArrayAdapter<String>(activity,
	            		android.R.layout.simple_list_item_single_choice, paymentsListInUI));
				if (paymentsListInUI.isEmpty()) { 
					deletePayment.setEnabled(false);
				}
		        paymentsListViewChecked = false;
			} else {
				Toast toast = Toast.makeText(this, "No hay pagos seleccionados", Toast.LENGTH_LONG);
				toast.show();
			}
		} else {
			Toast toast = Toast.makeText(this, "No hay pagos seleccionados", Toast.LENGTH_LONG);
			toast.show();
		}
	}
}
