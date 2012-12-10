package com.mipos.reader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AudioDecoder {
	public static String TAG = "Rhombus AudioDecoder";
	public static int TRACK_1_BITLENGTH = 7;
	public static int TRACK_1_BASECHAR = 32;
	
	public static int TRACK_2_BITLENGTH = 5;
	public static int TRACK_2_BASECHAR = 48;
	
	private boolean debugging = false;

	private Handler mHandler;
	
	private byte[] audioBytes;
	
	private int frequency = 22050; //44100;
	private int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
	private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	private int bufferSize;
	private AudioRecord audioRecord;
	private int silenceLevel = 3000; //arbitrary level below which we consider "silent"
	private int minLevel = silenceLevel; //adaptive minimum level, should vary with each swipe.
	private double minLevelCoeff = 0.5;
	
	private boolean recording = false;
	
	public AudioDecoder(Handler handler){
		mHandler = handler;
		setFrequency(frequency);
	}
	
	/**
	 * set the sample rate for recording.  Recalculates internal buffersize according to value.
	 * @param f
	 * @throws IllegalStateException if called while recording
	 */
	public void setFrequency(int f){
		if (recording){
			throw new IllegalStateException("Cannot set frequency while recording");
		}else{
			frequency = f;
			debug(TAG, "setting frequency to: "+f);
			bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding)*2;
		}
	}
	
	/**
	 * get the sample_rate used in recording audio
	 * @return
	 */
	public int getFrequency(){
		return frequency;
	}
	
	/**
	 * get the level below which we consider audio data to be silent
	 * @return
	 */
	public int getSilenceLevel() {
		return silenceLevel;
	}
	
	/**
	 * set arbitrary audio level below which we consider silent.  
	 * Defaults to 500
	 * @param silenceLevel
	 */
	public void setSilenceLevel(int silenceLevel) {
		this.silenceLevel = silenceLevel;
	}

	/**
	 * get the percentage of average peak to set decoding threshold to.
	 * when decoding, the signal must go above this level between zero-crossings in order to count
	 * as separate zeros.
	 * @return
	 */
	public double getMinLevelCoeff() {
		return minLevelCoeff;
	}

	/**
	 * 
	 * get the percentage of average peak to set decoding threshold to.
	 * when decoding, the signal must go above this level between zero-crossings in order to count
	 * as separate zeros.
	 * Defaults to 0.5
	 * 
	 * @param minLevelCoeff
	 * @throws IllegalArgumentException if passed a value outside of 0 to 1. fs
	 */
	public void setMinLevelCoeff(double minLevelCoeff) {
		this.minLevelCoeff = minLevelCoeff;
	}

	/**
	 * get whether currently recording
	 * @return
	 */
	public boolean isRecording(){
		return recording;
	}

	public void startRecording(){
		debug(TAG, "start recording");
		debug(TAG, "bufferSize: "+bufferSize);
    	audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
    			frequency, channelConfiguration,
    			audioEncoding, bufferSize);
    	audioRecord.startRecording();
    	recording = true;

	}

	public void stopRecording(){
		debug(TAG, "stop recording");
		if (audioRecord != null){
	    	audioRecord.stop();
	    	audioRecord.release();
	    	audioRecord = null;
		}
    	recording = false;
    }
	
	//begin monitoring mic input for > threshold values.  When one is detected, go to "record" mode
	public void monitor(){
        Message msg = Message.obtain();
        msg.what = MessageType.NO_DATA_PRESENT.ordinal();
        mHandler.sendMessage(msg);
    	short[] buffer = new short[bufferSize];
    	boolean silent = true;
    	short bufferVal;
    	boolean effectivelySilent;
        startRecording();
        int found = 0;
        int quorum = 5; //number of non-silent samples to find before we begin recording.
        int bufferReadResult = 0;
        while(silent && recording){
	    	bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
	    	found = 0;
	    	for (int i = 0; i < bufferReadResult; i++){
	    		bufferVal = buffer[i];
	    		debug(TAG, "monitor val:"+bufferVal+", found:"+found);
	    		effectivelySilent =Math.abs(bufferVal) < silenceLevel; 
	    		if (silent && !effectivelySilent){
		    		found++;
		    		if (found > quorum){
		    			silent = false;
		    			msg = Message.obtain();
		    			msg.what = MessageType.DATA_PRESENT.ordinal();
		    			mHandler.sendMessage(msg);
		    		}
	    		}else{ //need non-silent samples to be next to each other.
	    			found = 0;
	    		}
	    	}
        }
        if (!silent){
        	recordData(buffer, bufferReadResult); //pass because we're going to consider this part of the swipe
        }
	}
	
	private void recordData(short[] initialBuffer, int initialBufferSize){
		debug(TAG, "recording data");
        Message msg = Message.obtain();
    	// Create a DataOuputStream to write the audio data 
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	BufferedOutputStream bos = new BufferedOutputStream(os);
    	DataOutputStream dos = new DataOutputStream(bos);
		
    	short bufferVal;
    	short[] buffer = new short[bufferSize];
    	boolean effectivelySilent;
    	int silenceAtEndThreshold = frequency; //get one second of (near) silence
    	int silentSamples = 0;
    	int maxSamples = frequency * 10;
    	int totalSamples = 0;
    	boolean done = false; //have we recorded 1 second of silence
    	int bufferReadResult = 0;
    	try{
        	//copy stuff from intialBuffer to dos.
        	for (int i = 0; i < initialBufferSize; i++){
    			dos.writeShort(initialBuffer[i]);
        	}
        	int nonSilentAtEndFound = 0;
        	int quorum = 5;
	    	while(!done && recording && totalSamples < maxSamples){
		    	bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
		    	for (int i = 0; i < bufferReadResult; i++){
		    		bufferVal = buffer[i];
		    		effectivelySilent =Math.abs(bufferVal) < silenceLevel; 
	    			dos.writeShort(buffer[i]);
		    		if (effectivelySilent){
		    			nonSilentAtEndFound = 0;
		    			silentSamples++;
		    			if (silentSamples > silenceAtEndThreshold){
		    				done = true;
			    			msg = Message.obtain();
			    			msg.what = MessageType.NO_DATA_PRESENT.ordinal();
			    			mHandler.sendMessage(msg);
		    			}
		    		}else{
		    			nonSilentAtEndFound++;
		    			if (nonSilentAtEndFound > quorum){ //filter out noise blips
		    				silentSamples = 0;
		    			}
		    		}
		    		totalSamples++;
		    	}
	    		
	    	}
	    	dos.close();
	    	if (!recording){
	    		debug(TAG, "not recording after loop in recorddata, assuming aborted");
    			msg = Message.obtain();
    			msg.what = MessageType.NO_DATA_PRESENT.ordinal();
    			mHandler.sendMessage(msg);
    			return;
	    	}
	    	audioBytes = os.toByteArray(); 
	    	processData(audioBytes);
	    	
    	}catch(Exception e){
    		Log.e(TAG,"Recording Failed", e);
    		e.printStackTrace();
    		stopRecording();
			msg = Message.obtain();
			msg.what = MessageType.RECORDING_ERROR.ordinal();
			mHandler.sendMessage(msg);
    	}
		
	}

	private void processData(byte[] bytes) throws IOException{
		debug(TAG, "processing data");
        Message msg = Message.obtain();
		//first pass, iterate through bytes, get avg peak level
		//set minLevel to min% of avg peak
        SwipeData result = new SwipeData();
        result.setContent("Unevaluated.  This shouldn't happen");
        result.setBadRead();
		minLevel = getMinLevel(bytes, minLevelCoeff);
		//second pass, decode to bitset
		BitSet bits = decodeToBitSet(bytes);
		result = decodeToASCII(bits);
		if (result.isBadRead()){
			debug(TAG, "bad read, lets try it backwards");
			result = decodeToASCII(reverse(bits));
		}
		msg = Message.obtain();
		msg.what = MessageType.DECODED_TRACK.ordinal();
		result.raw = bytes;
		msg.obj = result;
		mHandler.sendMessage(msg);

	}
	
	private int getMinLevel(byte[]bytes, double coeff) throws IOException{
		short lastval = 0;
		short val = 0;
		int peakcount = 0;
		int peaksum = 0;
		int peaktemp = 0; //value to store highest peak value between zero crossings
    	InputStream is = new ByteArrayInputStream(audioBytes);
    	BufferedInputStream bis = new BufferedInputStream(is);
    	DataInputStream dis = new DataInputStream(bis);
    	boolean hitmin = false;
		while (dis.available() > 0) {
    		val = dis.readShort();
    		if (val > 0 && lastval <= 0){
    			//we're coming from negative to positive, reset peaktemp
    			peaktemp = 0;
    			hitmin = false;
    		}else if (val < 0 && lastval >= 0 && hitmin){
    			//we're going from positive to negative, so add peaktemp to peaksum
    			peaksum += peaktemp;
    			peakcount++;
    		}
    		if ((val > 0) &&(lastval > val) && (lastval > silenceLevel) && (val > peaktemp)){
    			//new peak, higher than last peak since zero
    			//debug(TAG, "Peak: "+lastval);
    			hitmin = true;
    			peaktemp = val;
    		}
    		lastval = val;
		}
		
		//The .3 in the following line is an arbitrary scaling factor.  
		//I have come up with it experimentally, but it can be changed to make the decode more or less noise sensitive
		if (peakcount > 0){
			int level =(int)Math.floor((peaksum / peakcount) * coeff); 
			debug(TAG, "returning "+level+" for minLevel");
			debug(TAG, "there were "+peakcount+" peaks");
			return level;
		}else{
			return silenceLevel;
		}
	}
		
	
	/* convert array of bytes representing sample levels to BitSet of bits representing 
	 * logical bits of stripe
	 * 
	 * @param byte[] bytes array of samples (shorts)
	 * @return BitSet BitSet representing logical signal
	 */
	public BitSet decodeToBitSet(byte[] bytes) throws IOException{
		debug(TAG, "bytes length: "+bytes.length);
		BitSet result = new BitSet(); //Todo: determine if setting initial capacity is worth it.
    	// Create a DataOuputStream to write the audio data 
    	InputStream is = new ByteArrayInputStream(bytes);
    	BufferedInputStream bis = new BufferedInputStream(is);
    	DataInputStream dis = new DataInputStream(bis);
    	//current sample index
    	int i = 0;
    	int resultBitCount = 0;
		int lastSign = -1;
		int lasti = 0;
    	short dp;
		int first = 0;
		int oneinterval = -1; //interval between transitions for a 1 bit.  There are two transitions per 1 bit, 1 per 0.
		//so if interval is around 15, then if the space between transitions is 17, 15, that's a 1.  but if that was 32, that'd be 0.
		//the pattern starts with a self-clocking set of 0s.  We'll discard the first few, just because.
		int introDiscard = 1;
		int discardCount = 0;
		boolean needHalfOne = false; //if the last interval was the first half of a 1, the next better be the second half
		int expectedParityBit = 1; //invert every 1 bit.  parity bit should make number of 1s in group odd.

		while (dis.available() > 0) {
    		dp = dis.readShort();
			if ((dp * lastSign < 0) && (Math.abs(dp) > minLevel)) {
				if (first == 0) {
					first = i;
					debug(TAG,"set first to: " + first);
				}else if (discardCount < introDiscard) {
					discardCount++;
				}else {
					int sinceLast = i - lasti;
					if (oneinterval == -1) {
						oneinterval = sinceLast/2;
					}else {
						boolean oz = isOne(sinceLast, oneinterval);
						//debug(TAG, "diff: " + sinceLast+ " oneinterval: "+oneinterval+"one?: " + oz);
						if (oz) {
							oneinterval = sinceLast;
							if (needHalfOne) {
								expectedParityBit = 1-expectedParityBit;
								//debug(TAG, "1");
								result.set(resultBitCount, true);
								resultBitCount++;
								needHalfOne = false; //don't need next to be
							}else {
								needHalfOne = true;
							}
						}else {
							oneinterval = sinceLast / 2;
							if (needHalfOne) {
								//debug(TAG, "result so far: " + result);
								break;
								//throw new Error("parse exception, did not get second half of expected 1 value");
							}else {
								//group+="0";
								//debug(TAG, "0");
								result.set(resultBitCount, false);
								resultBitCount++;
							}
						}
					}
				}
				lasti = i;
				lastSign *= -1;
			}
			i++;
    		
    	}
    	dis.close();
    	debug(TAG, "raw binary: "+dumpString(result));
		return result;
	}
	
	private SwipeData decodeToASCII(BitSet bits){
		SwipeData toreturn = new SwipeData();
		//get index of first 1
		int first1 = bits.nextSetBit(0);
		if (first1 < 0){
			debug(TAG, "no 1 bits detected.");
			toreturn.setBadRead();
			return toreturn;
		}
		debug(TAG, "first 1 bit is at position "+first1);
		int sentinel = 0;
		int exp = 0;
		int i = first1;
		//check for 5 bit sentinel
		for (; i < first1 + 4; i++){
			if (bits.get(i)){
				sentinel += 1 << exp; //lsb first. so with each following bit, shift it left 1 place.
			}
			exp++;
		}
		debug(TAG, "sentinel value for 4 bit:" + sentinel);
		if (sentinel == 11){ //11 is magic sentinel number for track 2.  corresponds to ascii ';' with offset 48 (ascii '0');
			return decodeToASCII(bits, first1, 4, 48);
		}else{
			for (; i < first1 + 6; i++){
				if (bits.get(i)){
					sentinel += 1 << exp;
				}
				exp++;
			}
			debug(TAG, "sentinel value for 6 bit:" + sentinel);
			if (sentinel == 5){ //5 is magic sentinel for track 1.  corresponds to ascii '%' with offset 32 (ascii space)
				return decodeToASCII(bits, first1, 6, 32);
			}
		}
		debug(TAG, "could not match sentinel value to either 11 or 5 magic values");
		toreturn.setBadRead();
		return toreturn;
	}
	
	/**
	 * Turn BitSet into String by decoding binary into ASCII characters
	 * @param bits BitSet of logical bits in swipe
	 * @param beginIndex int index of first 1 (start of sentinel)
	 * @param bitsPerChar int number of bits (not including parity bit) in character
	 * @return SwipeData
	 */
	public SwipeData decodeToASCII(BitSet bits, int beginIndex, int bitsPerChar, int baseChar){
		StringBuilder sb = new StringBuilder();
		SwipeData toreturn = new SwipeData();
		int i = beginIndex;
		char endSentinel = '?'; //for both!
		int charCount = 0;
		boolean sentinelFound = false;
		int size = bits.size(); //actual number of bits in bits, but we may not need all of them because we don't care after the end sentinel
		int letterVal = 0;
		char letter;
		boolean expectedParity;
		boolean bit;
		int exp;
		while((i < size) && !sentinelFound){
			letterVal = 0;
			expectedParity = true;
			exp = 0;
			int nextCharIndex = i + bitsPerChar;
			for (; i < nextCharIndex; i++){
				bit = bits.get(i);
				if (bit){
					letterVal += 1 << exp;
					expectedParity = !expectedParity;
				}
				exp++;
			}
			letter = decode(letterVal, baseChar);
			sb.append(letter);
			bit = bits.get(i);
			if (bit != expectedParity){
				toreturn.addBadCharIndex(charCount);
			}
			i++;
			charCount++;
			if (letter == endSentinel){
				sentinelFound = true;
			}
		}
		toreturn.setContent(sb.toString());
		return toreturn;
	}
   
    private char decode(int input, int baseChar){
    	//debug(TAG, "decode input: "+input);
    	char decoded = (char)(input + baseChar);
    	return decoded;
    }
    
	private boolean isOne(int actualInterval, int oneInterval) {
		int diffToOI = Math.abs(actualInterval - oneInterval);
		int diffToZI = Math.abs(actualInterval - (2 * oneInterval));
		if (diffToOI < diffToZI) {
			return true;
		}else {
			return false;
		}
	}
	
	private BitSet reverse(BitSet bits){
		int size = bits.size();
		BitSet toreturn = new BitSet(size);
		for (int i = 0; i < size; i++){
			toreturn.set(i, bits.get((size-1)-i));
		}
		//debug(TAG, "reversed BitSet: "+dumpString(toreturn));
		return toreturn;
	}
	
	private String dumpString(BitSet bits){
		StringBuilder sb = new StringBuilder();
		for (int i =0; i < bits.size(); i++){
			if (bits.get(i)){
				sb.append("1");
			}else{
				sb.append("0");
			}
		}
		return sb.toString();
	}
	
	private void debug(String tag, String message){
		if (debugging){
			Log.d(tag, message);
		}
	}

}
