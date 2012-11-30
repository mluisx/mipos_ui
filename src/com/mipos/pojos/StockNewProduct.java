package com.mipos.pojos;

import java.math.BigDecimal;

public class StockNewProduct {
	String code;
	String description;
	BigDecimal price;
	int quantity;
	String category;
	byte[] pictureData;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return code + category;
	}

	public byte[] getPictureData() {
		return pictureData;
	}
	public void setPictureData(byte[] pictureData) {
		this.pictureData = pictureData;
	}
}
