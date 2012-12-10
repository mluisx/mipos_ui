package com.mipos.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mipos.adapters.*;
import com.mipos.database.ClientsDataSource;
import com.mipos.database.ProductsDataSource;
import com.mipos.database.SQliteLoader;
import com.mipos.database.SalesDataSource;
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
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SalesAddNewSaleActivity extends ListActivity {

	EditText productCodeEditText;
	TextView productDescriptionTextView, productPriceTextView, productQuantityTextView,
		cartItem, finalCartPriceTextView;
	Button addSaleButtonPrice, addSaleButtonAdd,
		deleteProducts, addSaleButtonNextPurchaseStep;
	Map<String,String[]> mp;
	List<ProductForSale> productForSaleList;
	ScrollView cartItems;
	int quantity;
	CartItemsAdapter adapter;
	Activity activity;
	BigDecimal productPrice;
	ProductsDataSource productsDataSource;
	ClientsDataSource clientsDataSource;
	SalesDataSource salesDataSource;
	BigDecimal totalCartAmount;	
	ArrayList<String> productsListInUI = new ArrayList<String>();
	ListView productsListView;
	boolean productsListViewChecked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_add_new_sale);
        
        productsDataSource = new ProductsDataSource(this);
        productsDataSource.open();
                                    
    	int currentStock;
        currentStock = productsDataSource.selectProductQuantity("P1");
        Log.i("SalesAddNewSaleActivity", "current P1 Stock: " + currentStock);
        SQliteLoader.DBackup();
//      currentStock--;
//      datasource.updateProduct("P1", currentStock);
//      currentStock = datasource.selectProductQuantity("P1");
//      Log.i("SalesAddNewSaleActivity", "new P1 Stock: " + currentStock);
//      OpenObjectFromSQLite();
     
//      List<StockNewProduct> values2 = productsDataSource.getAllProducts();
//      Log.i("SalesAddNewSaleActivity", "MyClass.getView() - get item values from sqlite " + values2.toString());        
        
        addSaleButtonAdd = (Button) findViewById(R.id.sales_add_new_sale_button);
        deleteProducts = (Button) findViewById(R.id.sales_add_new_sale_delete_products_button);
        addSaleButtonNextPurchaseStep = (Button) findViewById(R.id.sales_add_new_sale_button_next_purchase_step);
        productCodeEditText = (EditText) findViewById(R.id.sale_add_new_sale_product_code_editText);
        productDescriptionTextView = (TextView) findViewById(R.id.sale_add_new_sale_product_description_textView);        
        productPriceTextView = (TextView) findViewById(R.id.sales_add_new_sale_product_price_textView);
        productQuantityTextView = (TextView) findViewById(R.id.sales_add_new_sale_product_quantity_textView);
        finalCartPriceTextView = (TextView) findViewById(R.id.sales_add_new_sale_textView_final_price);
        quantity = 0;
        productForSaleList = new ArrayList<ProductForSale>();
        productsListView = getListView();
        activity = this;
        totalCartAmount = new BigDecimal("0");
        deleteProducts.setEnabled(false);
        addSaleButtonAdd.setEnabled(false);
		productsListViewChecked = false;
        
//        mp = new HashMap<String, String[]>();
//
//        // adding or set elements in Map by put method key and value pair
//        mp.put("P1", new String[]{"Cartera de Cuero de Milano","345"});
//        mp.put("P22", new String[]{"Billetera Leopardo Color Camel","388"});
//        mp.put("P333", new String[]{"Zapato Doble Taco Alto Negro","450"});
//        mp.put("P4444", new String[]{"Campera Oveja Invierno 2012","550"});

        addSaleButtonNextPurchaseStep.setOnClickListener(new OnClickListener() {

        	public void onClick(View v) {
        		openPaymentsActivity();
        	}});
        
        deleteProducts.setOnClickListener(new OnClickListener() {

        	public void onClick(View v) {
        		deleteProducts(productForSaleList, productsListInUI);
        	}});

        productCodeEditText.addTextChangedListener(new TextWatcher() {
        			
        	public void afterTextChanged(Editable arg0) {
        		String code = arg0.toString();		  				
		        boolean hasValue = false;
		        
		        List<StockNewProduct> values = productsDataSource.getAllProducts();
		        Iterator it = values.iterator();
		        
		        while(it.hasNext() && !hasValue) {
		            StockNewProduct prod = (StockNewProduct) it.next();            
		            if (prod.getCode().equals(code)) {
		            	productDescriptionTextView.setText((CharSequence) prod.getDescription());
		            	productPrice = prod.getPrice();
		            	productPriceTextView.setText((CharSequence) "Precio $" + productPrice.toString());
		            	if (quantity == 0) {
		            		quantity++;
		            	}
		            	productQuantityTextView.setText("Disponible en Stock: " + prod.getQuantity());
		            	hasValue = true;
		            	if (prod.getQuantity()>0) {
		            		addSaleButtonAdd.setEnabled(true);
		            	} else {
		            		addSaleButtonAdd.setEnabled(false);
		            	}
		            }	            
		        }
		        
		        if (!hasValue && !productDescriptionTextView.getText().toString().isEmpty()) {
		        	productDescriptionTextView.setText((CharSequence) "");
		        	addSaleButtonAdd.setEnabled(false);
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
//
//              ArrayList<String> list1 = new ArrayList<String>();
//              list1.add("Producto 1");
//              list1.add("Producto 2");
//              list1.add("Producto 3");

        //   	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_categories, PRODUCT_CATEGORIES));

        productsListView.setClickable(true);

//      adapter = new CartItemsAdapter(this, null, this);

        productsListView.setAdapter(new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_single_choice, productsListInUI));
        productsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//      listView.setAdapter(adapter);

        productsListView.setTextFilterEnabled(true);

        productsListView.setOnItemClickListener(new OnItemClickListener() {

        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        			long arg3) {
        		productsListViewChecked = true;
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
        		productsListInUI.add(productForSale.getCode() + " - $" + productForSale.getPrice().toString() +
    					" - Cantidad: "+ productForSale.getQuantity());
        		//adapter = new CartItemsAdapter(activity, list1, activity);
                productsListView.setAdapter(new ArrayAdapter<String>(activity,
                		android.R.layout.simple_list_item_single_choice, productsListInUI));
        		finalCartPriceTextView.setText("Total $" + (CharSequence) totalCartAmount.toString());
        		if (!deleteProducts.isEnabled()) {
        			deleteProducts.setEnabled(true);
        		}
        		addSaleButtonAdd.setEnabled(false);
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
        Sale sale = productsDataSource.selectTaskToSync();
        Log.i("SalesAddNewSaleActivity", "Object from SQlite Value: " + sale.getTotalCartAmount());
	}
	
	private void deleteProducts(List<ProductForSale> productForSaleList, ArrayList<String> productsListInUI) {
		if (productsListViewChecked) {
			SparseBooleanArray checkedItems = productsListView.getCheckedItemPositions();
			int deletedProducts = 0;
			for (int i=0; i<checkedItems.size(); i++) {
				if (checkedItems.valueAt(i)) {
					Log.d("SalesAddNewSaleActivity","checked item: " + checkedItems.keyAt(i));
					totalCartAmount = totalCartAmount.subtract(
							productForSaleList.get(checkedItems.keyAt(i)-deletedProducts).getPrice());
					finalCartPriceTextView.setText("Total $" + (CharSequence) totalCartAmount.toString());
					productForSaleList.remove(checkedItems.keyAt(i)-deletedProducts);
					productsListInUI.remove(checkedItems.keyAt(i)-deletedProducts);
					deletedProducts++;
				}
			}
			if (deletedProducts>0) {
				productsListView.setAdapter(new ArrayAdapter<String>(activity,
	            		android.R.layout.simple_list_item_single_choice, productsListInUI));
				if (productsListInUI.isEmpty()) { 
					deleteProducts.setEnabled(false);
				}
				productsListViewChecked = false;
			} else {
				Toast toast = Toast.makeText(this, "No hay productos seleccionados", Toast.LENGTH_LONG);
				toast.show();
			}
		} else {
			Toast toast = Toast.makeText(this, "No hay productos seleccionados", Toast.LENGTH_LONG);
			toast.show();
		}
	}

}
