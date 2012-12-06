package com.mipos.pojos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Sale implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	List<ProductForSale> productForSaleList;
	BigDecimal totalCartAmount;
	ArrayList<PaymentMethod> paymentMethodList;
	Client client;
	CreditCard creditCard;
	SerializedBitmap signature;
	NFCData nfcData;
	
	public Sale() {
		totalCartAmount = new BigDecimal("0");
		paymentMethodList = new ArrayList<PaymentMethod>();
		signature = null;
	}
	
	public List<ProductForSale> getProductForSaleList() {
		return productForSaleList;
	}
	
	public void setProductForSaleList(List<ProductForSale> productForSaleList) {
		this.productForSaleList = productForSaleList;
	}
	
	public BigDecimal getTotalCartAmount() {
		return totalCartAmount;
	}
	
	public void setTotalCartAmount(BigDecimal totalAmount) {
		this.totalCartAmount = totalAmount;
	}
	
	public ArrayList<PaymentMethod> getPaymentMethodList() {
		return paymentMethodList;
	}
	
	public void setPaymentMethodList(ArrayList<PaymentMethod> paymentMethodList) {
		this.paymentMethodList = paymentMethodList;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public SerializedBitmap getSignature() {
		return signature;
	}

	public void setSignature(SerializedBitmap signature) {
		this.signature = signature;
	}

	public NFCData getNfcData() {
		return nfcData;
	}

	public void setNfcData(NFCData nfcData) {
		this.nfcData = nfcData;
	}

}
