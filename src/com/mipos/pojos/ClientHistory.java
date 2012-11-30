package com.mipos.pojos;

public class ClientHistory {
	
	int clientId;
	int saleId;
	
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getSaleId() {
		return saleId;
	}
	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}
	
	@Override
	public String toString() {
		return getClientId() + " - Sale Number:" + getSaleId();	
	}
	
}
