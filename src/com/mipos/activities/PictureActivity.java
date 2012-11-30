/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
 */

package com.mipos.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class PictureActivity extends Activity {
  private SurfaceView preview=null;
  private SurfaceHolder previewHolder=null;
  private Camera camera=null;
  private boolean inPreview=false;
  private boolean cameraConfigured=false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.camera_preview);

    preview=(SurfaceView)findViewById(R.id.camera_preview_surfaceView);
    previewHolder=preview.getHolder();
    previewHolder.addCallback(surfaceCallback);
  }

  @Override
  public void onResume() {
    super.onResume();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      Camera.CameraInfo info=new Camera.CameraInfo();

      for (int i=0; i < Camera.getNumberOfCameras(); i++) {
        Camera.getCameraInfo(i, info);

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
          try {
        	  camera=Camera.open(i);
          } catch (Exception e) {
          	camera.release();
            camera=null;
            inPreview=false;
            Log.e("Releasing Camera",
                    "Exception in camera detection", e);
          }
        }
      }
    }

    if (camera == null) {
      camera=Camera.open();
    }

    startPreview();
  }

  @Override
  public void onPause() {
    if (inPreview) {
      camera.stopPreview();
    }

    camera.release();
    camera=null;
    inPreview=false;

    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    new MenuInflater(this).inflate(R.menu.camera_options, menu);

    return(super.onCreateOptionsMenu(menu));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.camera) {
      if (inPreview) {
        camera.takePicture(null, null, photoCallback);
        inPreview=false;
      }
    }

    return(super.onOptionsItemSelected(item));
  }

  private Camera.Size getBestPreviewSize(int width, int height,
                                         Camera.Parameters parameters) {
    Camera.Size result=null;

    for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
      if (size.width <= width && size.height <= height) {
        if (result == null) {
          result=size;
        }
        else {
          int resultArea=result.width * result.height;
          int newArea=size.width * size.height;

          if (newArea > resultArea) {
            result=size;
          }
        }
      }
    }

    return(result);
  }

  private Camera.Size getSmallestPictureSize(Camera.Parameters parameters) {
    Camera.Size result=null;

    for (Camera.Size size : parameters.getSupportedPictureSizes()) {
      if (result == null) {
        result=size;
      }
      else {
        int resultArea=result.width * result.height;
        int newArea=size.width * size.height;

        if (newArea < resultArea) {
          result=size;
        }
      }
    }

    return(result);
  }

  private void initPreview(int width, int height) {
    if (camera != null && previewHolder.getSurface() != null) {
      try {
        camera.setPreviewDisplay(previewHolder);
      }
      catch (Throwable t) {
        Log.e("PreviewDemo-surfaceCallback",
              "Exception in setPreviewDisplay()", t);
        Toast.makeText(PictureActivity.this, t.getMessage(),
                       Toast.LENGTH_LONG).show();
      }

      if (!cameraConfigured) {
        Camera.Parameters parameters=camera.getParameters();
        Camera.Size size=getBestPreviewSize(width, height, parameters);
        Camera.Size pictureSize=getSmallestPictureSize(parameters);

        if (size != null && pictureSize != null) {
          parameters.setPreviewSize(size.width, size.height);
          parameters.setPictureSize(pictureSize.width,
                                    pictureSize.height);
          parameters.setPictureFormat(ImageFormat.JPEG);
          camera.setParameters(parameters);
          cameraConfigured=true;
        }
      }
    }
  }

  private void startPreview() {
    if (cameraConfigured && camera != null) {
      camera.startPreview();
      inPreview=true;
    }
  }

  SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback() {
    public void surfaceCreated(SurfaceHolder holder) {
      // no-op -- wait until surfaceChanged()
    }

    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {
      initPreview(width, height);
      startPreview();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
      // no-op
    }
  };

  Camera.PictureCallback photoCallback=new Camera.PictureCallback() {
    public void onPictureTaken(byte[] data, Camera camera) {
    	Intent resultData = new Intent();
    	resultData.putExtra("PICTURE_DATA", data);
    	setResult(Activity.RESULT_OK, resultData);
    	finish();	
/*    	new SavePhotoTask(this).execute(data);
    	camera.startPreview();
    	inPreview=true;*/
    }
  };

}