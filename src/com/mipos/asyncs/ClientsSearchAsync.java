package com.mipos.asyncs;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mipos.activities.CashOpenActivity;
import com.mipos.activities.ClientsAddNewClientActivity;
import com.mipos.activities.ClientsListActivity;
import com.mipos.pojos.Cash;
import com.mipos.pojos.Client;
import com.mipos.pojos.ClientsList;
import com.mipos.pojos.LogIn;
import com.mipos.utils.ConfigHelper;

public class ClientsSearchAsync extends AsyncTask<Request, Void, String> {

	private ClientsList clientsList;
	private ClientsListActivity clientsListActivity;
	
    public ClientsSearchAsync(Activity clientsListActivity) { 
    	this.clientsListActivity = (ClientsListActivity) clientsListActivity;
    }
	
	@Override
	protected String doInBackground(Request... params) {
    	HttpHost target = new HttpHost(ConfigHelper.SERVER_URL, Integer.parseInt(ConfigHelper.SERVER_PORT), ConfigHelper.SERVER_PROTOCOL);
    	HttpEntity entity = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(ConfigHelper.URL4);
		
		try {			
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(target, getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
			
			entity = response.getEntity();
			
			Gson gson = new Gson();
			
			clientsList = gson.fromJson(EntityUtils.toString(entity), ClientsList.class);
			Log.i("ClientsSearchAsync", "Getting Clients List");
			
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
		clientsListActivity.loadClientsList(clientsList);
	}
}
