package com.mipos.pojos;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentMethod implements Serializable {

	private static final long serialVersionUID = 1L;
	String paymentType;
	BigDecimal amount;
	
	public String getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
