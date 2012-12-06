package com.mipos.pojos;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductForSale implements Serializable {

	private static final long serialVersionUID = 1L;
	String code;
	BigDecimal price;
	int quantity;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
