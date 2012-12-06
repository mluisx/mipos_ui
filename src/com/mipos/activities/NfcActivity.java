package com.mipos.activities;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

import com.mipos.asyncs.CashOpenAsync;
import com.mipos.pojos.Cash;
import com.mipos.pojos.NFCData;
import com.mipos.pojos.PaymentMethod;
import com.mipos.pojos.Sale;
import com.mipos.pojos.SerializedBitmap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NfcActivity extends Activity {

	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	private TextView mText;
	private int mCount = 0;
    private Context context;
    private IntentFilter[] mWriteTagFilters;
    private boolean mWriteMode = false;
    private Button endSaleButton;
    Bundle extras;
    private NFCData nfcData;
    private static final String NFC_PAYMENT = "Dispositivo NFC";
    private String textToPrintOnView = "";

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		setContentView(R.layout.nfc);
		mText = (TextView) findViewById(R.id.text);
		mText.setText("Scan a tag");
		context = getApplicationContext();  
		endSaleButton = (Button) findViewById(R.id.nfc_end_sale_button);
		endSaleButton.setEnabled(false);
		extras = getIntent().getExtras();

		mAdapter = NfcAdapter.getDefaultAdapter(this);

		// Create a generic PendingIntent that will be deliver to this activity. The NFC stack
		// will fill in the intent with the details of the discovered tag before delivering to
		// this activity.
		mPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		// Setup an intent filter for all MIME based dispatches
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		
        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
		
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] {
				ndef,
		};
		
		endSaleButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				openTicketActivity();
			}			
		});

		// Setup a tech list for all NfcF tags
		mTechLists = new String[][] { new String[] { NfcF.class.getName() } };
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null) mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
				mTechLists);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch", "Discovered tag " + mCount + " with intent: " + intent);
		// mText.setText("Discovered tag " + ++mCount + " with intent: " + intent);
		super.onNewIntent(intent);            
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {  
			NdefMessage[] messages = null;  
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);  
			if (rawMsgs != null) {  
				messages = new NdefMessage[rawMsgs.length];  
				for (int i = 0; i < rawMsgs.length; i++) {  
					messages[i] = (NdefMessage) rawMsgs[i];  
				}  
			}  
			if(messages[0] != null) {  
				String result="";  
				byte[] payload = messages[0].getRecords()[0].getPayload();  
				// this assumes that we get back am SOH followed by host/code  
				for (int b = 0; b<payload.length; b++) { // skip SOH  
					result += (char) payload[b];  
				}  
				Log.i("Foreground dispatch", "tag info loaded");
				textToPrintOnView = "El dispositivo NFC fue leído correctamente.\n" +
						"El saldo antes de la venta es $" + result + "\n";				
				BigDecimal amountToPay = null;
				nfcData = new NFCData();
				BigDecimal balanceBeforeDebit = new BigDecimal(result.substring(result.indexOf(":")+1));
				nfcData.setBalanceBeforeDebit(balanceBeforeDebit);
				Sale saleToSend = null;
				if (extras != null) {
					saleToSend = (Sale) extras.getSerializable("SaleData");
					amountToPay = findAmountToPay(amountToPay, saleToSend);
				}
				Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				nfcData.setNFCType(detectedTag.toString());
				Log.i("Foreground dispatch", "begining transaction");
				if (amountToPay != null) {
					if (balanceBeforeDebit.compareTo(amountToPay) > 0) {
						BigDecimal balanceAfterDebit = balanceBeforeDebit.subtract(amountToPay);
						nfcData.setBalanceAfterDebit(balanceAfterDebit);
			            writeTag(getNoteAsNdef("Balance:" + balanceAfterDebit.toString()), detectedTag);
			            textToPrintOnView += "Transacción aprobada\nEl nuevo saldo es de $" + balanceAfterDebit.toString();
			    		endSaleButton.setEnabled(true);
					} else {
						textToPrintOnView += "El monto a pagar de $" + amountToPay.toString() +
								" supera el valor del saldo.\nLa Transacción no podrá realizarse";	
						nfcData.setBalanceAfterDebit(balanceBeforeDebit);
					}
				} else {
					textToPrintOnView += "No se encontró el monto a debitar.\nTransacción cancelada"; 
				}
				Log.i("Foreground dispatch", "transaction ended");
				mText.setText(textToPrintOnView);
			//  Toast.makeText(getApplicationContext(), "Tag Now Contains " + result, Toast.LENGTH_SHORT).show();  
			}  
		}
		
        // Tag writing mode
//        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
//            
//        }
	}

	private BigDecimal findAmountToPay(BigDecimal amountToPay, Sale saleToSend) {
		boolean paymentAmountFound = false;
		int index = 0;
		ArrayList<PaymentMethod> listOfPayments = saleToSend.getPaymentMethodList();
		while (!paymentAmountFound && index<listOfPayments.size()) {
			PaymentMethod paymentReceived = listOfPayments.get(index);
			if (paymentReceived.getPaymentType().equals(NFC_PAYMENT)) {
				amountToPay = paymentReceived.getAmount();
				paymentAmountFound = true;
			}
			else {
				index++;
			}
		}
		return amountToPay;
	}
	
    private NdefMessage getNoteAsNdef(String text) {
        byte[] textBytes = text.getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] {
            textRecord
        });
    }
	
    boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    toast("Tag is read-only.");
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    toast("Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
                            + " bytes.");
                    return false;
                }

                ndef.writeNdefMessage(message);
                toast("Wrote message to pre-formatted tag.");
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        toast("Formatted tag and wrote message");
                        return true;
                    } catch (IOException e) {
                        toast("Failed to format tag.");
                        return false;
                    }
                } else {
                    toast("Tag doesn't support NDEF.");
                    return false;
                }
            }
        } catch (Exception e) {
            toast("Failed to write tag");
        }

        return false;
    }
	

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    
	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null) mAdapter.disableForegroundDispatch(this);
	}
	
	public void openTicketActivity() {
		Intent intent = new Intent();
		Sale saleToSend = null;
		intent.setClass(getBaseContext(), SalesTicketActivity.class);
		if (extras != null) {
			saleToSend = (Sale) extras.getSerializable("SaleData");
			saleToSend.setNfcData(nfcData);
		}
		intent.putExtra("SaleData", saleToSend);
		startActivity(intent);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}
	
}