package com.mipos.activities;

import java.math.BigDecimal;
import java.util.Set;

import com.mipos.asyncs.CashOpenAsync;
import com.mipos.dummies.BluetoothConnect;
import com.mipos.pojos.Cash;
import com.mipos.services.BluetoothChatBackGroundTasks;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ClientsAddNewClientActivity extends Activity {
    
	Activity clientsAddNewClientActivity;
	EditText editText;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clients_add_new_client);
		
		clientsAddNewClientActivity = this;
		Button addNewClientData = (Button) findViewById(R.id.clients_add_button_add_client);
		Button captureClientPicture = (Button) findViewById(R.id.clients_add_button_add_client_picture);
		editText = (EditText) findViewById(R.id.clients_add_editText_client_name);
//		final EditText productDescriptionEditText = (EditText) findViewById(R.id.stock_product_description_editText);
//		final EditText productPriceEditText = (EditText) findViewById(R.id.stock_product_price_editText);
		
		addNewClientData.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

			}			
		});
	} 	
}
