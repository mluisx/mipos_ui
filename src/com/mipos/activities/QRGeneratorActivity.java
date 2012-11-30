package com.mipos.activities;

import java.util.Hashtable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QRGeneratorActivity extends Activity {

	ImageView imageView;
	TextView productCodeTextView;
	byte[] qrCodeImageData;
	Bitmap photo;
	
	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stock_qr_generator);

		imageView = (ImageView) findViewById(R.id.stock_qr_generator_imageView);
		Button exit = (Button) findViewById(R.id.stock_qr_generator_button2);
		productCodeTextView = (TextView) findViewById(R.id.stock_qr_generator_textView);
		
		String productCode = getIntent().getStringExtra("PRODUCT_CODE");
		productCodeTextView.setText("Código QR del Producto " + productCode);

		try {
			photo = encodeAsBitmap(productCode, BarcodeFormat.QR_CODE, 100, 100);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		ByteArrayOutputStream out = QRCode.from(productCode).to(ImageType.PNG).stream();
//		qrCodeImageData = out.toByteArray();
		
//    	Bitmap photo = BitmapFactory.decodeByteArray(qrCodeImageData, 0, qrCodeImageData.length); 
    	imageView.setImageBitmap(photo);
		
		exit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}			
		});

	}

	public Bitmap encodeAsBitmap(String contents,
			BarcodeFormat format,
			int desiredWidth,
			int desiredHeight) throws WriterException {
		Hashtable<EncodeHintType,Object> hints = null;
		String encoding = guessAppropriateEncoding(contents);
		if (encoding != null) {
			hints = new Hashtable<EncodeHintType,Object>(2);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();    
		BitMatrix result = writer.encode(contents, format, desiredWidth, desiredHeight, hints);
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}
}
