/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.vo;
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
public class GoodsMaxMin {
private Integer maxPrice;
private Integer minPrice;
private Integer maxGoodsQuantity;


public GoodsMaxMin() {
	super();
}
public Integer getMaxPrice() {
	return maxPrice;
}
public void setMaxPrice(Integer maxPrice) {
	this.maxPrice = maxPrice;
}
public Integer getMinPrice() {
	return minPrice;
}
public void setMinPrice(Integer minPrice) {
	this.minPrice = minPrice;
}
public Integer getMaxGoodsQuantity() {
	return maxGoodsQuantity;
}
public void setMaxGoodsQuantity(Integer maxGoodsQuantity) {
	this.maxGoodsQuantity = maxGoodsQuantity;
}
@Override
public String toString() {
	return "goodsMaxMin [maxPrice=" + maxPrice + ", minPrice=" + minPrice + ", maxGoodsQuantity=" + maxGoodsQuantity
			+ "]";
}


}
