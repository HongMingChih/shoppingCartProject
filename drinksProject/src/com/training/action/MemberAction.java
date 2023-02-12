/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/

package com.training.action;

import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;

import com.training.model.Goods;
import com.training.model.ShoppingCartGoods;
import com.training.service.FrontendService;
import com.training.vo.BuyGoodsRtn;
import com.training.vo.ShoppingCartGoodsInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
@MultipartConfig
public class MemberAction extends DispatchAction {

	private FrontendService frontendService = FrontendService.getInstance();

	/**
	 * 新增購物車
	 */
	public ActionForward addCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("addCartGoods...");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		List<ShoppingCartGoods> shoppingCartGoods = new ArrayList<>();
		HttpSession session = request.getSession();
		Integer allPrice= session.getAttribute("allPriceSession")==null?0:(Integer) session.getAttribute("allPriceSession");
		// 商品加入購物車
		String goodsID = null;
		String buyQuantity = null;
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(request);
		while (iter.hasNext()) {
			FileItemStream item = iter.next();
			String name = item.getFieldName();
			InputStream stream = item.openStream();
			if (item.isFormField()) {
				String value = Streams.asString(stream);
				// 如果是表單字段，處理表單字段數據
				if (name.equals("goodsID")) {
					goodsID = value;
				} else if (name.equals("buyQuantity")) {
					buyQuantity = value;
				}
			}
		}

		// 查詢資料庫商品並且加入購物車
		Goods goods = frontendService.queryGoodsById(goodsID);
		ShoppingCartGoods shopCG = null;
		if (Integer.parseInt(buyQuantity) > 0) {

			if (session.getAttribute("carGoods") == null) {
				Map<Goods, Integer> carGoods = new LinkedHashMap<>();
				carGoods.put(goods, Integer.parseInt(buyQuantity));
				session.setAttribute("carGoods", carGoods);
				shopCG = new ShoppingCartGoods(goods.getGoodsID().longValue(), goods.getGoodsName(),
						goods.getGoodsPrice(), Integer.parseInt(buyQuantity));
				allPrice+=goods.getGoodsPrice()*Integer.parseInt(buyQuantity);
			} else {
				Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
				Map<Goods, Integer> newCarGoods = new LinkedHashMap<>(carGoods);
				boolean flag = false;
				for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
					Goods g = entry.getKey();
					if (g.getGoodsID().equals(goods.getGoodsID())) {
						int currentQuantity = entry.getValue();
						newCarGoods.put(g, currentQuantity + Integer.parseInt(buyQuantity));
						flag = true;
						shopCG = new ShoppingCartGoods(goods.getGoodsID().longValue(), goods.getGoodsName(),
								goods.getGoodsPrice(), currentQuantity + Integer.parseInt(buyQuantity));
						break;
					}
				}
				if (!flag) {
					newCarGoods.put(goods, Integer.parseInt(buyQuantity));
					shopCG = new ShoppingCartGoods(goods.getGoodsID().longValue(), goods.getGoodsName(),
							goods.getGoodsPrice(), Integer.parseInt(buyQuantity));
				}
				session.setAttribute("carGoods", newCarGoods);
				allPrice=frontendService.allMoney(newCarGoods);
				
			} // if

			shoppingCartGoods.add(shopCG);
		}
		
		
			
			session.setAttribute("allPriceSession", allPrice);
		
		
		out.println(JSONArray.fromObject(shoppingCartGoods));
		out.flush();
		out.close();
//		ActionForward actionForward = mapping.findForward("addCartGood");
		return null;
	}

	/**
	 * 查詢目前購物車 價格計算以最新價格為主
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("queryCartGoods...");
		ShoppingCartGoodsInfo cartGoodsInfo = new ShoppingCartGoodsInfo();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		ShoppingCartGoods sCG = null;
		Set<ShoppingCartGoods> shoppingCartGoods = new LinkedHashSet<>();

		HttpSession session = request.getSession();
		Map<Goods, Integer> carGoods = null;
		if (session.getAttribute("carGoods") == null) {
			carGoods = new LinkedHashMap<>();
		} else {
			carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
		}
		int allPrice = 0;
		Goods goods = null;

		for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
			goods = entry.getKey();
			entry.getValue();
			Goods good = frontendService.queryGoodsById(goods.getGoodsID().toString());
			goods.setGoodsPrice(good.getGoodsPrice());
			allPrice += good.getGoodsPrice() * entry.getValue();
//			System.out.println("購買商品: "+goods.getGoodsName()+
//							   " 商品價格: "+goods.getGoodsPrice()+
//							   " 購買數量: "+entry.getValue());
			sCG = new ShoppingCartGoods(goods.getGoodsID().longValue(), goods.getGoodsName(), goods.getGoodsPrice(),
					entry.getValue());
			shoppingCartGoods.add(sCG);
		}
//			System.out.println("總消費金額: "+allPrice);
		session.setAttribute("carGoods", carGoods);
//		request.setAttribute("allPrice", allPrice);
		session.setAttribute("allPriceSession", allPrice);

		cartGoodsInfo.setTotalAmount(allPrice);

		cartGoodsInfo.setShoppingCartGoods(shoppingCartGoods);
		out.println(JSONObject.fromObject(cartGoodsInfo));
		out.flush();
		out.close();
//		ActionForward actionForward = mapping.findForward("queryCartGood");
		return null;
	}

	/**
	 * 清空購物車
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward clearCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("clearCartGoods...");
		// 清空購物車
		HttpSession session = request.getSession();
		session.removeAttribute("carGoods");
		session.removeAttribute("allPriceSession");
		session.removeAttribute("buyGoodsList");
		ActionForward actionForward = mapping.findForward("addCartGood");
		return actionForward;
	}

	public ActionForward goCart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("goCart...");
		HttpSession session = request.getSession();
		int allPrice = 0;
		Map<Goods, Integer> carGoods = null;
		if (session.getAttribute("carGoods") == null) {
			carGoods = new LinkedHashMap<>();
		} else {
			carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
			for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
				Goods goods = entry.getKey();
				allPrice += goods.getGoodsPrice() * entry.getValue();
			}
		}
		session.setAttribute("carGoods", carGoods);
		session.setAttribute("allPriceSession", allPrice);
		ActionForward actionForward = mapping.findForward("queryCartGood");
		return actionForward;
	}

	public ActionForward increaseQuantity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("increaseQuantity...");
		HttpSession session = request.getSession();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		// 取得商品編號
		String goodsID = null;
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(request);
		while (iter.hasNext()) {
			FileItemStream item = iter.next();
			String name = item.getFieldName();
			InputStream stream = item.openStream();
			if (item.isFormField()) {
				String value = Streams.asString(stream);
				// 如果是表單字段，處理表單字段數據
				if (name.equals("goodsID")) {
					goodsID = value;
				}
			}
		}
		Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
		for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
			Goods goods = entry.getKey();
			if (goods.getGoodsID().equals(Integer.parseInt(goodsID)) && entry.getValue() + 1 <= 20) {
				carGoods.put(goods, entry.getValue() + 1);
			}

		}

		session.setAttribute("carGoods", carGoods);
		out.flush();
		out.close();
		return null;
	}

	public ActionForward decreaseQuantity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("decreaseQuantity...");
		HttpSession session = request.getSession();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		// 取得商品編號
		String goodsID = null;
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(request);
		while (iter.hasNext()) {
			FileItemStream item = iter.next();
			String name = item.getFieldName();
			InputStream stream = item.openStream();
			if (item.isFormField()) {
				String value = Streams.asString(stream);
				// 如果是表單字段，處理表單字段數據
				if (name.equals("goodsID")) {
					goodsID = value;
				}
			}
		}
		Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
		int allPrice = (int) session.getAttribute("allPriceSession");
		BuyGoodsRtn buyGoodsRtn= (BuyGoodsRtn) session.getAttribute("buyGoodsRtn");
		for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
			Goods goods = entry.getKey();
			if (goods.getGoodsID().equals(Integer.parseInt(goodsID)) && entry.getValue() - 1 > 0) {
				carGoods.put(goods, entry.getValue() - 1);
			}

		}
		session.setAttribute("carGoods", carGoods);
		out.flush();
		out.close();
		return null;
	}
	public ActionForward deleteGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("deleteGoods...");
		HttpSession session = request.getSession();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		// 取得商品編號
		String goodsID = null;
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(request);
		while (iter.hasNext()) {
			FileItemStream item = iter.next();
			String name = item.getFieldName();
			InputStream stream = item.openStream();
			if (item.isFormField()) {
				String value = Streams.asString(stream);
				// 如果是表單字段，處理表單字段數據
				if (name.equals("goodsID")) {
					goodsID = value;
				}
			}
		}
		Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
		int allPrice = (int) session.getAttribute("allPriceSession");
		BuyGoodsRtn buyGoodsRtn= (BuyGoodsRtn) session.getAttribute("buyGoodsRtn");
		for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
			Goods goods = entry.getKey();
			Integer buyQuantity=entry.getValue();
			if (goods.getGoodsID().equals(Integer.parseInt(goodsID))) {
				int thisPrice=goods.getGoodsPrice()*buyQuantity;
				allPrice-=thisPrice;
				carGoods.remove(goods);
				session.setAttribute("allPriceSession", allPrice);
				session.setAttribute("carGoods", carGoods);
				
				out.flush();
				out.close();
		        break;
			}

		}
		
		
		return null;
	}
}
