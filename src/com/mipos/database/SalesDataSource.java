package com.mipos.database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mipos.pojos.Client;
import com.mipos.pojos.Sale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SalesDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_TOTAL, MySQLiteHelper.COLUMN_CLIENT_ID};

  public SalesDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public long insertSale(Sale sale) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_TOTAL, sale.getTotalCartAmount().toString());
    values.put(MySQLiteHelper.COLUMN_CLIENT_ID, sale.getClient().getId());
    long insertId = database.insert(MySQLiteHelper.TABLE_SALES, null, values);
    return insertId;
  }
  
  public List<Sale> getAllSalesByClientId(int id) {
	  List<Sale> sales = new ArrayList<Sale>();
	  Cursor cursor = database.query(MySQLiteHelper.TABLE_SALES,
			  allColumns,  MySQLiteHelper.COLUMN_CLIENT_ID + " = " + id, null, null, null, null);
	  cursor.moveToFirst();
	  while (!cursor.isAfterLast()) {
		  Sale sale = cursorToSale(cursor);
		  sales.add(sale);
		  cursor.moveToNext();
	  }
	  // Make sure to close the cursor
	  cursor.close();
	  return sales;
  }
  
//  public void deleteSale(Sale sale) {
//    long id = client.getId(); 
//    database.delete(MySQLiteHelper.TABLE_CLIENTS, MySQLiteHelper.COLUMN_ID
//        + " = " + id, null);
//  }

  private Sale cursorToSale(Cursor cursor) {
	  Sale sale = new Sale();
	  sale.setTotalCartAmount(new BigDecimal(cursor.getDouble(1)));
	  Client client = new Client();
	  client.setId(cursor.getInt(2));
	  sale.setClient(client);
	  return sale;
  }

}
