package com.mipos.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_PRODUCTS = "products";
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_CODE = "code";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_PRICE = "price";
  public static final String COLUMN_QUANTITY = "quantity";
  public static final String COLUMN_CATEGORY = "category";

  private static final String DATABASE_NAME = "mipos.db";
  private static final int DATABASE_VERSION = 2;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_PRODUCTS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_CODE
      + " text not null, " + COLUMN_DESCRIPTION + " text not null, " + COLUMN_PRICE +
      " real not null, " + COLUMN_QUANTITY + " integer not null, " + COLUMN_CATEGORY + 
      " text null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
    onCreate(db);
  }

} 
