package com.mipos.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mipos.adapters.*;
import com.mipos.database.MySQLiteHelper;
import com.mipos.database.ProductsDataSource;
import com.mipos.pojos.ProductForSale;
import com.mipos.pojos.Sale;
import com.mipos.pojos.StockNewProduct;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class SalesAddNewSaleActivity extends ListActivity {

	EditText productCodeEditText;
	TextView productDescriptionTextView, productPriceTextView, productQuantityTextView,
		cartItem, finalCartPriceTextView;
	Button addSaleButtonPrice, addSaleButtonQuantityMore, addSaleButtonQuantityLess, addSaleButtonAdd;
	Map<String,String[]> mp;
	List<ProductForSale> productForSaleList;
	ScrollView cartItems;
	int quantity;
	CartItemsAdapter adapter;
	Activity activity;
	BigDecimal productPrice;
	ProductsDataSource datasource;
	BigDecimal totalCartAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_add_new_sale);
        
        datasource = new ProductsDataSource(this);
        datasource.open();
              
//        StockNewProduct product = new StockNewProduct();
//        product.setCode("P1");
//        product.setDescription("Cartera de Cuero de Milano");
//        product.setPrice(new BigDecimal("345"));
//        product.setQuantity(2);
//        product.setCategory("Carteras");
//        
//        datasource.createProduct(product);
//        
//        product.setCode("P22");
//        product.setDescription("Billetera Leopardo Color Camel");
//        product.setPrice(new BigDecimal("388"));
//        product.setQuantity(1);
//        product.setCategory("Billeteras");
//
//        datasource.createProduct(product);
        
    	int currentStock;
        currentStock = datasource.selectProductQuantity("P1");
        Log.i("SalesAddNewSaleActivity", "current P1 Stock: " + currentStock);
        MySQLiteHelper.DBackup();
//      currentStock--;
//      datasource.updateProduct("P1", currentStock);
//      currentStock = datasource.selectProductQuantity("P1");
//      Log.i("SalesAddNewSaleActivity", "new P1 Stock: " + currentStock);
        OpenObjectFromSQLite();
     
        List<StockNewProduct> values2 = datasource.getAllProducts();
        Log.i("SalesAddNewSaleActivity", "MyClass.getView() - get item values from sqlite " + values2.toString());        
        
        addSaleButtonAdd = (Button) findViewById(R.id.sales_add_new_sale_button);
        addSaleButtonQuantityMore = (Button) findViewById(R.id.sales_add_new_sale_button_quantity_more);
        addSaleButtonQuantityLess = (Button) findViewById(R.id.sales_add_new_sale_button_quantity_less);
        addSaleButtonPrice = (Button) findViewById(R.id.sales_add_new_sale_button_price);
        Button addSaleButtonNextPurchaseStep = (Button) findViewById(R.id.sales_add_new_sale_button_next_purchase_step);
        productCodeEditText = (EditText) findViewById(R.id.sale_add_new_sale_product_code_editText);
        productDescriptionTextView = (TextView) findViewById(R.id.sale_add_new_sale_product_description_textView);        
        productPriceTextView = (TextView) findViewById(R.id.sales_add_new_sale_product_price_textView);
        productQuantityTextView = (TextView) findViewById(R.id.sales_add_new_sale_product_quantity_textView);
        finalCartPriceTextView = (TextView) findViewById(R.id.sales_add_new_sale_textView_final_price);
        quantity = 0;
        productForSaleList = new ArrayList<ProductForSale>();
        final ListView listView = getListView();
        activity = this;
        totalCartAmount = new BigDecimal("0");
        
//        mp = new HashMap<String, String[]>();
//
//        // adding or set elements in Map by put method key and value pair
//        mp.put("P1", new String[]{"Cartera de Cuero de Milano","345"});
//        mp.put("P22", new String[]{"Billetera Leopardo Color Camel","388"});
//        mp.put("P333", new String[]{"Zapato Doble Taco Alto Negro","450"});
//        mp.put("P4444", new String[]{"Campera Oveja Invierno 2012","550"});
                
        addSaleButtonQuantityMore.setOnClickListener(new OnClickListener() {
			
        	public void onClick(View v) {
          		quantity++;
            	productQuantityTextView.setText((CharSequence) "Cantidad: " + Integer.toString(quantity));
			}});
        
        addSaleButtonQuantityLess.setOnClickListener(new OnClickListener() {
			
    		public void onClick(View v) {
    			quantity--;
            	productQuantityTextView.setText((CharSequence) "Cantidad: " + Integer.toString(quantity));
    		}});
        
        addSaleButtonNextPurchaseStep.setOnClickListener(new OnClickListener() {
			
    		public void onClick(View v) {
    			openPaymentsActivity();
    		}});
        
        productCodeEditText.addTextChangedListener(new TextWatcher() {
        			
        	public void afterTextChanged(Editable arg0) {
        		//String code = productCodeEditText.getText().toString();
        		String code = arg0.toString();
				
				//Get Map in Set interface to get key and value
		        //Set s = mp.entrySet();
		        
		        //Move next key and value of Map by iterator
		        //Iterator it = s.iterator();	        
				
		        boolean hasValue = false;
		        
		        List<StockNewProduct> values = datasource.getAllProducts();
		        Iterator it2 = values.iterator();
		        
		        while(it2.hasNext() && !hasValue)
		        {
		            //key=value separator this by Map.Entry to get key and value
		            //Map.Entry m = (Map.Entry) it.next();
		            StockNewProduct prod = (StockNewProduct) it2.next();
	            
		            if (prod.getCode().equals(code)) {
		            	//String[] datos = new String[2];
		            	// datos = (String[]) m.getValue();
		            	productDescriptionTextView.setText((CharSequence) prod.getDescription());
		            	productPrice = prod.getPrice();
		            	productPriceTextView.setText((CharSequence) "Precio $" + productPrice.toString());
		            	if (quantity == 0) {
		            		quantity++;
		            	}	
		            	productQuantityTextView.setText((CharSequence) "Cantidad: " + Integer.toString(quantity));
		            	hasValue = true;
		            }
		            
		        }
		        
		        if (!hasValue && !productDescriptionTextView.getText().toString().isEmpty()) {
		        	productDescriptionTextView.setText((CharSequence) "");
		        }
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

		});
       
       ArrayList<String> list1 = new ArrayList<String>();
       list1.add("Producto 1");
       list1.add("Producto 2");
       list1.add("Producto 3");
       
       adapter = new CartItemsAdapter(this, list1, this);
 	   listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
 	   listView.setAdapter(adapter);
 	   
// 	   setListAdapter(new ArrayAdapter<String>(this, R.layout.list_categories, PRODUCT_CATEGORIES));

 	   listView.setTextFilterEnabled(true);

 	   listView.setOnItemClickListener(new OnItemClickListener() {

 		   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
 				   long arg3) {		
 		   }

 	   });
 	   
 	   addSaleButtonAdd.setOnClickListener(new OnClickListener() {

 		   public void onClick(View v) {
 			   ProductForSale productForSale = new ProductForSale();
 			   productForSale.setCode(productCodeEditText.getText().toString());
 			   productForSale.setQuantity(quantity);
 			   productForSale.setPrice(productPrice);
 			   productForSaleList.add(productForSale);
 			   totalCartAmount = totalCartAmount.add(productPrice);
 			   ArrayList<String> list1 = new ArrayList<String>();
 			   for (int i=0 ; i<productForSaleList.size() ; i++) {
 				   list1.add(productForSaleList.get(i).getCode() + " - " + productForSaleList.get(i).getPrice().toString() +
 						   " - "+ productForSaleList.get(i).getQuantity());
 			   }
 			   adapter = new CartItemsAdapter(activity, list1, activity);
 			   listView.setAdapter(adapter);
 			   finalCartPriceTextView.setText((CharSequence) totalCartAmount.toString());
 		   }});
     
    }    

    public void openPaymentsActivity() {
    	Intent intent = new Intent();
    	intent.setClass(getBaseContext(), SalesPaymentsActivity.class);
    	//		   TransferBigDecimalObject objectReadyToTransfer = new TransferBigDecimalObject(sale);
    	Sale saleToSend = new Sale();
    	saleToSend.setProductForSaleList(productForSaleList);
    	saleToSend.setTotalCartAmount(totalCartAmount);
    	intent.putExtra("SaleData", saleToSend);
    	startActivity(intent);
    	this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
    }
	
	public void OpenObjectFromSQLite() {
        Sale sale = datasource.selectTaskToSync();
        Log.i("SalesAddNewSaleActivity", "Object from SQlite Value: " + sale.getTotalCartAmount());
	}

}
