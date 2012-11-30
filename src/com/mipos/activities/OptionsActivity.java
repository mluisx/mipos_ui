package com.mipos.activities;

import com.mipos.dummies.BluetoothConnect;
import com.mipos.services.BluetoothChatBackGroundTasks;
import com.mipos.utils.ConfigHelper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class OptionsActivity extends Activity {
	EditText protocolEditText, urlEditText, portEditText;
	EditText connectionStatus, textToSend, textReceived;
	Button saveButton, connectToDevice, textToSendButton;
	Activity activity;
	BluetoothConnect btConnect = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.options);
	
	   saveButton = (Button) findViewById(R.id.options_save_button);
	   protocolEditText = (EditText) findViewById(R.id.options_protocol_editText);
	   urlEditText = (EditText) findViewById(R.id.options_server_address_editText);
	   portEditText = (EditText) findViewById(R.id.options_port_editText);
	   connectionStatus = (EditText) findViewById(R.id.options_bluetooth_connection_device_editText);
	   textToSend = (EditText) findViewById(R.id.options_bluetooth_text_to_send_editText);
	   textReceived = (EditText) findViewById(R.id.options_bluetooth_text_received_editText);
	   connectToDevice = (Button) findViewById(R.id.options_bluetooth_connection_device_button);
	   textToSendButton = (Button) findViewById(R.id.options_bluetooth_text_to_send_button);
	   activity = this;
	   if (btConnect==null) {
		   btConnect = new BluetoothConnect();
	   }
	   btConnect.setActivity(activity);
//	   MiPosApp myApp = (MiPosApp) getApplicationContext();
//	   btConnect = new BluetoothConnect(activity);
//	   myApp.setBtConnect(btConnect);

       if (isMyServiceRunning()) {
    	   Log.i("Options Activity", "Service Already Running");
       }
	   
	   protocolEditText.setHint(ConfigHelper.SERVER_PROTOCOL);	
	   urlEditText.setHint(ConfigHelper.SERVER_URL);
	   portEditText.setHint(ConfigHelper.SERVER_PORT);
	   
	   saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
               finish();
			}
	   });
	   
	   connectToDevice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
               btConnect.connectDeviceViaBluetooth();
			}
	   });
	   
	   textToSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				btConnect.sendMessage(textToSend.getText().toString());
				textToSend.setText("");
			}
	   });
	}
	
	@Override
    public void onPause() {
		Log.i("Options Activity", "On Pause");		
		super.onPause();
	}
	
	@Override
    public void onStop() {
		Log.i("Options Activity", "On Stop");
		super.onStop();
	}
	
	@Override
	public void onBackPressed() {
	   Log.i("Options Activity", "onBackPressed Called");
	   Intent intent = new Intent();
	   MiPosApp myApp = (MiPosApp) getApplicationContext();
	   myApp.setBtConnect(btConnect);
	   setResult(RESULT_OK, intent);
	   super.onBackPressed();
	}
	
	public void setReceivedText(String text) {
		textReceived.setText(text);
	}
	
	public void setConnectionStatus(String text) {
		connectionStatus.setText(text);
	}
	
	public void setTextWritten(String text) {
		textToSend.setText(text);
	}
	
    private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (BluetoothChatBackGroundTasks.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
    
    @Override
    public void onResume() {
 	   Log.i("Options Activity", "on Resume");
 	   MiPosApp myApp = (MiPosApp) getApplicationContext();
    	if (myApp.getNumber()==2) {
    		Log.i("Options Activity", "on Resume 2");
        	btConnect = myApp.getBtConnect();
        	setConnectionStatus(btConnect.getStatus());
      	    btConnect.setActivity(this);
    	}
    	myApp.setNumber(2);
    	super.onResume();
    }
}
