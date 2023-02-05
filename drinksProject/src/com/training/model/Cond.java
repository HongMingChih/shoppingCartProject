/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.model;

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
public class Cond {

private Integer pageNo;
private Integer goodsID;
private String goodsName;
private Integer minPrice;
private Integer maxPrice;
private String sortPrice;
private Integer goodsQuantity;
private String status;


public Cond(Integer minPrice, Integer maxPrice, Integer goodsQuantity) {
	super();
	this.minPrice = minPrice;
	this.maxPrice = maxPrice;
	this.goodsQuantity = goodsQuantity;
}
public Cond() {
	super();
}
public Integer getPageNo() {
	return pageNo;
}
public void setPageNo(Integer pageNo) {
	this.pageNo = pageNo;
}
public Integer getGoodsID() {
	return goodsID;
}
public void setGoodsID(Integer goodsID) {
	this.goodsID = goodsID;
}
public String getGoodsName() {
	return goodsName;
}
public void setGoodsName(String goodsName) {
	this.goodsName = goodsName;
}
public Integer getMinPrice() {
	return minPrice;
}
public void setMinPrice(Integer minPrice) {
	this.minPrice = minPrice;
}
public Integer getMaxPrice() {
	return maxPrice;
}
public void setMaxPrice(Integer maxPrice) {
	this.maxPrice = maxPrice;
}
public String getSortPrice() {
	return sortPrice;
}
public void setSortPrice(String sortPrice) {
	this.sortPrice = sortPrice;
}
public Integer getGoodsQuantity() {
	return goodsQuantity;
}
public void setGoodsQuantity(Integer goodsQuantity) {
	this.goodsQuantity = goodsQuantity;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}

@Override
public String toString() {
	return "FormCond [pageNo=" + pageNo + ", goodsID=" + goodsID + ", goodsName=" + goodsName + ", minPrice=" + minPrice
			+ ", maxPrice=" + maxPrice + ", sortPrice=" + sortPrice + ", goodsQuantity=" + goodsQuantity + ", status="
			+ status + "]";
}


}
