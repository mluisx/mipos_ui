package com.mipos.activities;

import com.mipos.asyncs.LogInAsync;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends Activity {

	EditText userText, passText;	
	TextView loginText;
	Button loginButton;
	public ProgressBar progressBar;
	LoginActivity activity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        
        userText = (EditText) findViewById(R.id.editText1);
        passText = (EditText) findViewById(R.id.editText2);
        loginButton = (Button) findViewById(R.id.button1);
        loginText = (TextView) findViewById(R.id.textView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
        activity = this;
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				progressBar.setVisibility(View.VISIBLE);
				LogInAsync logInAsync = new LogInAsync(activity);
				logInAsync.setUser(userText.getText().toString());
				logInAsync.setPass(passText.getText().toString());
				logInAsync.execute();				
			}
			
//			private String getKeywords(HttpHost target) {
//				String keywords = null;
//				HttpEntity entity = null;
//				HttpClient client = new DefaultHttpClient();
//				HttpGet get = new HttpGet(URL1);
//				try {
//					HttpResponse response=client.execute(target, get);
//					entity = response.getEntity();
//					keywords = EntityUtils.toString(entity);
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					if (entity!=null)
//					try {
//						entity.consumeContent();
//					} catch (IOException e) {}					
//				}
//				return keywords;
//			}
		});    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void openMainMenu() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), MainMenuActivity.class);
//		startActivityForResult(intent, 0);
		startActivity(intent);
	}
    
}
