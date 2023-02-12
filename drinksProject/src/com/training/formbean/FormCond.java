/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.formbean;

import org.apache.struts.action.ActionForm;

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
public class FormCond extends ActionForm{

private Integer pageNo;
private Integer goodsID;	
private String goodsName;
private Integer minPrice;
private Integer maxPrice;
private String sortPrice;//排序方式
private Integer goodsQuantity;
private String status;

public FormCond(Integer pageNo, String goodsName, Integer minPrice, Integer maxPrice, String sortPrice,
		Integer goodsQuantity, String status) {
	super();
	this.pageNo = pageNo;
	this.goodsName = goodsName;
	this.minPrice = minPrice;
	this.maxPrice = maxPrice;
	this.sortPrice = sortPrice;
	this.goodsQuantity = goodsQuantity;
	this.status = status;
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
public FormCond() {
	super();
}
@Override
public String toString() {
	return "FormCond [pageNo=" + pageNo + ", goodsID=" + goodsID + ", goodsName=" + goodsName + ", minPrice=" + minPrice
			+ ", maxPrice=" + maxPrice + ", sortPrice=" + sortPrice + ", goodsQuantity=" + goodsQuantity + ", status="
			+ status + "]";
}


}
