package com.mipos.database;

import java.util.ArrayList;
import java.util.List;

import com.mipos.pojos.Client;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ClientsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_NAME};

  public ClientsDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public long insertClient(Client client) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_NAME, client.getName());
    long insertId = database.insert(MySQLiteHelper.TABLE_CLIENTS, null, values);
    return insertId;
  }
  
  public List<Client> getAllClients() {
	  List<Client> clients = new ArrayList<Client>();
	  Cursor cursor = database.query(MySQLiteHelper.TABLE_CLIENTS,
			  allColumns, null, null, null, null, null);
	  cursor.moveToFirst();
	  while (!cursor.isAfterLast()) {
		  Client client = cursorToClient(cursor);
		  clients.add(client);
		  cursor.moveToNext();
	  }
	  // Make sure to close the cursor
	  cursor.close();
	  return clients;
  }
  
  public boolean updateClient(String name, int id) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_NAME, name);
	    int updatedRows = database.update(MySQLiteHelper.TABLE_CLIENTS, values, 
	    		MySQLiteHelper.COLUMN_ID + " = " + id, null);
	    if (updatedRows==1) { 
	    	return true;
	    } else { 
	    	return false;
	    } 
  }

  public void deleteClient(Client client) {
    long id = client.getId(); 
    database.delete(MySQLiteHelper.TABLE_CLIENTS, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  private Client cursorToClient(Cursor cursor) {
	  Client client = new Client();
	  client.setId(cursor.getLong(0));
	  client.setName(cursor.getString(1));
	  return client;
  }

}
