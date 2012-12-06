package com.mipos.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.mipos.pojos.CreditCard;
import com.mipos.pojos.Sale;
import com.mipos.pojos.SerializedBitmap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SalesSignatureActivity extends Activity {

	private static final float LENGTH_THRESHOLD = 120.0f;

	private Gesture mGesture;
	private View mDoneButton;
	Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.signature_activity);

		mDoneButton = findViewById(R.id.done);
		
		extras = getIntent().getExtras();

		GestureOverlayView overlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		overlay.addOnGestureListener(new GesturesProcessor());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mGesture != null) {
			outState.putParcelable("gesture", mGesture);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		mGesture = savedInstanceState.getParcelable("gesture");
		if (mGesture != null) {
			final GestureOverlayView overlay =
					(GestureOverlayView) findViewById(R.id.gestures_overlay);
			overlay.post(new Runnable() {
				public void run() {
					overlay.setGesture(mGesture);
				}
			});

			mDoneButton.setEnabled(true);
		}
	}

//	@SuppressWarnings({"UnusedDeclaration"})
	public void addGesture(View v) {
//		if (mGesture != null) {
//			final TextView input = (TextView) findViewById(R.id.gesture_name);
//			final CharSequence name = input.getText();
//			if (name.length() == 0) {
//				input.setError(getString(R.string.error_missing_name));
//				return;
//			}
//
//			final GestureLibrary store = GestureBuilderActivity.getStore();
//			store.addGesture(name.toString(), mGesture);
//			store.save();
//
//			setResult(RESULT_OK);
//
//			final String path = new File(Environment.getExternalStorageDirectory(),
//					"gestures").getAbsolutePath();
//			Toast.makeText(this, getString(R.string.save_success, path), Toast.LENGTH_LONG).show();
//		} else {
//			setResult(RESULT_CANCELED);
		//		}
		Resources resources = getResources();
		int mPathColor = resources.getColor(R.color.gesture_color);
		int mThumbnailInset = (int) resources.getDimension(R.dimen.gesture_thumbnail_inset);
		int mThumbnailSize = (int) resources.getDimension(R.dimen.gesture_thumbnail_size);
	
		Bitmap bitmap = mGesture.toBitmap(mThumbnailSize, mThumbnailSize, mThumbnailInset, mPathColor);
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		OutputStream outStream = null;
		File file = new File(extStorageDirectory, "er1.PNG");
		try {
			outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		}
		catch(Exception e)
		{}
		openTicketActivity(bitmap);
		finish();

	}

	@SuppressWarnings({"UnusedDeclaration"})
	public void cancelGesture(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}

	private class GesturesProcessor implements GestureOverlayView.OnGestureListener {
		public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
			mDoneButton.setEnabled(false);
			mGesture = null;
		}

		public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		}

		public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
			mGesture = overlay.getGesture();
			if (mGesture.getLength() < LENGTH_THRESHOLD) {
				overlay.clear(false);
			}
			mDoneButton.setEnabled(true);
		}

		public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) { }
	}
	
	public void openTicketActivity(Bitmap bitmap) {
		Intent intent = new Intent();
		Sale saleToSend = null;
		intent.setClass(getBaseContext(), SalesTicketActivity.class);
		if (extras != null) {
			saleToSend = (Sale) extras.getSerializable("SaleData");
			saleToSend.setSignature(new SerializedBitmap(bitmap));
		}
		intent.putExtra("SaleData", saleToSend);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}

}

