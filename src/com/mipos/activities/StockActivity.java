package com.mipos.activities;

import java.math.BigDecimal;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mipos.adapters.*;
import com.mipos.asyncs.*;
import com.mipos.pojos.StockNewProduct;
import com.mipos.utils.PhotoHandler;

public class StockActivity extends ListActivity {

	StockActivity activity;
	EditText productCodeEditText;
	ItemsAdapter adapter;
	String categorySelected;
	ImageView imageView;
	byte[] pictureData;
	CheckBox generateQRCode;
	StockNewProduct newProduct;
//	private Camera camera;
//	private int cameraId = 0;
	
	static final String[] PRODUCT_CATEGORIES = new String[] { "Carteras", "Cintos", "Zapatos",
		"Camperas", "Billeteras", "Aros", "Perfumes" };
//	private static final String DEBUG_CAMERA_TAG = "Stock Activity";
	private static final int MY_CODE_SCANNER_REQUEST_ID = 0;
	private static final int MY_CAMERA_REQUEST_ID = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.stock);
	
	   Button readProductQRCodeButton = (Button) findViewById(R.id.stock_read_product_qrcode_button);
	   Button readProductBarCodeButton = (Button) findViewById(R.id.stock_read_product_barcode_button);
	   Button takeProductPictureButton = (Button) findViewById(R.id.stock_product_picture_action_button);
	   Button addProductToDatabase = (Button) findViewById(R.id.stock_product_add_to_database_button);
	   productCodeEditText = (EditText) findViewById(R.id.stock_product_code_editText);
	   final EditText productDescriptionEditText = (EditText) findViewById(R.id.stock_product_description_editText);
	   final EditText productPriceEditText = (EditText) findViewById(R.id.stock_product_price_editText);
	   final EditText productQuantityEditText = (EditText) findViewById(R.id.stock_product_quantity_editText);
	   ListView listView = getListView();
	   imageView = (ImageView) findViewById(R.id.stock_product_picture_imageView);
	   generateQRCode = (CheckBox) findViewById(R.id.stock_product_generate_qrcode_checkBox);
	   activity = this;
	      
	   readProductQRCodeButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, MY_CODE_SCANNER_REQUEST_ID);
			}			
       });
	   
	   readProductBarCodeButton.setOnClickListener(new OnClickListener() {
	   
			public void onClick(View v) {
			   Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		       intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
		       startActivityForResult(intent, MY_CODE_SCANNER_REQUEST_ID);
			}			
	   });
	      
	   takeProductPictureButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
//				ShutterCallback shutterCallback = new ShutterCallback() {
//					public void onShutter() {
//						Log.d(DEBUG_CAMERA_TAG, "onShutter'd");
//					}
//				};
//			 
//				/** Handles data for raw picture */
//				PictureCallback rawCallback = new PictureCallback() {
//					public void onPictureTaken(byte[] data, Camera camera) {
//						Log.d(DEBUG_CAMERA_TAG, "onPictureTaken - raw");
//					}
//				};
			 
				/** Handles data for jpeg picture */
//				PictureCallback jpegCallback = new PictureCallback() {
//					public void onPictureTaken(byte[] data, Camera camera) {
//						FileOutputStream outStream = null;
//						try {
//							// write to local sandbox file system
//							// outStream =
//							// CameraDemo.this.openFileOutput(String.format("%d.jpg",
//							// System.currentTimeMillis()), 0);
//							// Or write to sdcard
//							outStream = new FileOutputStream(String.format(
//									"/%d.jpg", System.currentTimeMillis()));
//							outStream.write(data);
//							outStream.close();
//							Log.d(DEBUG_CAMERA_TAG, "onPictureTaken - wrote bytes: " + data.length);
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						} finally {
//						}
//						Log.d(DEBUG_CAMERA_TAG, "onPictureTaken - jpeg");
//					}
//				};
//				camera.takePicture(shutterCallback, rawCallback,
//						new PhotoHandler(getApplicationContext()));
				//camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));

				openCamera();

			}			
	   });

	   addProductToDatabase.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				StockAddNewProductToDatabaseAsync stockAddNewProductToDatabaseAsync = new StockAddNewProductToDatabaseAsync(activity);
				newProduct = new StockNewProduct();
				newProduct.setCode(productCodeEditText.getText().toString());
				newProduct.setDescription(productDescriptionEditText.getText().toString());
				newProduct.setPrice(new BigDecimal(Double.parseDouble(productPriceEditText.getText().toString())));
				newProduct.setQuantity(Integer.parseInt(productQuantityEditText.getText().toString()));
				newProduct.setCategory(categorySelected);
				newProduct.setPictureData(pictureData);
				stockAddNewProductToDatabaseAsync.setNewProduct(newProduct);
				stockAddNewProductToDatabaseAsync.execute();
				pictureData = null;
			}			
       });
	   
	   adapter = new ItemsAdapter(this, PRODUCT_CATEGORIES, this);
	   listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	   listView.setAdapter(adapter);
	   
//	   setListAdapter(new ArrayAdapter<String>(this, R.layout.list_categories, PRODUCT_CATEGORIES));

	   listView.setTextFilterEnabled(true);

	   listView.setOnItemClickListener(new OnItemClickListener() {
		   public void onItemClick(AdapterView<?> parent, View view,
				   int position, long id) {
			   // When clicked, show a toast with the TextView text
			   CheckedTextView tv = (CheckedTextView) view.findViewById(R.id.list_checkedTextView);
			   adapter.toggle(tv);
			   Toast.makeText(getApplicationContext(),
					   ((CheckedTextView) view.findViewById(R.id.list_checkedTextView)).getText(), Toast.LENGTH_SHORT).show();
			   categorySelected = ((CheckedTextView) view.findViewById(R.id.list_checkedTextView)).getText().toString();
		   }
	   });
	   
//	   FeatureInfo[] ft = getPackageManager().getSystemAvailableFeatures();
//	   Log.i(DEBUG_CAMERA_TAG, ft.toString());
//	   
//	   // do we have a camera?
//	   if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA + ".any")) {
//		   Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG).show();
//	   } else {
//		   cameraId = findFrontFacingCamera();
//		   try {
//			   camera = Camera.open(cameraId);
//		   } catch (RuntimeException e) {
//			   Log.e(DEBUG_CAMERA_TAG, "Camera failed to open: " + e.getLocalizedMessage());
//		   }
//		   if (cameraId < 0) {
//			   Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
//		   }
//	   }
	} 
	
//	private int findFrontFacingCamera() {
//		int cameraId = -1;
//		// Search for the front facing camera
//		int numberOfCameras = Camera.getNumberOfCameras();
//		for (int i = 0; i < numberOfCameras; i++) {
//			CameraInfo info = new CameraInfo();
//			Camera.getCameraInfo(i, info);
//			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
//				Log.d(DEBUG_CAMERA_TAG, "Camera found");
//				cameraId = i;
//				break;
//			}
//		}
//		return cameraId;
//	}
	
//	@Override
//	protected void onPause() {
//		if (camera != null) {
//			camera.release();
//			camera = null;
//		}
//		super.onPause();
//	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == MY_CODE_SCANNER_REQUEST_ID) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                productCodeEditText.setText((CharSequence) contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        } else if (requestCode == MY_CAMERA_REQUEST_ID) {
        	pictureData = intent.getByteArrayExtra("PICTURE_DATA");
        	Bitmap photo = BitmapFactory.decodeByteArray(pictureData , 0, pictureData.length) ; 
        	imageView.setImageBitmap(photo);
        }
    }
	    
	public void openMainMenu() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), MainMenuActivity.class);
//		startActivityForResult(intent, 0);
		startActivity(intent);
	}
	
	public void openCamera() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), PictureActivity.class);
//		startActivityForResult(intent, 0);
		startActivityForResult(intent, MY_CAMERA_REQUEST_ID);	
	}
	
	public void openQRGenerator() {
		Intent intent = new Intent();
		intent.setClass(getBaseContext(), QRGeneratorActivity.class);
		intent.putExtra("PRODUCT_CODE", newProduct.getCode());
//		startActivityForResult(intent, 0);
		startActivity(intent);
		finish();
	}

	public CheckBox getGenerateQRCode() {
		return generateQRCode;
	}

	public void setGenerateQRCode(CheckBox generateQRCode) {
		this.generateQRCode = generateQRCode;
	}
	
}