package com.mipos.pojos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SerializedBitmap implements Serializable {

	private static final long serialVersionUID = 1L;
	public Bitmap bitmap;

	public SerializedBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	// Converts the Bitmap into a byte array for serialization
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
		byte bitmapBytes[] = byteStream.toByteArray();
		out.write(bitmapBytes, 0, bitmapBytes.length);
	}

	// Deserializes a byte array representing the Bitmap and decodes it
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int b;
		while((b = in.read()) != -1)
			byteStream.write(b);
		byte bitmapBytes[] = byteStream.toByteArray();
		bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
}
