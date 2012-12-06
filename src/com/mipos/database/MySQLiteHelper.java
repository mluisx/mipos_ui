package com.mipos.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_PRODUCTS = "products";
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_CODE = "code";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_PRICE = "price";
  public static final String COLUMN_QUANTITY = "quantity";
  public static final String COLUMN_CATEGORY = "category";
  
  public static final String COLUMN_TYPE = "type";
  public static final String COLUMN_OBJECT = "object";
  public static final String TABLE_TASKS = "tasks";

  private static final String DATABASE_NAME = "mipos.db";
  private static final int DATABASE_VERSION = 5;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_PRODUCTS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_CODE
      + " text not null, " + COLUMN_DESCRIPTION + " text not null, " + COLUMN_PRICE +
      " real not null, " + COLUMN_QUANTITY + " integer not null, " + COLUMN_CATEGORY + 
      " text null);";

  //Database creation sql statement
  private static final String DATABASE_CREATE_TASK_TABLE = "create table "
		  + TABLE_TASKS + "(" + COLUMN_ID
		  + " integer primary key autoincrement, " + COLUMN_TYPE
		  + " text not null, " + COLUMN_OBJECT + " blob not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
  
  public static void DBackup() {
	  try {
	      File sd = Environment.getExternalStorageDirectory();
	      File data = Environment.getDataDirectory();

	      if (sd.canWrite()) {
	          String currentDBPath = "data/com.mipos.activities/databases/mipos.db";
	          String backupDBPath = "miposbk.db";
	          File currentDB = new File(data, currentDBPath);
	          File backupDB = new File(sd, backupDBPath);

	          if (currentDB.exists()) {
	              FileChannel src = new FileInputStream(currentDB).getChannel();
	              FileChannel dst = new FileOutputStream(backupDB).getChannel();
	              dst.transferFrom(src, 0, src.size());
	              src.close();
	              dst.close();
	          }
	      }
	  } catch (Exception e) {
		  Log.e("MySQLiteHelper", "Exception: " + e.getMessage());
	  }  
  }


  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
    database.execSQL(DATABASE_CREATE_TASK_TABLE);
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
    onCreate(db);
  }
} 
