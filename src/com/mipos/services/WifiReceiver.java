package com.mipos.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {

	public static int times = 0;
	
    @Override
    public void onReceive(Context context, Intent intent) {     
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null) {
        	if ((netInfo.getType() == ConnectivityManager.TYPE_WIFI) && 
            		(netInfo.getState() == NetworkInfo.State.CONNECTED)) { 
                Log.d("WifiReceiver", "Have Wifi Connection and is Connected " + times);
            } else if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d("WifiReceiver", "Don't have Wifi Connection " + times);
            } else {
            	Log.d("WifiReceiver", "Wifi is Not Turned On " + times);
            }
        } else {
        	Log.d("WifiReceiver", "Wifi Error " + times);
        }
        times++;
    }
}
