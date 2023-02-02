/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.formbean.FormCond;
import com.training.formbean.FormMember;
import com.training.model.Cond;
import com.training.model.Goods;
import com.training.service.BackendService;
import com.training.vo.SalesReport;

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
public class BackendAction extends DispatchAction{
	
	private BackendService backendService=BackendService.getInstance();
	/**
	 * 條件查詢
	 */
	public ActionForward conditionalGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("conditionalGoods...");
		
		
		// 將表單資料使用 struts ActionForm 方式自動綁定，省去多次由 request getParameter 取表單資料的工作
		FormCond formCond=(FormCond) form;
		Cond cond=new Cond();
		BeanUtils.copyProperties(cond, formCond);
//		cond.setGoodsName(new String(request.getParameter("goodsName").getBytes("ISO-8859-1"), "UTF-8"));
		 int endRowNo=(cond.getPageNo()*6)+1;
		 int startRowNo=endRowNo-7;
		Set<Goods> queryCond=backendService.queryCond(cond, startRowNo, endRowNo);
		System.out.println("共 "+queryCond.size()+" 件商品");
		 for (Goods goods : queryCond) {
				System.out.println(goods.toString());
			}
		 
		ActionForward actionForward=mapping.findForward("queryGoodView");
		return actionForward;
	}
	
	
	public ActionForward querySalesReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("querySalesReport...");
		String queryStartDate=request.getParameter("queryStartDate");
		String queryEndDate=request.getParameter("queryEndDate");
		
		Set<SalesReport> queryOrderBetweenDate=backendService.queryOrderBetweenDate(queryStartDate,queryEndDate);
		System.out.println("查詢訂單總數："+queryOrderBetweenDate.size());
		queryOrderBetweenDate.stream().forEach(g -> System.out.println(g));
		
		ActionForward actionForward=mapping.findForward("salesReportView");
		return actionForward;
	}
	
	public ActionForward updateGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("updateGoods...");
		Goods goods = new Goods();
		goods.setGoodsID(new BigDecimal(request.getParameter("goodsID")));
		goods.setGoodsPrice(Integer.parseInt(request.getParameter("goodsPrice")));
		goods.setGoodsQuantity(Integer.parseInt(request.getParameter("goodsQuantity")));
		goods.setStatus(request.getParameter("status"));
		boolean updateGood=backendService.updateGoods(goods);
		
		String message = updateGood ? "商品維護作業成功！" : "商品維護作業失敗！";
		System.out.println(message);
		
		ActionForward actionForward=mapping.findForward("updateGoodView");
		return actionForward;
	}
	//新增商品 圖片
	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("addGoods...");
		Goods goods=new Goods();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    List<FileItem> items = upload.parseRequest(request);
	    for (FileItem item : items) {
	        if (item.isFormField()) {
	            // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
	            String fieldName = item.getFieldName();
	            String fieldValue = item.getString();
	            if (fieldName.equals("goodsName")) {
	            	fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
	            	goods.setGoodsName(fieldValue);
	            } else if (fieldName.equals("goodsPrice")) {
	            	goods.setGoodsPrice(Integer.parseInt(fieldValue));
	            } else if (fieldName.equals("goodsQuantity")) {
	            	goods.setGoodsQuantity(Integer.parseInt(fieldValue));
	            } else if (fieldName.equals("status")) {
	            	goods.setStatus(fieldValue);
	            }
	        } else {
	            // Process form file field (input type="file").
	            String fieldName = item.getFieldName();
	            String fileName = FilenameUtils.getName(item.getName());
	            InputStream fileContent = item.getInputStream();
	            if (fieldName.equals("goodsImage")) {
	            	goods.setGoodsImage(fileContent);
	            	goods.setGoodsImageName(fileName);
	            	String filePath = "C:\\Eclipse_File\\Web_File\\JavaEE_Session4_Homework\\WebContent\\DrinksImage\\"+fileName;
		        	FileOutputStream fos = new FileOutputStream(filePath);
		        	byte[] buffer = new byte[1024];
		        	int length;
		        	while ((length = fileContent.read(buffer)) > 0) {
		        	    fos.write(buffer, 0, length);
		        	}
		        	fos.close();
	            }
	        }
	    }

		    
		// 將Struts BackedActionForm 資料複製 Goods
		// 將表單資料轉換儲存資料物件(commons-beanutils-1.8.0.jar)
//		Goods goods =new Goods();
//		BeanUtils.copyProperties(goods,formGood);
		int createGood=backendService.createGoods(goods);
		
		String message = createGood!=0 ? "商品新增上架成功！" : "商品新增上架失敗！";
		System.out.println(message);
		
		ActionForward actionForward=mapping.findForward("goodsCreateView");
		return actionForward;
	}	
	
	
	public ActionForward queryGood(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("queryGoods...");
		Set<Goods> goods=backendService.queryGoods();
		System.out.println("商品總數："+goods.size());
		goods.stream().forEach(g -> System.out.println(g));
		
		ActionForward actionForward=mapping.findForward("queryGoodView");
		return actionForward;
	}
	

	
	public ActionForward VM_Backend_GoodsReplenishment(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("VM_Backend_GoodsReplenishment");
		ActionForward actionForward=mapping.findForward("VM_Backend_GoodsReplenishment");
		return actionForward;
	}

}
