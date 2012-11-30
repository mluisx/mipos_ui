package com.mipos.activities;

import com.mipos.dummies.BluetoothConnect;

import android.app.Application;

public class MiPosApp extends Application {
	
    private BluetoothConnect btConnect = null;
    private int number = 1;
 
    @Override
    public void onCreate() {
        super.onCreate();
    }

	public BluetoothConnect getBtConnect() {
		return btConnect;
	}

	public void setBtConnect(BluetoothConnect btConnect) {
		this.btConnect = btConnect;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}