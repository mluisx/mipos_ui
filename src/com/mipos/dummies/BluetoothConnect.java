package com.mipos.dummies;

import java.io.Serializable;
import java.util.Set;

import com.mipos.activities.OptionsActivity;
import com.mipos.services.BluetoothChatBackGroundTasks;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class BluetoothConnect {

	 // Debugging
	private static final String TAG = "DeviceListActivity";
	private static final boolean D = true;

	// Return Intent extra
	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
    // Name of the connected device
    private String mConnectedDeviceName = null;   
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatBackGroundTasks mChatService = null;
    
    String status;

    Activity activity;

	public void connectDeviceViaBluetooth() {
		// Get the local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatBackGroundTasks(activity, mHandler);

		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if (device.getAddress().equals("F0:08:F1:F4:2B:A5")) {
					connectDevice(device.getAddress(), false);
				}	
				break;
			}
		}
		onPostConnection();
	}			

	private void connectDevice(String address, boolean secure) {
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mChatService.connect(device, secure);		
	}

	private void onPostConnection() {
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatBackGroundTasks.STATE_CONNECTED:
					status = "Conectado a: " + mConnectedDeviceName;
					setStatus();
					//  mConversationArrayAdapter.clear();
					break;
				case BluetoothChatBackGroundTasks.STATE_CONNECTING:
					status = "Conectando";
					setStatus();
					break;
				case BluetoothChatBackGroundTasks.STATE_LISTEN:
				case BluetoothChatBackGroundTasks.STATE_NONE:
					status = "No Conectando";
					setStatus();
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				Log.i(TAG, "MESSAGE_SENT_BY_NEXUS_7");
				setTextWritten("written: " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				if (readMessage.equals("open_app")) {
					setTextReaded("open_app_maurix");
				} else {
					setTextReaded(readMessage);
				}
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(activity.getApplicationContext(), "Connected to "
						+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(activity.getApplicationContext(), msg.getData().getString(TOAST),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatBackGroundTasks.STATE_CONNECTED) {
            Toast.makeText(this.activity, "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }
	
	private final void setStatus() {
		((OptionsActivity) activity).setConnectionStatus(status);
	}
	
	private final void setTextReaded(CharSequence text) {
		((OptionsActivity) activity).setReceivedText(text.toString());
	}
	
	private final void setTextWritten(CharSequence text) {
		((OptionsActivity) activity).setTextWritten(text.toString());
	}

	public BluetoothChatBackGroundTasks getmChatService() {
		return mChatService;
	}

	public void setmChatService(BluetoothChatBackGroundTasks mChatService) {
		this.mChatService = mChatService;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
