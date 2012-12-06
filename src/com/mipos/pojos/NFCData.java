package com.mipos.pojos;

import java.io.Serializable;
import java.math.BigDecimal;

public class NFCData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	String NFCType;
	BigDecimal balanceBeforeDebit;
	BigDecimal balanceAfterDebit;
	
	public String getNFCType() {
		return NFCType;
	}
	
	public void setNFCType(String nFCType) {
		NFCType = nFCType;
	}
	
	public BigDecimal getBalanceBeforeDebit() {
		return balanceBeforeDebit;
	}
	
	public void setBalanceBeforeDebit(BigDecimal balanceBeforeDebit) {
		this.balanceBeforeDebit = balanceBeforeDebit;
	}
	
	public BigDecimal getBalanceAfterDebit() {
		return balanceAfterDebit;
	}
	
	public void setBalanceAfterDebit(BigDecimal balanceAfterDebit) {
		this.balanceAfterDebit = balanceAfterDebit;
	}
	
}
