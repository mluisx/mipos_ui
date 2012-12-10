package com.mipos.reader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeadsetStateReceiver extends BroadcastReceiver {
	RhombusActivity activity;
	
	public HeadsetStateReceiver(RhombusActivity activity){
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
  		if (intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG)) {
	        int st = intent.getIntExtra("state", -1);
	        if (st==1) {
	        	Log.d("Card Reader", "Square Connected");
	        } else {
	        	Log.d("Card Reader", "Square Disconnected");
	        }
	        activity.setDongleReady(st == 1);
	    }

	}

}
