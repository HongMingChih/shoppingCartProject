/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.formbean.FormMember;
import com.training.model.Goods;
import com.training.model.Member;
import com.training.service.BackendService;
import com.training.service.FrontendService;
import com.training.vo.GoodsOrder;

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
public class FrontendAction extends DispatchAction {
	
	private FrontendService frontendService=FrontendService.getInstance();
	
	public ActionForward buyGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("buyGoods...");
		String customerID=request.getParameter("customerID");
		int inputMoney= Integer.parseInt(request.getParameter("inputMoney"));
		String[] goodsIDs= request.getParameterValues("goodsIDs");
		String[] buyQuantitys= request.getParameterValues("buyQuantitys");
		int change=0;
		Map<Goods, Integer> goodsOrders = new HashMap<>();
		for (int i = 0; i < goodsIDs.length; i++) {
			if (Integer.parseInt(buyQuantitys[i])!=0) {
				// 3.前臺顧客購買建立訂單
				// Step1:查詢顧客所購買商品資料(價格、庫存)
				Set<BigDecimal> goodsID = new HashSet<>();
				goodsID.add(new BigDecimal(goodsIDs[i]));
//				allMoney+=frontendService.allMoney(buyGoods,Integer.parseInt(buyQuantitys[i]));
//				buyGoods.values().stream().forEach(g -> System.out.println(g));	
				int buyQuantity=Integer.parseInt(buyQuantitys[i]);
				goodsID.stream().forEach(goodsId -> {
					Goods g = frontendService.queryGoodsById(goodsId.toString());
					if (g.getGoodsQuantity()>=buyQuantity) {
						goodsOrders.put(g, buyQuantity); 
					}else {
						System.out.println("庫存不足: "+g.getGoodsName()+" 剩餘庫存: "+g.getGoodsQuantity());
					}
				});
			}
		}
		int allMoney=frontendService.allMoney(goodsOrders);
		change=inputMoney>=allMoney?change=inputMoney-allMoney:allMoney;
		System.out.println("投入金額"+inputMoney);
		System.out.println("購買金額"+allMoney);
		System.out.println("找零金額"+change);
		if (inputMoney>allMoney) {
			for (Map.Entry<Goods, Integer> entry : goodsOrders.entrySet()) {
				Goods goods = entry.getKey();
				System.out.println("商品名稱:"+goods.getGoodsName()+
						" 商品金額:"+goods.getGoodsPrice()+
						" 購買數量:"+entry.getValue());
			}
		}
		
		// 建立訂單
		boolean insertSuccess = frontendService.batchCreateGoodsOrder(customerID, goodsOrders);
		if(insertSuccess){System.out.println("建立訂單成功!");}
		// Step3:交易完成更新扣商品庫存數量
		// 將顧客所購買商品扣除更新商品庫存數量
		goodsOrders.forEach((goods, buyQuantity) -> 
		{goods.setGoodsQuantity(goods.getGoodsQuantity() - buyQuantity);});
		boolean updateSuccess = frontendService.batchUpdateGoodsQuantity(goodsOrders.keySet().stream().collect(Collectors.toSet()));
		if(updateSuccess){System.out.println("商品庫存更新成功!");}
		
		
		
		
		ActionForward actionForward=mapping.findForward("vendingBuyView");
		return actionForward;
	}
	
	public ActionForward searchGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("queryGoods...");
		String searchKeyword=request.getParameter("searchKeyword");
		searchKeyword=searchKeyword==null?"":searchKeyword;
		
		int pageNo=Integer.parseInt(request.getParameter("pageNo"));
//		0-7  6-13  12-19 18-25
//		1     2      3     4
//	    7
	    int	endRowNo=(pageNo*6)+1;
	    int	startRowNo=endRowNo-7;
	    Set<Goods> searchGoods=frontendService.searchGoods(searchKeyword, startRowNo, endRowNo);
	    for (Goods goods : searchGoods) {
			System.out.println(goods.toString());
		}
		
		ActionForward actionForward=mapping.findForward("vendingView");
		return actionForward;
	}
	
	
	public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		// 將表單資料使用 struts ActionForm 方式自動綁定，省去多次由 request getParameter 取表單資料的工作
		FormMember formMember=(FormMember) form;
		
		// 將Struts FormMember 資料複製 member
		// 將表單資料轉換儲存資料物件(commons-beanutils-1.8.0.jar)
		Member member =new Member();
		BeanUtils.copyProperties(member,formMember);
		Member acc_verification=frontendService.queryMember(member);
		
		String message = acc_verification!=null ? "帳戶登入成功！" : "帳戶登入失敗！";
		System.out.println(message);
		ActionForward actionForward= acc_verification!=null ? mapping.findForward("loginSuccess") : mapping.findForward("loginError");
		return actionForward;
	}
	
	public ActionForward register(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		// 將表單資料使用 struts ActionForm 方式自動綁定，省去多次由 request getParameter 取表單資料的工作
				FormMember formMember=(FormMember) form;
		// 將Struts BackedActionForm 資料複製 Goods
		// 將表單資料轉換儲存資料物件(commons-beanutils-1.8.0.jar)
		Member member =new Member();
		BeanUtils.copyProperties(member,formMember);
		Member acc_verification=frontendService.register(member);
		
		String message = acc_verification.getCustomerName()!=null ? "帳戶創建成功！" : "帳戶名稱已重複！";
		System.out.println(message);
		
		ActionForward actionForward=mapping.findForward("loginError");
		return actionForward;
	}
	
	
}
