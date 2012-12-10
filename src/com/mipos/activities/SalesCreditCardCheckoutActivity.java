package com.mipos.activities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.mipos.pojos.Client;
import com.mipos.pojos.CreditCard;
import com.mipos.pojos.Sale;
import com.mipos.pojos.SerializedBitmap;
import com.mipos.reader.*;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVReader;

public class SalesCreditCardCheckoutActivity extends Activity implements RhombusActivity {

	Bundle extras;
	EditText holdersName, cardNumber, cardExpirationDate, cardCvc;
	TextView readerText;
	SalesCreditCardCheckoutActivity activity;
	
	private static final int SIGNATURE_ACTIVITY_ID = 1;

	private Handler mHandler;
	private AudioDecoder decoder;
	private HeadsetStateReceiver dongleListener;
	private boolean dongleReady;
	private TextView track;
	private TextView issuer;
	private ImageView issuerLogo;
	private Thread listenerThread;	
	public static String READER_TAG = "Card Reader";
	private static String IINFile = "IINs-simple.csv";
	private Map<String, IIN> prefixMap;
	private static final IIN UNKNOWN_IIN = new IIN("", "unknown", "unknown");
	private Map<String, Integer> nameToLogo;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_credit_card_checkout);
        
		Button addNewClientButton = (Button) findViewById(R.id.sales_begin_credit_card_checkout_process_button);
		holdersName = (EditText) findViewById(R.id.sales_credit_card_checkout_holders_name_editText);
		cardNumber = (EditText) findViewById(R.id.sales_credit_card_checkout_card_number_editText);
		cardExpirationDate = (EditText) findViewById(R.id.sales_credit_card_checkout_expiration_date_editText);
		cardCvc = (EditText) findViewById(R.id.sales_credit_card_checkout_cvc_editText);
		extras = getIntent().getExtras();
		activity = this;
	    
	    // Card Reader
	    
		final ImageView dataIcon = (ImageView) this.findViewById(R.id.sales_credit_card_checkout_dataIcon);
		track = (TextView) this.findViewById(R.id.sales_credit_card_checkout_trackContent);
		issuer = (TextView) this.findViewById(R.id.sales_credit_card_checkout_issuerContent);
		issuerLogo = (ImageView)this.findViewById(R.id.sales_credit_card_checkout_issuerLogo);
		this.dongleReady = false;  
		prefixMap = new HashMap<String, IIN>();
		populateIINs();
		nameToLogo = new HashMap<String, Integer>();
		populateLogos();
		readerText = (TextView) findViewById(R.id.sales_credit_card_checkout_reader_textView);
		
		mHandler = new Handler(){
			public void handleMessage(Message msg){
				MessageType message = MessageType.values()[msg.what];
				Toast toast;
				switch (message){
				case DATA_PRESENT:
					dataIcon.setImageResource(R.drawable.data_icon);
					clearFields();
					break;
				case NO_DATA_PRESENT:
					dataIcon.setImageResource(R.drawable.idle_icon);
					break;
				case DECODED_TRACK:
					SwipeData sd = (SwipeData)msg.obj;
					processSwipe(sd);
					startListening();
					break;
				case RECORDING_ERROR:
					toast = Toast.makeText(activity, R.string.sales_credit_card_checkout_recordingError, Toast.LENGTH_LONG);
					toast.show();
					startListening();
					break;
				case INVALID_SAMPLE_RATE:
					toast = Toast.makeText(activity, R.string.sales_credit_card_checkout_unsupportedSampleRate, Toast.LENGTH_LONG);
					toast.show();
					break;

				}
			}
		};
		
		decoder = new AudioDecoder(mHandler);
		
		addNewClientButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				openSignatureActivity();
			}

		});
	}  

    @Override
    public void onStart(){
    	super.onStart();
	    IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
	    dongleListener = new HeadsetStateReceiver(this);
	    registerReceiver( dongleListener, receiverFilter );
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	unregisterReceiver(dongleListener);
    	stopListening();
    }
	
	public void openSignatureActivity() {
		Sale saleToSend = null;
		Intent intent = new Intent();
		CreditCard creditCardData = new CreditCard(holdersName.getText().toString(), cardNumber.getText().toString(), 
				cardExpirationDate.getText().toString(), cardCvc.getText().toString());
		intent.setClass(getBaseContext(), SalesSignatureActivity.class);
		if (extras != null) {
			saleToSend = (Sale) extras.getSerializable("SaleData");
			saleToSend.setCreditCard(creditCardData);
		}
		intent.putExtra("SaleData", saleToSend);
		startActivityForResult(intent, SIGNATURE_ACTIVITY_ID);
		this.overridePendingTransition(R.anim.left_mov, R.anim.right_mov);
	}

	public void setDongleReady(boolean ready) {
    	this.dongleReady = ready;
    	if (ready){
    		readerText.setText("Lector de Tarjetas: Conectado");
    		startListening();
    	}else{
    		readerText.setText("Lector de Tarjetas: Desconectado");
    		stopListening();
    	}
	}
	
	private void startListening(){
		Log.d(READER_TAG, "in StartListening, dongleReady:"+dongleReady);
		if (!dongleReady){
			/*
	    		Toast toast = Toast.makeText(this, R.string.readerNotPresent, 2000);
	    		toast.show();
			 */
			return;
		}else{
			stopListening();
			decoder.setFrequency(getFrequency());
			decoder.setMinLevelCoeff(getMinLevelCoeff()/100f);
			decoder.setSilenceLevel(getSilenceLevel());
			listenerThread = new Thread(new Runnable() {
				public void run() {
					decoder.monitor();
				}
			});
			listenerThread.start();
		}
	}

	private void stopListening(){
		if (listenerThread != null){
			decoder.stopRecording();
			try{
				listenerThread.join(5000);
			}catch(InterruptedException ie){
				Log.d(READER_TAG, "Interruped Exception in CardDumpActivity.stopListening");
			}finally{
				listenerThread = null;
			}
		}
	}

	private void clearFields(){
		track.setText("");
		issuer.setText("");
		issuerLogo.setImageResource(R.drawable.no_logo);
	}
	
    /**
     * load and parse IIN csv file
     * iterate over each line, create IIN for it and add to prefix hash
     */
    private void populateIINs(){
    	try{
	    	InputStream is = getAssets().open(IINFile);
	    	InputStreamReader ireader = new InputStreamReader(is);
	    	CSVReader reader = new CSVReader(ireader, ';');
	    	String[] nextline;
	    	String prefix;
	    	String name;
	    	String type;
	    	IIN iin;
	    	while((nextline = reader.readNext()) != null){
	    	    // nextLine[] is an array of values from the line
	    		prefix = nextline[0];
	    		name = nextline[1];
	    		if (nextline.length > 2){
	    			type = nextline[2];
	    		}else{
	    			type = "";
	    		}
	    		iin = new IIN(prefix, name, type);
	    		prefixMap.put(prefix, iin);
	    	}
	    	reader.close();
    	} catch(IOException ioe){
			Toast toast = Toast.makeText(this, R.string.sales_credit_card_checkout_fileError, Toast.LENGTH_LONG);
			toast.show();
			ioe.printStackTrace();
			Log.e(READER_TAG, ioe.getMessage());
    	}   	
    }
	
    private void populateLogos(){
    	nameToLogo.put("American Express", R.drawable.american_express);
    	nameToLogo.put("Maestro", R.drawable.maestro);
    	nameToLogo.put("MasterCard", R.drawable.mastercard);
    	nameToLogo.put("Visa", R.drawable.visa);
    	nameToLogo.put("Visa Electron", R.drawable.visa_electron);
    }
    
    private void processSwipe(SwipeData swipe){
    	String raw = swipe.content;
    	//set decoded
		if (swipe.isBadRead()){
			track.setText("Mala Lectura");
		}else{
			track.setText(swipe.toFormattedCharSequence());
			//attempt to find IIN by prefix
	    	String prefix = "";
	    	if (raw.length() > 7){
	    		prefix = raw.substring(1, 7); //first 6 other than start sentinel
	    	}else if (raw.length() > 0){
	    		prefix = raw.substring(1);
	    	}
	    	IIN iin = getIINbyPrefix(prefix);
	    	issuer.setText(iin.getName());
	    	//issuerLogo
	    	issuerLogo.setImageResource(getImgResourceByIIN(iin));
		}
    }
    
    /**
     * get IIN by longest prefix of prefix that has an entry in prefixMap
     * @param prefix
     * @return
     */
    private IIN getIINbyPrefix(String prefix){
    	IIN toreturn;
    	while(prefix.length() > 0){
    		toreturn = prefixMap.get(prefix);
    		if (toreturn != null){
    			return toreturn;
    		}else{
    			prefix = prefix.substring(0, prefix.length()-1);
    		}
    	}
    	return UNKNOWN_IIN;
    }
    
    /**
     * get image resource id by IIN
     * @param iin
     * @return
     */
    private int getImgResourceByIIN(IIN iin){
    	Integer toreturn = R.drawable.no_logo;
    	if (iin.getType() == CardType.DRIVERSLICENSE){
    		toreturn = R.drawable.american_express; // return an AMEX instead of Driver License
    	}else{
    	  	toreturn = nameToLogo.get(iin.getName());
    	}
    	if (toreturn == null){
    		toreturn = R.drawable.no_logo;
    	}
    	return toreturn;
    }
    
    private SharedPreferences getPrefs(){
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    	return sharedPrefs;
    }
    
    private int getFrequency(){
    	String freqString = getPrefs().getString("sample_rate", "44100");
    	int freq = Integer.parseInt(freqString);
    	return freq;
    }
    
    private int getMinLevelCoeff(){
    	return getPrefs().getInt("threshold", 50);
    }
    
    private int getSilenceLevel(){
    	return getPrefs().getInt("silenceLevel", 500);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == SIGNATURE_ACTIVITY_ID) {
    		finishActivity(data);
    	}
    } 
    
	public void finishActivity(Intent data) {
		setResult(RESULT_OK, data);
		finish();
	}
	
}
