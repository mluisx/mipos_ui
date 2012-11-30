package com.mipos.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NfcAddFriendsActivity extends Activity {

	TextView date;
	EditText amount;
	Button saveButton;
	Activity cashOpenActivity;
	NfcAdapter mNfcAdapter;
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mNdefExchangeFilters;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.);
//		saveButton = (Button) findViewById(R.id.);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// Intent filters for exchanging over p2p.
		IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefDetected.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) {
		}
		mNdefExchangeFilters = new IntentFilter[] { ndefDetected };	
		
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				Place place = (Place) mListAdapter.getItem(position);
//				 
//				// NFC: Write id to tag
//				placeidToWrite = place.mPlaceId;
//				enableTagWriteMode();
//				 
//				new AlertDialog.Builder(NfcWriteCheckinActivity.this).setTitle("Touch tag to write")
//				    .setOnCancelListener(new DialogInterface.OnCancelListener() {
//				        public void onCancel(DialogInterface dialog) {
//				            disableTagWriteMode();
//				        }
//				    }).create().show();
			}
		});
	}
	
    @Override
    public void onResume() {
    	enableNdefExchangeMode();
    }
	
	private void enableNdefExchangeMode() {
//	    mNfcAdapter.enableForegroundNdefPush(NfcAddFriendsActivity.this,
//	        NfcUtils.getUidAsNdef(mUserId));
//	    mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, 
//	        mNdefExchangeFilters, null);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    // Tag writing mode
//	    if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
//	        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//	        if (NfcUtils.writeTag(NfcUtils.getPlaceidAsNdef(placeidToWrite), detectedTag)) {
//	            Toast.makeText(this, "Success: Wrote placeid to nfc tag", Toast.LENGTH_LONG)
//	                .show();
//	            NfcUtils.soundNotify(this);
//	        } else {
//	            Toast.makeText(this, "Write failed", Toast.LENGTH_LONG).show();
//	        }
//	    }
	}
	
	private void enableTagWriteMode() {
//	    mWriteMode = true;
//	    IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//	    mWriteTagFilters = new IntentFilter[] { tagDetected };
//	    mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
	}
	
	NdefMessage[] getNdefMessages(Intent intent) {
	    // Parse the intent
	    NdefMessage[] msgs = null;
	    String action = intent.getAction();
	    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
	        || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
	        Parcelable[] rawMsgs = 
	            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	        if (rawMsgs != null) {
	            msgs = new NdefMessage[rawMsgs.length];
	            for (int i = 0; i < rawMsgs.length; i++) {
	                msgs[i] = (NdefMessage) rawMsgs[i];
	            }
	        } else {
	            // Unknown tag type
	            byte[] empty = new byte[] {};
	            NdefRecord record = 
	                new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
	            NdefMessage msg = new NdefMessage(new NdefRecord[] {
	                record
	            });
	            msgs = new NdefMessage[] {
	                msg
	            };
	        }
	    } else {
//	        Log.d(TAG, "Unknown intent.");
	        finish();
	    }
	    return msgs;
	}
}

