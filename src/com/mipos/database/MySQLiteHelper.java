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
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_TOTAL = "total";
  public static final String COLUMN_PAYMENT_ID = "payment_id";
  public static final String COLUMN_CLIENT_ID = "client_id";
  
  public static final String COLUMN_TYPE = "type";
  public static final String COLUMN_OBJECT = "object";
  public static final String TABLE_TASKS = "tasks";
  public static final String TABLE_CLIENTS = "clients";
  public static final String TABLE_SALES = "sales";

  private static final String DATABASE_NAME = "mipos.db";
  private static final int DATABASE_VERSION = 7;

  // Database creation sql statement
  private static final String DATABASE_CREATE_PRODUCTS_TABLE = "create table "
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
  
  //Database creation sql statement
  private static final String DATABASE_CREATE_CLIENTS_TABLE = "create table "
		  + TABLE_CLIENTS + "(" + COLUMN_ID
		  + " integer primary key autoincrement, " + COLUMN_NAME
		  + " text not null);";
  
  // Database creation sql statement
  private static final String DATABASE_CREATE_SALES_TABLE = "create table "
      + TABLE_SALES + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_TOTAL
      + " real not null, " + COLUMN_CLIENT_ID + " int not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
  
  @Override
  public void onCreate(SQLiteDatabase database) {
//  database.execSQL(DATABASE_CREATE_PRODUCTS_TABLE);
//  database.execSQL(DATABASE_CREATE_TASK_TABLE);
//    database.execSQL(DATABASE_CREATE_CLIENTS_TABLE);
//	database.execSQL(DATABASE_CREATE_SALES_TABLE);  
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
//  db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
//  db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
//    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTS);    
    onCreate(db);
  }
} 
