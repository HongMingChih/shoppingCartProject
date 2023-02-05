/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/

package com.training.vo;

import java.util.Map;

import com.training.model.Goods;

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
public class BuyGoodsRtn {
	private int inputMoney;// 投入金額
	private int allMoney;// 總消費
	private int change;// 找零

	public BuyGoodsRtn() {
		super();
	}

	public int getInputMoney() {
		return inputMoney;
	}

	public void setInputMoney(int inputMoney) {
		this.inputMoney = inputMoney;
	}

	public int getAllMoney() {
		return allMoney;
	}

	public void setAllMoney(int allMoney) {
		this.allMoney = allMoney;
	}

	public int getChange() {
		return change;
	}

	public void setChange(int change) {
		this.change = change;
	}



}
