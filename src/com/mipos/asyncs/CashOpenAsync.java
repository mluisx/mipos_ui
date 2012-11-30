package com.mipos.asyncs;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.mipos.activities.CashOpenActivity;
import com.mipos.pojos.Cash;
import com.mipos.utils.ConfigHelper;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.os.AsyncTask;

public class CashOpenAsync extends AsyncTask<Request, Void, String> {

	private String jsonObject;
	private HttpResponse response;
	private Cash cash;
	private CashOpenActivity cashOpenActivity;
	
    public CashOpenAsync(Activity cashOpenActivity) { 
    	this.cashOpenActivity = (CashOpenActivity) cashOpenActivity;
    }
	
	@Override
	protected String doInBackground(Request... params) {
		HttpHost target = new HttpHost(ConfigHelper.SERVER_URL, Integer.parseInt(ConfigHelper.SERVER_PORT), ConfigHelper.SERVER_PROTOCOL);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(ConfigHelper.URL3);

		try {			
			postRequest.addHeader("Content-Type", "application/json");

			Gson gson = new Gson();

			jsonObject = gson.toJson(cash, Cash.class);

			StringEntity entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);

			postRequest.setEntity(entity);

			response = httpClient.execute(target, postRequest);

			if (response.getStatusLine().getStatusCode() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postRequest.getEntity()!=null)
				try {
					postRequest.getEntity().consumeContent();
				} catch (IOException e) {}					
		}
		return "finish";
	}
	
	@Override
	protected void onPostExecute(String unused) {
		cashOpenActivity.cashSuccessfullyOpened();
	}

	public Cash getCash() {
		return cash;
	}

	public void setCash(Cash cash) {
		this.cash = cash;
	} 
	
}
