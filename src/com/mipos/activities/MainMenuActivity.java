package com.mipos.activities;

import java.util.Date;

import com.mipos.dummies.BluetoothConnect;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainMenuActivity extends Activity {

	private static final int OPTIONS_ACTIVITY_ID = 0;
	Intent optionsIntent = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        Button button3 = (Button) findViewById(R.id.main_menu_cash_close);
        Button cashOpen = (Button) findViewById(R.id.main_menu_cash_open);
        Button clientsButton = (Button) findViewById(R.id.main_menu_clients);
        Button options = (Button) findViewById(R.id.main_menu_options);
        Button stockButton = (Button) findViewById(R.id.main_menu_stock);
        Button button7 = (Button) findViewById(R.id.main_menu_view_sale_items);
        final Button mainMenuAddSaleButton = (Button) findViewById(R.id.main_menu_add_sale);
        
        mainMenuAddSaleButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		openAddNewSaleActivity();
       	    }
        });
        
        clientsButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		openClientsListActivity();
       	    }
        });
        
        stockButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		openStockActivity();
       	    }
        });
        
        cashOpen.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		openCashOpenActivity();
       	    }
        });
        
        options.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		openOptionsActivity();
       	    }
        });
        
    }    
    
	public void openAddNewSaleActivity() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), SalesAddNewSaleActivity.class);
//		startActivityForResult(intent, 0);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
	public void openCashOpenActivity() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), CashOpenActivity.class);
//		startActivityForResult(intent, 0);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
	public void openClientsListActivity() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), ClientsListActivity.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
	public void openStockActivity() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), StockActivity.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
	public void openOptionsActivity() {
		if (optionsIntent==null) {
			optionsIntent = new Intent();
			optionsIntent.setClass(getBaseContext(), OptionsActivity.class);
		}
		startActivityForResult(optionsIntent, OPTIONS_ACTIVITY_ID);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_inicial, menu);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode) {
    	case OPTIONS_ACTIVITY_ID:
    		if (resultCode == RESULT_OK) {
    			Log.i("MainMenu Activity", "On activity Result OK for Options");
//    			BluetoothConnect btc = (BluetoothConnect) data.getSerializableExtra("btc");
    		}
    		break;
    	}
    }
    
    @Override
    public void onResume() {
    	Log.i("MainMenu Activity", "On Resume");
        super.onResume();
    }
    
}
