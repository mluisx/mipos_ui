package com.mipos.activities;

import com.mipos.adapters.ClientHistoryItemsAdapter;
import com.mipos.adapters.ClientsListItemsAdapter;
import com.mipos.asyncs.ClientHistorySearchAsync;
import com.mipos.asyncs.ClientsSearchAsync;
import com.mipos.pojos.ClientHistoryList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ClientHistoryActivity extends ListActivity {
	
	Activity activity;
	TextView clientHistory;
	ListView listView;
	ClientHistoryItemsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clients_history);
		Intent intent = this.getIntent();
		int clientId = intent.getIntExtra("clientId",1);
		Log.i("Client Id To Obtain History:", Integer.toString(clientId));
		activity = this;
		
		Button backButton = (Button) findViewById(R.id.clients_history_back_button);
		clientHistory = (TextView) findViewById(R.id.clients_list_item_textView);
		listView = getListView();	
		
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();			
			}

		});
		
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				openClientHistoryDetailActivity();			
			}
		});	
		
		ClientHistorySearchAsync clientHistorySearchAsync = new ClientHistorySearchAsync(this);
		clientHistorySearchAsync.setClientId(clientId);
		clientHistorySearchAsync.execute();		
	}

	public void loadClientHistoryList(ClientHistoryList clientHistoryList) {		
		adapter = new ClientHistoryItemsAdapter(this, clientHistoryList.getClientHistoryList(), this);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setAdapter(adapter);
	}
	
	public void openClientHistoryDetailActivity() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), ClientsHistoryDetailActivity.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
}
