package com.mipos.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;

import com.mipos.pojos.Client;
import com.mipos.pojos.Sale;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

public class SQliteLoader {

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

	  public static void SalesLoader(Activity activity) {
		  SalesDataSource salesDataSource;           
		  salesDataSource = new SalesDataSource(activity);
		  salesDataSource.open();

		  Sale sale = new Sale();
		  Client client = new Client();
		  sale.setTotalCartAmount(new BigDecimal("2899"));
		  client.setId(2);
		  sale.setClient(client);
		  salesDataSource.insertSale(sale);
		  sale.setTotalCartAmount(new BigDecimal("499"));
		  client.setId(1);
		  salesDataSource.insertSale(sale);
		  sale.setTotalCartAmount(new BigDecimal("250"));
		  client.setId(4);
		  salesDataSource.insertSale(sale);
		  sale.setTotalCartAmount(new BigDecimal("900"));
		  client.setId(3);
		  salesDataSource.insertSale(sale);
	  }
	  
	  public static void ClientsLoader(Activity activity) {
		  ClientsDataSource clientsDataSource;
		  clientsDataSource = new ClientsDataSource(activity);
		  clientsDataSource.open();	

		  Client client = new Client();
		  client.setName("Javier Perez");
		  clientsDataSource.insertClient(client);
		  client.setName("Jose Gonzalez");
		  clientsDataSource.insertClient(client);
		  client.setName("Samuel Eto");
		  clientsDataSource.insertClient(client);
		  client.setName("Xavi Alonzo");
		  clientsDataSource.insertClient(client);
	  }

	  public static void ProductsLoader(Activity activity) {
		  ProductsDataSource productsDataSource;
		  productsDataSource = new ProductsDataSource(activity);
		  productsDataSource.open();

		  //	        StockNewProduct product = new StockNewProduct();
		  //	        product.setCode("P1");
		  //	        product.setDescription("Cartera de Cuero de Milano");
		  //	        product.setPrice(new BigDecimal("345"));
		  //	        product.setQuantity(2);
		  //	        product.setCategory("Carteras");
		  //	        
		  //	        datasource.createProduct(product);
		  //	        
		  //	        product.setCode("P22");
		  //	        product.setDescription("Billetera Leopardo Color Camel");
		  //	        product.setPrice(new BigDecimal("388"));
		  //	        product.setQuantity(1);
		  //	        product.setCategory("Billeteras");
		  //
		  //	        datasource.createProduct(product);
	  }
	
}
