package com.mipos.pojos;

import java.io.Serializable;

public class CreditCard implements Serializable {

	private static final long serialVersionUID = 1L;

	public String holdersName;
	public long cardNumber;
	public int expirationDate;
	public int cvc;
	
	public CreditCard(String holdersName, String cardNumber, String cardExpirationDate, String cardCvc) {
		this.holdersName = holdersName;
		this.cardNumber = Long.parseLong(cardNumber);
		this.expirationDate = Integer.parseInt(cardExpirationDate);
		this.cvc = Integer.parseInt(cardCvc);
	}
	
	public String getHoldersName() {
		return holdersName;
	}
	public void setHoldersName(String holdersName) {
		this.holdersName = holdersName;
	}
	public Long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(int expirationDate) {
		this.expirationDate = expirationDate;
	}
	public int getCvc() {
		return cvc;
	}
	public void setCvc(int cvc) {
		this.cvc = cvc;
	}
	
}
