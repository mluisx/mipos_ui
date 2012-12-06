package com.mipos.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mipos.pojos.Sale;
import com.mipos.pojos.StockNewProduct;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProductsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_CODE, MySQLiteHelper.COLUMN_DESCRIPTION, MySQLiteHelper.COLUMN_PRICE,
      MySQLiteHelper.COLUMN_QUANTITY, MySQLiteHelper.COLUMN_CATEGORY};
  private String[] allColumnsForTasks = { MySQLiteHelper.COLUMN_ID,
	      MySQLiteHelper.COLUMN_TYPE, MySQLiteHelper.COLUMN_OBJECT};

  public ProductsDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public StockNewProduct createProduct(StockNewProduct product) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_CODE, product.getCode());
    values.put(MySQLiteHelper.COLUMN_DESCRIPTION, product.getDescription());
    values.put(MySQLiteHelper.COLUMN_PRICE, product.getPrice().toString());
    values.put(MySQLiteHelper.COLUMN_QUANTITY, product.getQuantity());
    values.put(MySQLiteHelper.COLUMN_CATEGORY, product.getCategory());
    long insertId = database.insert(MySQLiteHelper.TABLE_PRODUCTS, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    StockNewProduct newProduct = cursorToProduct(cursor);
    cursor.close();
    return newProduct;
  }
  
  public int selectProductQuantity(String code) {
	  StockNewProduct newProduct = null;
	  Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
			  allColumns, MySQLiteHelper.COLUMN_CODE + " = \"" + code + "\"", null,
			  null, null, null);
	  if (cursor.moveToNext()) {
		  newProduct = cursorToProduct(cursor);
		  cursor.close();
		  return newProduct.getQuantity();
	  } else {
		  return -1;
	  }
  }
  
  public boolean updateProduct(String code, int quantity) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_QUANTITY, quantity);
	    int updatedRows = database.update(MySQLiteHelper.TABLE_PRODUCTS, values, 
	    		MySQLiteHelper.COLUMN_CODE + " = \"" + code + "\"", null);
	    if (updatedRows==1) { 
	    	return true;
	    } else { 
	    	return false;
	    } 
  }

  public void deleteProduct(StockNewProduct product) {
    long id = 1; 
//    		product.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_PRODUCTS, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<StockNewProduct> getAllProducts() {
    List<StockNewProduct> products = new ArrayList<StockNewProduct>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      StockNewProduct product = cursorToProduct(cursor);
      products.add(product);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return products;
  }

  private StockNewProduct cursorToProduct(Cursor cursor) {
	StockNewProduct product = new StockNewProduct();
//	product.setId(cursor.getLong(0));
	product.setCode(cursor.getString(1));
	product.setDescription(cursor.getString(2));
	product.setPrice(new BigDecimal(cursor.getDouble(3)));
	product.setQuantity(cursor.getInt(4));
	product.setCategory(cursor.getString(5));
    return product;
  }
  
  private byte[] cursorToObjectFromTask(Cursor cursor) {
	byte[] objectValueFromBlob = cursor.getBlob(2);
    return objectValueFromBlob;
  }

  public boolean createTask(Sale sale) {
	  ContentValues values = new ContentValues();
	  values.put(MySQLiteHelper.COLUMN_TYPE, "sale");
	  values.put(MySQLiteHelper.COLUMN_OBJECT, serialize(sale));
	  long insertId = database.insert(MySQLiteHelper.TABLE_TASKS, null,
			  values);
	  if (insertId>0) {
		  return true;
	  } else {
		  return false;
	  }
  }

  public Sale selectTaskToSync() {
	  Sale sale = null;
	  Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS,
			  allColumnsForTasks, MySQLiteHelper.COLUMN_TYPE + " = \"sale\"", null,
			  null, null, null);
	  if (cursor.moveToNext()) {
		  byte[] objectValue = cursorToObjectFromTask(cursor);
		  cursor.close();
		  sale = (Sale) deserialize(objectValue);
		  return sale;
	  } else {
		  return null;
	  }
  }

  //Convert a byte array to an Object
  public static Object deserialize(byte[] data) {
	  ByteArrayInputStream in = new ByteArrayInputStream(data);
	  ObjectInputStream is;
	  try {
		  is = new ObjectInputStream(in);
		  return is.readObject();
	  } catch (StreamCorruptedException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  } catch (IOException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  } catch (ClassNotFoundException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }
	  return null;
  }

  //Serialize an Object
  public static byte[] serialize(Object obj) {
	  ByteArrayOutputStream out = new ByteArrayOutputStream();
	  ObjectOutputStream os;
	  try {
		  os = new ObjectOutputStream(out);
		  os.writeObject(obj);
		  return out.toByteArray();
	  } catch (IOException e1) {
		  e1.printStackTrace();
	  }
	  return null;	 
  }
  
} 
