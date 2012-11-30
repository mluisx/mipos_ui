package com.mipos.asyncs;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.mipos.activities.LoginActivity;
import com.mipos.pojos.LogIn;
import com.mipos.utils.ConfigHelper;

import android.app.DownloadManager.Request;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class LogInAsync extends AsyncTask<Request, Void, String> {

    private String message;
    private String user;
	private String pass;
	private LoginActivity mainActivity;
	private LogIn logInfo;

    public LogInAsync(LoginActivity mainActivity) { 
    	this.mainActivity = mainActivity;
    }

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Request... params) {
    	HttpHost target = new HttpHost(ConfigHelper.SERVER_URL, Integer.parseInt(ConfigHelper.SERVER_PORT), ConfigHelper.SERVER_PROTOCOL);
    	HttpEntity entity = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(ConfigHelper.URL1 + ConfigHelper.USER_ID + getUser() + ConfigHelper.PASS_ID + getPass());
		
		try {			
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(target, getRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
			
			entity = response.getEntity();
			setMessage(EntityUtils.toString(entity));
			
			Gson gson = new Gson();
			
			logInfo = gson.fromJson(getMessage(), LogIn.class);
//			logInfo = new LogIn(); // test
//			logInfo.setId(1); // test
//			logInfo.setUser("mluisx"); // test
			Log.i("LogInAsync", logInfo.toString());
			
			setMessage("El Usuario y ID Son " + logInfo.getUser() + " " + logInfo.getId());
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
    	mainActivity.progressBar.setVisibility(View.INVISIBLE);
    	Toast.makeText(mainActivity, getMessage(), Toast.LENGTH_LONG).show();
    	try {
			if (logInfo.getId()==1) {
				mainActivity.openMainMenu();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public String getMessage() {
		return message;
	}
	
    public void setMessage(String message) {
		this.message = message;
	}
    
    public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
}
