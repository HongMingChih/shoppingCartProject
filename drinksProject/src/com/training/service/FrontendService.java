/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/

package com.training.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.training.dao.FrontEndDao;
import com.training.model.Goods;
import com.training.model.Member;
import com.training.vo.GoodsResult;

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
public class FrontendService {
	private static FrontendService frontendService = new FrontendService();

	private FrontendService() {
	}

	private FrontEndDao frontEndDao = FrontEndDao.getInstance();

	public Member register(Member m) {
		Member member = new Member();
		member = frontEndDao.queryMemberByIdentificationNo(m.getIdentificationNo());
		if (null != member) {
			return new Member();
		} else {
			return frontEndDao.register(m);
		}

	}

	public Goods queryGoodsById(String goodsIDs) {

		return frontEndDao.queryGoodsById(goodsIDs);
	}

	public Member queryMember(Member member) {

		return frontEndDao.queryMember(member);
	}

	public int allMoney(Map<Goods, Integer> goodsOrders) {
		int money=0;
//		money=g.getGoodsPrice()*buyQuantity;
		for (Map.Entry<Goods, Integer> entry : goodsOrders.entrySet()) {
		    Goods goods = entry.getKey();
		    int buyQuantity = entry.getValue();
		    money+=goods.getGoodsPrice()*buyQuantity;
		}
		
		return money;
	}

	public static FrontendService getInstance() {
		return frontendService;
	}

	public Member queryMemberByIdentificationNo(String identificationNo) {

		return frontEndDao.queryMemberByIdentificationNo(identificationNo);
	}

	public GoodsResult searchGoods(String searchKeyword, int startRowNo, int endRowNo) {
		
		return frontEndDao.searchGoods(searchKeyword, startRowNo, endRowNo);
	}

	public Map<BigDecimal, Goods> queryBuyGoods(Set<BigDecimal> goodsIDs) {
	
		return frontEndDao.queryBuyGoods(goodsIDs);

	}

	public boolean batchUpdateGoodsQuantity(Set<Goods> goods) {

		return frontEndDao.batchUpdateGoodsQuantity(goods);
	}

	public boolean batchCreateGoodsOrder(String customerID, Map<Goods, Integer> goodsOrders) {

		return frontEndDao.batchCreateGoodsOrder(customerID, goodsOrders);
	}
}
