package com.mipos.utils;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransferBigDecimalObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	BigDecimal number;
	
	public TransferBigDecimalObject(BigDecimal number) {
		this.number = number;
	}
	
	public BigDecimal getNumber() {
		return number;
	}
}
