/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.model.Goods;
import com.training.service.FrontendService;
import com.training.vo.BuyGoodsRtn;
import com.training.vo.GoodsResult;

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
@MultipartConfig
public class FrontendAction extends DispatchAction {
	
	private FrontendService frontendService=FrontendService.getInstance();
	//訂單傳送訊息清單
		public ActionForward vendingBuyViewMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
				HttpServletResponse response)
				throws Exception {
			System.out.println("vendingBuyViewMessage...");
//			BuyGoodsRtn buyGoodsRtn= (BuyGoodsRtn) request.getAttribute("buyGoodsRtn");
//			Map<Goods, Integer> buyGoods=  (Map<Goods, Integer>) request.getAttribute("buyGoodsList");
//			
//			request.setAttribute("buyGoodsRtn", buyGoodsRtn);
//			request.setAttribute("buyGoodsList", buyGoods);
			ActionForward actionForward=mapping.findForward("vendingBuyMessageView");
			return actionForward;
		}
		
		/**
		 * 建立訂單 並更改資料庫
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
	public ActionForward buyGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("buyGoods...");
		HttpSession session = request.getSession();
		ActionForward actionForward=null;
		String customerID=request.getParameter("customerID");
		int inputMoney= Integer.parseInt(request.getParameter("inputMoney"));
		Integer allPrice= session.getAttribute("allPriceSession")==null?0:(Integer) session.getAttribute("allPriceSession");

		BuyGoodsRtn buyGoodsRtn=new BuyGoodsRtn();
		buyGoodsRtn.setInputMoney(inputMoney);//輸入金額
		buyGoodsRtn.setAllMoney(allPrice);//總消費金額
		
		
		
		if (inputMoney<allPrice||inputMoney<=0) {
			session.setAttribute("message", "輸入金額不足，請投入正確金額!!");
			int change=inputMoney>=allPrice?change=inputMoney-allPrice:inputMoney;//找零
			buyGoodsRtn.setChange(change);	
			session.setAttribute("buyGoodsRtn", buyGoodsRtn);
			actionForward=mapping.findForward("vendingBuyMessage");
			return actionForward;
		}
		if (session.getAttribute("carGoods") == null||allPrice==0) {
			session.setAttribute("message", "您還沒選購商品!!");
			int change=inputMoney>=allPrice?change=inputMoney-allPrice:inputMoney;//找零
			buyGoodsRtn.setChange(change);	
			session.setAttribute("buyGoodsRtn", buyGoodsRtn);
			actionForward=mapping.findForward("vendingBuyMessage");
			return actionForward;
		}else {
			
			Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
			Map<Goods, Integer> buyGoods=new LinkedHashMap<>();
//			int allMoney=0;
//			allMoney+=allPrice;
			for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
				Goods goods = entry.getKey();
				Goods queryGoods=frontendService.queryGoodsById(goods.getGoodsID().toString());
				if (queryGoods.getGoodsQuantity()>= entry.getValue()) {
					int change=inputMoney>=allPrice?change=inputMoney-allPrice:inputMoney;//找零
					buyGoodsRtn.setChange(change);	
					goods.setGoodsQuantity(goods.getGoodsQuantity()-entry.getValue());
					buyGoods.put(goods,entry.getValue());
				}else {
					String message="目前商品庫存不足將取消選購: 請確認購買數量重新選取";
//					System.out.println("目前商品庫存不足: "+goods.getGoodsName()+" 剩餘庫存: "+goods.getGoodsQuantity());
					
					session.setAttribute("message", message);
					buyGoodsRtn.setChange(inputMoney);
					buyGoodsRtn.setAllMoney(allPrice);
					session.setAttribute("buyGoodsRtn", buyGoodsRtn);
					
					session.removeAttribute("carGoods");
					session.removeAttribute("allPriceSession");	
					actionForward=mapping.findForward("vendingBuyMessage");
					return actionForward;
				}
			}
			// 建立訂單
			boolean insertSuccess = frontendService.batchCreateGoodsOrder(customerID, buyGoods);
			if(insertSuccess){System.out.println("建立訂單成功!");}
			// Step3:交易完成更新扣商品庫存數量
			// 將顧客所購買商品扣除更新商品庫存數量
			boolean updateSuccess = frontendService.batchUpdateGoodsQuantity(buyGoods.keySet().stream().collect(Collectors.toSet()));
			if(updateSuccess){System.out.println("商品庫存更新成功!");}
			
			session.setAttribute("message", "謝謝光臨!!");
			session.setAttribute("buyGoodsRtn", buyGoodsRtn);
			session.setAttribute("buyGoodsList", buyGoods);
			session.removeAttribute("carGoods");
			session.removeAttribute("allPriceSession");	
		}
		
	
			 
		actionForward=mapping.findForward("vendingBuyMessage");
		
		return actionForward;
	}
	
	public ActionForward searchGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("queryGoods...");
		String searchKeyword=request.getParameter("searchKeyword");
		searchKeyword=searchKeyword==null?"":searchKeyword;
		
		int pageNo=request.getParameter("pageNo")==null?1:Integer.parseInt(request.getParameter("pageNo"));
//		0-7  6-13  12-19 18-25
//		1     2      3     4
//	    7
	    int	endRowNo=(pageNo*6)+1;
	    int	startRowNo=endRowNo-7;
	    GoodsResult goodsResult=frontendService.searchGoods(searchKeyword, startRowNo, endRowNo);
	    Set<Goods> searchGoods=goodsResult.getGoods();
	    for (Goods goods : searchGoods) {
			System.out.println(goods.toString());
		}
	    int totalPages = goodsResult.getTotalRecords()!=0?(goodsResult.getTotalRecords() + 5) / 6:0;
	    request.setAttribute("searchGoods", searchGoods);
	    request.setAttribute("totalPages", totalPages);
	    request.setAttribute("searchKeyword", searchKeyword);
	    request.setAttribute("pageNo", pageNo);
		ActionForward actionForward=mapping.findForward("vendingView");
		return actionForward;
	}
	
	
	
	
	
	
}
