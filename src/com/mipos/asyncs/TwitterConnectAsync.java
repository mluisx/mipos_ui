package com.mipos.asyncs;

import com.mipos.activities.SalesMarketingActivity;
import com.mipos.twitter.TwitterUtils;

import android.app.DownloadManager.Request;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class TwitterConnectAsync extends AsyncTask<Request, Void, String> {

	SalesMarketingActivity salesMarketingActivity;
	private SharedPreferences prefs;
	private boolean connected = false;
	
	public TwitterConnectAsync(SalesMarketingActivity salesMarketingActivity) { 
		this.salesMarketingActivity = salesMarketingActivity;
	}	
	
	@Override
	protected String doInBackground(Request... params) {	
		
		if (TwitterUtils.isAuthenticated(prefs)) {
			connected = true;
		}		
		return "done";		
	}

	public SharedPreferences getPrefs() {
		return prefs;
	}

	public void setPrefs(SharedPreferences prefs) {
		this.prefs = prefs;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
		
	@Override
	protected void onPostExecute(String unused) {
		salesMarketingActivity.setText("Logged into Twitter : " + isConnected());
	}
}
