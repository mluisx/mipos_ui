package com.mipos.asyncs;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mipos.activities.ClientHistoryActivity;
import com.mipos.pojos.ClientHistoryList;
import com.mipos.utils.ConfigHelper;

public class ClientHistorySearchAsync extends AsyncTask<Request, Void, String> {

	int clientId;
	
	private ClientHistoryList clientHistoryList;
	private Activity activity;
	
    public ClientHistorySearchAsync(Activity activity) { 
    	this.activity = activity;
    }
	
	@Override
	protected String doInBackground(Request... params) {
    	HttpHost target = new HttpHost(ConfigHelper.SERVER_URL, Integer.parseInt(ConfigHelper.SERVER_PORT), ConfigHelper.SERVER_PROTOCOL);
    	HttpEntity entity = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(ConfigHelper.URL5 + ConfigHelper.CLIENT_ID + getClientId());
		
		try {			
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(target, getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
			
			entity = response.getEntity();
			
			Gson gson = new Gson();
			
			clientHistoryList = gson.fromJson(EntityUtils.toString(entity), ClientHistoryList.class);
			Log.i("ClientHistorySearchAsync", "Getting Client History List");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entity!=null)
			try {
				entity.consumeContent();
			} catch (IOException e) {}					
		}
        return "finish";
	}
	
	@Override
	protected void onPostExecute(String unused) {
		((ClientHistoryActivity) activity).loadClientHistoryList(clientHistoryList);
	}

	private int getClientId() {
		return clientId;
	}
	
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
}
