package com.mipos.database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
} 
