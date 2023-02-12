/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/

package com.training.service;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import com.training.dao.BackEndDao;
import com.training.model.Cond;
import com.training.model.Goods;
import com.training.vo.GoodsMaxMin;
import com.training.vo.GoodsResult;
import com.training.vo.SalesReport;

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
public class BackendService {

	private static BackendService bankService = new BackendService();

	private BackendService() {
	}

	private BackEndDao backEndDao = BackEndDao.getInstance();

	public static BackendService getInstance() {
		return bankService;
	}

	public Set<Goods> queryCond(Cond cond, int startRowNo, int endRowNo) {
		GoodsMaxMin gdmn = backEndDao.queryMaxMin();

		Integer GoodsID = cond.getGoodsID() == 0 ? cond.getGoodsID(): cond.getGoodsID();

		cond.setMinPrice(cond.getMinPrice() == 0 ? gdmn.getMinPrice() : cond.getMinPrice());
		cond.setMaxPrice(cond.getMaxPrice() == 0 ? gdmn.getMaxPrice() : cond.getMaxPrice());
		cond.setGoodsQuantity(cond.getGoodsQuantity() == 0 ? gdmn.getMaxGoodsQuantity() : cond.getGoodsQuantity());

		return backEndDao.queryCond(cond, startRowNo, endRowNo, GoodsID);
	}

	public GoodsResult searchAllGoods(String searchKeyword,int startRowNo, int endRowNo) {

		return backEndDao.searchAllGoods(searchKeyword,startRowNo, endRowNo);
	}

	public Set<Goods> queryGoods() {

		return backEndDao.queryGoods();
	}

	public int createGoods(Goods goods) {

		return backEndDao.createGoods(goods);
	}

	public boolean updateGoods(Goods goods) {
		Goods g = backEndDao.queryGoodsById(new BigDecimal(goods.getGoodsID()) );
		int a = g.getGoodsQuantity() + goods.getGoodsQuantity();
		goods.setGoodsQuantity(a);

		return backEndDao.updateGoods(goods);
	}


	public Goods queryGoodsById(BigDecimal goodsID) {
		return backEndDao.queryGoodsById(goodsID);
	}
	
	public boolean deleteGoods(BigDecimal goodsID) {
		return backEndDao.deleteGoods(goodsID);
	}
	
	public Set<SalesReport> queryAllOrder() {
		return backEndDao.queryAllOrder();
	}
	public Set<SalesReport> queryOrderBetweenDate(String queryStartDate, String queryEndDate) {
		if (queryStartDate.equals("") || queryEndDate.equals("")) {
			return new LinkedHashSet<>();
		} else {
			return backEndDao.queryOrderBetweenDate(queryStartDate, queryEndDate);
		}

	}

}
