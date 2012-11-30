package com.mipos.asyncs;

import java.io.File;
import java.io.FileOutputStream;

import com.mipos.activities.LoginActivity;
import com.mipos.pojos.LogIn;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class SavePhotoTask extends AsyncTask<byte[], String, String> {
    
	private Activity activity;

    public SavePhotoTask(Activity activity) { 
    	this.activity = activity;
    }
	
	@Override
    protected String doInBackground(byte[]... jpeg) {
      Log.i("Picture Activity", "External Storage State" + Environment.getExternalStorageState());
      File photo=
          new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                   "photo.jpg");

      if (photo.exists()) {
        photo.delete();
      }

      try {
        FileOutputStream fos=new FileOutputStream(photo.getPath());

        fos.write(jpeg[0]);
        fos.close();
      }
      catch (java.io.IOException e) {
        Log.e("PictureDemo", "Exception in photoCallback", e);
      }

      return(null);
    }
  }
