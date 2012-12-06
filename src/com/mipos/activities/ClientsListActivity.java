package com.mipos.activities;

import com.mipos.adapters.ClientsListItemsAdapter;
import com.mipos.asyncs.ClientsSearchAsync;
import com.mipos.pojos.Client;
import com.mipos.pojos.ClientsList;
import com.mipos.utils.TransferBigDecimalObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ClientsListActivity extends ListActivity {

	TextView client;
	ClientsListItemsAdapter adapter;
	ListView listView;
	Activity activity;
	ClientsList clientsList;
	String parentActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clients_list);

		Button addNewClientButton = (Button) findViewById(R.id.clients_list_button_add_client);
		activity = this;
			
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			parentActivity = extras.getString("ParentActivity");
		}

		addNewClientButton.setOnClickListener(new OnClickListener() {
	
			public void onClick(View v) {
				openAddNewClientActivity();
			}

		});
		
		client = (TextView) findViewById(R.id.clients_list_item_textView);
		listView = getListView();	

		// 	   setListAdapter(new ArrayAdapter<String>(this, R.layout.list_categories, PRODUCT_CATEGORIES));

		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (parentActivity.equals("Sales")) {
					finish(clientsList.getClientsList().get(position));
				} else {
					openClientHistoryActivity(clientsList.getClientsList().get(position));
				}
			}
		});	
		
		ClientsSearchAsync clientsSearchAsync = new ClientsSearchAsync(this);
		clientsSearchAsync.execute();
	}

	public void finish(Client clientSelected) {
	  // Prepare data intent 
	  Intent data = new Intent();
	  data.putExtra("clientSelected", clientSelected);
	  setResult(RESULT_OK, data);
	  finish();
	} 
	
	public void loadClientsList(ClientsList clientsList) {		
		this.clientsList = clientsList;
		adapter = new ClientsListItemsAdapter(this, this.clientsList.getClientsList(), this);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setAdapter(adapter);
	}   
   
	public void openAddNewClientActivity() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), ClientsAddNewClientActivity.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
	public void openClientHistoryActivity(Client client) {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), ClientHistoryActivity.class);
		intent.putExtra("clientId", client.getId());
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
}
