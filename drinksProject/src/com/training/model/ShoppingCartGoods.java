package com.training.model;

public class ShoppingCartGoods {

	private long goodsID;
	private String goodsName;
	private int goodsPrice;
	private int buyQuantity;
	
	public ShoppingCartGoods(long goodsID, String goodsName, int goodsPrice, int buyQuantity) {
		super();
		this.goodsID = goodsID;
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
		this.buyQuantity = buyQuantity;
	}
	public long getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(long goodsID) {
		this.goodsID = goodsID;
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
}
