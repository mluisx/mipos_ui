package com.mipos.asyncs;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.mipos.activities.StockActivity;
import com.mipos.pojos.LogIn;
import com.mipos.pojos.StockNewProduct;
import com.mipos.utils.ConfigHelper;

import android.app.DownloadManager.Request;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

public class StockAddNewProductToDatabaseAsync extends AsyncTask<Request, Void, String> {

	private StockActivity stockActivity;
	private StockNewProduct newProduct;
	private String jsonObject;
	private HttpResponse response;

    public StockAddNewProductToDatabaseAsync(StockActivity stockActivity) { 
    	this.stockActivity = stockActivity;
    }

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
	@Override
	protected String doInBackground(Request... params) {
    	HttpHost target = new HttpHost(ConfigHelper.SERVER_URL, Integer.parseInt(ConfigHelper.SERVER_PORT), ConfigHelper.SERVER_PROTOCOL);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(ConfigHelper.URL2);
		
		try {			
			postRequest.addHeader("Content-Type", "application/json");
			
			Gson gson = new Gson();
					
			jsonObject = gson.toJson(newProduct, StockNewProduct.class);
				
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
    	Toast.makeText(stockActivity, response.getStatusLine().getStatusCode() + response.getEntity().toString(), Toast.LENGTH_LONG).show();
    	try {
			if (stockActivity.getGenerateQRCode().isChecked()) {
				stockActivity.openQRGenerator();
			} else {
				stockActivity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
    }

	public void setNewProduct(StockNewProduct newProduct) {
		this.newProduct = newProduct;			
	}
}
