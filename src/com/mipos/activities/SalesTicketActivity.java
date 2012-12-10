package com.mipos.activities;

import harmony.java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.BarcodeEAN;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import com.mipos.database.ProductsDataSource;
import com.mipos.database.SalesDataSource;
import com.mipos.pojos.PaymentMethod;
import com.mipos.pojos.ProductForSale;
import com.mipos.pojos.Sale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SalesTicketActivity extends Activity {

	EditText emailText;
	Bundle extras;
	ProductsDataSource productsDataSource;
	SalesDataSource salesDataSource;
	Sale sale;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sales_ticket_activity);	
		
		Button finishWithoutPrinting = (Button) findViewById(R.id.sales_ticket_no_printing_and_exit_button);
		Button printTicketButton = (Button) findViewById(R.id.sales_ticket_print_ticket_button);
		Button sendTicketByMailButton = (Button) findViewById(R.id.sales_ticket_send_ticket_to_email_button);
		emailText = (EditText) findViewById(R.id.sales_ticket_email_editText);
		ImageView ticketImage = (ImageView) findViewById(R.id.sales_ticket_imageView);
		sale = null;
		
		extras = getIntent().getExtras();
		
		if (extras != null) {
			sale = (Sale) extras.getSerializable("SaleData");
			if (sale.getSignature()!=null) {
				ticketImage.setImageBitmap(sale.getSignature().getBitmap());
			}
		}
		
		generatePDFTicket();
		
		finishWithoutPrinting.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				writeSaleToDB(sale);
				openMarketingActivity();
			}

		});
		
		sendTicketByMailButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				sendTicketByMail();
			}

		});
		
		printTicketButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				printPDFTicket();
			}

		});
	}
	
	private void sendTicketByMail() {
		File pngFile = new File(Environment.getExternalStorageDirectory() + java.io.File.separator + "ticket.pdf");
		Uri pdfUri = Uri.fromFile(pngFile);
		String emailEntered = emailText.getText().toString();
		Intent email = new Intent(Intent.ACTION_SEND);
		email.setType("application/pdf");
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailEntered});		  
		email.putExtra(Intent.EXTRA_SUBJECT, "Tu Ticket de Compra de Manzanita Store Baires");
		email.putExtra(Intent.EXTRA_TEXT, "Estimado Cliente:\n\nEn este correo electrónico se " +
				"encuentra adjunto su ticket de compra.\n\nMuchas gracias por su visita y lo " +
				"esperamos nuevamente por nuestro local.\n\nSaludos cordiales.\nManzanita Store Baires.");
		email.putExtra(Intent.EXTRA_STREAM, pdfUri);
//		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Eliga un cliente de Email:"));
	}
	
    public void openMarketingActivity() {
 	   Intent intent = new Intent();
 	   intent.setClass(getBaseContext(), SalesMarketingActivity.class);
 	   startActivity(intent);
 	   this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
    }
    
    public void printPDFTicket() {
    	 File file = new File(Environment.getExternalStorageDirectory() + java.io.File.separator + "ticket.pdf");

         if (file.exists()) {
             Uri path = Uri.fromFile(file);
             Intent intent = new Intent(Intent.ACTION_VIEW);
             intent.setDataAndType(path, "application/pdf");
             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

             try {
                 startActivity(intent);
             } 
             catch (ActivityNotFoundException e) {
                 Toast.makeText(this, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
             }
         }
    }
    
    public void generatePDFTicket() {
		Document document = new Document();
		
		try {
			PdfWriter.getInstance(document, new FileOutputStream(Environment.getExternalStorageDirectory() + java.io.File.separator + "ticket.pdf"));
            document.open();
            Table table = new Table(1);
            table.setBorderWidth(1);
            table.setBorderColor(new Color(0, 0, 255));
            table.setPadding(5);
            table.setSpacing(5);
            Cell cell = new Cell();
 
            addHeader(cell);
            
            LineSeparator ls = new LineSeparator();
            cell.add(ls);
            
            List<ProductForSale> listOfProducts = sale.getProductForSaleList();
            ProductForSale product = listOfProducts.get(0);
            
            Phrase myPhrase = new Phrase("\n", new Font(Font.TIMES_ROMAN, 8));
    		cell.add(myPhrase);
        	myPhrase = new Phrase(product.getCode() + "\n", new Font(Font.TIMES_ROMAN, 8));
    		cell.add(myPhrase);
    		myPhrase = new Phrase("$" + product.getPrice() + "\n", new Font(Font.TIMES_ROMAN, 8));
    		cell.add(myPhrase);
    		myPhrase = new Phrase("Total $" + sale.getTotalCartAmount() + "\n", new Font(Font.TIMES_ROMAN, 8));
    		cell.add(myPhrase);
    		
    		ArrayList<PaymentMethod> listOfPayments = sale.getPaymentMethodList();    		
    		PaymentMethod paymentMethod = listOfPayments.get(0);
    		
    		myPhrase = new Phrase("Pagado con " + paymentMethod.getPaymentType() + "\n", 
    				new Font(Font.TIMES_ROMAN, 8));
    		cell.add(myPhrase);
                       
            cell.add(ls);

            table.addCell(cell);
            document.add(table);          
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
    }

	private void addHeader(Cell cell) throws BadElementException,
			MalformedURLException, IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Bitmap photo = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + java.io.File.separator + "mac.jpg");
		photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		Image img = Image.getInstance(stream.toByteArray());    
		cell.add(img);
		Phrase myPhrase = new Phrase("Manzanita Store Baires\n", new Font(Font.TIMES_ROMAN, 10, Font.BOLD));
		cell.add(myPhrase);
		myPhrase = new Phrase("Alem 965\n", new Font(Font.TIMES_ROMAN, 8));
		cell.add(myPhrase);
		myPhrase = new Phrase("Ciudad de Buenos Aires\n", new Font(Font.TIMES_ROMAN, 8));
		cell.add(myPhrase);
		myPhrase = new Phrase("manzana@store.com\n", new Font(Font.TIMES_ROMAN, 8));
		cell.add(myPhrase);
		myPhrase = new Phrase("+54-11-540118701\n", new Font(Font.TIMES_ROMAN, 8));
		cell.add(myPhrase);
		myPhrase = new Phrase("CUIT: 20-32938493-6\n", new Font(Font.TIMES_ROMAN, 8));
		cell.add(myPhrase);
		myPhrase = new Phrase("www.manzanita.com\n", new Font(Font.HELVETICA, 8));
		cell.add(myPhrase);
	}
	
    public void writeSaleToDB(Sale sale) {
    	 if (updateLocalStockTable(sale.getProductForSaleList())) {
    		 if (insertSaleToDB(sale)>0) {
    			 Toast.makeText(this, "La venta ha sido almacenada", Toast.LENGTH_LONG).show();
    		 } 	
         	createTask(sale);
         }
    }
    
    public boolean updateLocalStockTable(List<ProductForSale> listOfProducts) {
    	int currentStock;
        productsDataSource = new ProductsDataSource(this);
        productsDataSource.open();
        ProductForSale product = listOfProducts.get(0);
        currentStock = productsDataSource.selectProductQuantity(product.getCode());
        currentStock--;
        return productsDataSource.updateProduct(product.getCode(), currentStock);
    }
    
    public long insertSaleToDB(Sale sale) {
        salesDataSource = new SalesDataSource(this);
        salesDataSource.open();
        return salesDataSource.insertSale(sale);
    }
    
    public void createTask(Sale sale) {
    	productsDataSource.createTask(sale);
    }
}

