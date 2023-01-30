/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
*
*
*
*
*<br>author: MingChih Hong
*@since 11.0<br>
*TODO:
*
*/
public class GoodsOrder {
	private BigDecimal goodsID;
	private Date orderDate;
	private String customerName;
	private String goodsName;
	private int goodsPrice;
	private int buyQuantity;
	
	public BigDecimal getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(BigDecimal goodsID) {
		this.goodsID = goodsID;
	}
	public GoodsOrder(Date orderDate, String customerName, String goodsName, int goodsPrice, int buyQuantity) {
		super();
		this.orderDate = orderDate;
		this.customerName = customerName;
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
		this.buyQuantity = buyQuantity;
	}
	public GoodsOrder() {
		super();
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(int goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getBuyQuantity() {
		return buyQuantity;
	}
	public void setBuyQuantity(int buyQuantity) {
		this.buyQuantity = buyQuantity;
	}
	@Override
	public String toString() {
		return "GoodsOrder [goodsID=" + goodsID + ", orderDate=" + orderDate + ", customerName=" + customerName
				+ ", goodsName=" + goodsName + ", goodsPrice=" + goodsPrice + ", buyQuantity=" + buyQuantity + "]";
	}
	
	
}
