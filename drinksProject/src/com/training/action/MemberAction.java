/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/

package com.training.action;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;

import com.training.formbean.FormCond;
import com.training.model.Cond;
import com.training.model.Goods;
import com.training.service.BackendService;
import com.training.service.FrontendService;

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
	private BackendService backendService = BackendService.getInstance();

	private FrontendService frontendService = FrontendService.getInstance();

	/**
	 * 
	 */
	public ActionForward addCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("addCartGoods...");
		// 商品加入購物車
		String goodsID = null;
		String buyQuantity = null;
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(request);
		ActionForward actionForward = null;
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
		
		HttpSession session = request.getSession();
		
		if (Integer.parseInt(buyQuantity)>0) {
			
			if (session.getAttribute("carGoods") == null) {
				Map<Goods, Integer> carGoods = new LinkedHashMap<>();
				carGoods.put(goods, Integer.parseInt(buyQuantity));
				session.setAttribute("carGoods", carGoods);
			} else {
				Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
				Map<Goods, Integer> newCarGoods = new LinkedHashMap<>(carGoods);
				boolean flag = false;
				for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
					Goods g = entry.getKey();
					if (g.getGoodsID().equals(goods.getGoodsID())) {
						Goods good=frontendService.queryGoodsById(g.getGoodsID().toString());
						if (good.getGoodsQuantity()>=entry.getValue()+Integer.parseInt(buyQuantity)) {
							int currentQuantity = entry.getValue();
							newCarGoods.put(g, currentQuantity + Integer.parseInt(buyQuantity));
							flag = true;
						}else {
							System.out.println("庫存不足!!");
							return actionForward;
						}
						break;
					}
				}
				if (!flag) {
					newCarGoods.put(goods, Integer.parseInt(buyQuantity));
				}
				session.setAttribute("carGoods", newCarGoods);
		}//if

		}

		actionForward = mapping.findForward("addCartGood");
		return actionForward;
	}

	public ActionForward queryCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("queryCartGoods...");

		// 查詢資料庫商品並且加入購物車
//		Goods goods = frontendService.queryGoodsById(goodsID);
//		
//		Map<Goods, Integer> carGoods = new LinkedHashMap<>();
//		carGoods.put(goods, Integer.parseInt(buyQuantity));

		HttpSession session = request.getSession();
		Map<Goods, Integer> carGoods = null;
		if (session.getAttribute("carGoods") == null) {
			carGoods = new LinkedHashMap<>();
		} else {
			carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
		}
		int allPrice=0;
		Goods goods=null;
		
		for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
			 goods=entry.getKey();
			 entry.getValue();
			allPrice+=goods.getGoodsPrice()*entry.getValue();
			System.out.println("購買商品: "+goods.getGoodsName()+
							   " 商品價格: "+goods.getGoodsPrice()+
							   " 購買數量: "+entry.getValue());
		}
			System.out.println("總消費金額: "+allPrice);
			request.setAttribute("carGoods", carGoods);
			request.setAttribute("allPrice", allPrice);
			session.setAttribute("allPriceSession", allPrice);
		ActionForward actionForward = mapping.findForward("queryCartGood");
		return actionForward;
	}

	public ActionForward clearCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("clearCartGoods...");
		// 清空購物車
		HttpSession session = request.getSession();
		session.removeAttribute("carGoods");
		ActionForward actionForward = mapping.findForward("addCartGood");
		return actionForward;
	}

}
