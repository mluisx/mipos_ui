package com.mipos.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ClientsHistoryDetailActivity extends Activity {

	Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clients_extra_history);
		activity = this;
		Button backButton = (Button) findViewById(R.id.clients_extra_history_back_button);

		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();			
			}

		});
	}	
}
