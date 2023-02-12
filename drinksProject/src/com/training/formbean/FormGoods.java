/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/

package com.training.formbean;

import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.struts.action.ActionForm;

/**
 *
 *
 *
 *
 * <br>
 * author: MingChih Hong
 * 
 * @since 11.0<br>
 *        TODO:
 *
 */
public class FormGoods extends ActionForm {
	private Integer goodsID;
	private String goodsName;
	private int goodsPrice;
	private int goodsQuantity;
	private String goodsImageName;
	private InputStream goodsImage;
	private String status;

	public FormGoods(Integer goodsID, String goodsName, int goodsPrice, int goodsQuantity, String goodsImageName,
			InputStream goodsImage, String status) {
		super();
		this.goodsID = goodsID;
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
		this.goodsQuantity = goodsQuantity;
		this.goodsImageName = goodsImageName;
		this.goodsImage = goodsImage;
		this.status = status;
	}

	public FormGoods() {
		super();
	}

	public InputStream getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(InputStream goodsImage) {
		this.goodsImage = goodsImage;
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

	public int getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(int goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getGoodsQuantity() {
		return goodsQuantity;
	}

	public void setGoodsQuantity(int goodsQuantity) {
		this.goodsQuantity = goodsQuantity;
	}

	public String getGoodsImageName() {
		return goodsImageName;
	}

	public void setGoodsImageName(String goodsImageName) {
		this.goodsImageName = goodsImageName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Goods [goodsID=" + goodsID + ", goodsName=" + goodsName + ", goodsPrice=" + goodsPrice
				+ ", goodsQuantity=" + goodsQuantity + ", goodsImageName=" + goodsImageName + ", status=" + status
				+ "]";
	}
}
