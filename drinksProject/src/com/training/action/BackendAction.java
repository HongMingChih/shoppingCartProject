/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.action;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.training.formbean.FormGoods;
import com.training.model.Cond;
import com.training.model.Goods;
import com.training.service.BackendService;
import com.training.vo.GoodsResult;
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
@MultipartConfig
public class BackendAction extends DispatchAction{
	
	private BackendService backendService=BackendService.getInstance();
	//報表跳轉
	public ActionForward salesReportView(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		//報表資料
		Set<SalesReport> salesReport=backendService.queryAllOrder();
		request.setAttribute("queryOrderBetweenDate", salesReport);
		
		
		
		return mapping.findForward("salesReportView");
	}
	//更新跳轉
	public ActionForward updateGoodView(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		//商品選單資料
		Set<Goods> goods=backendService.queryGoods();
		request.setAttribute("goods", goods);
		// 被選擇要修改的商品資料
		String goodsID = request.getParameter("goodsID");
		goodsID = (goodsID != null) ? goodsID : (String)request.getSession().getAttribute("modifyGoodsID");
		if(goodsID != null){
			Goods good = backendService.queryGoodsById(new BigDecimal(goodsID));
			request.setAttribute("modifyGoods", good);
		}
		
		return mapping.findForward("updateGoodView");
	}
	/**
	 * 商品更新
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("updateGoods...");
		HttpSession session = request.getSession();
		// 將表單資料使用 struts ActionForm 方式自動綁定，省去多次由 request getParameter 取表單資料的工作
		FormGoods formGoods = (FormGoods) form;
		if (formGoods.getGoodsID()==0) {
			session.setAttribute("modifyMsg", "請選擇需要維護的商品!!");
			return mapping.findForward("updateGood");
		}
		
		// 將Struts BackedActionForm 資料複製 Goods
		// 將表單資料轉換儲存資料物件(commons-beanutils-1.8.0.jar)
		Goods goods = new Goods();
		BeanUtils.copyProperties(goods, formGoods);
		
		
		boolean updateGood=backendService.updateGoods(goods);
		
		String message = updateGood ? "商品維護作業成功！" : "商品維護作業失敗！";
		
		session.setAttribute("modifyMsg", message);
		session.setAttribute("modifyGoodsID", goods.getGoodsID().toString());
		
		return mapping.findForward("updateGood");
	}
	
	//新增跳轉
	public ActionForward goodsCreateView(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		ActionForward actionForward=mapping.findForward("goodsCreateView");
		return actionForward;
	}
	
	/**
	 * 條件查詢
	 */
	public ActionForward conditionalGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("conditionalGoods...");
		HttpSession session=request.getSession();
		int pageNo=request.getParameter("pageNo")==null?1:Integer.parseInt(request.getParameter("pageNo"));
		// 將表單資料使用 struts ActionForm 方式自動綁定，省去多次由 request getParameter 取表單資料的工作
		FormCond formCond=(FormCond) form!=null?(FormCond) form: (FormCond) session.getAttribute("formCond");
		Cond cond=new Cond();
		BeanUtils.copyProperties(cond, formCond);
		
//		cond.setGoodsName(new String(request.getParameter("goodsName").getBytes("ISO-8859-1"), "UTF-8"));
		 int endRowNo=(cond.getPageNo()*6)+1;
		 int startRowNo=endRowNo-7;
		 Set<Goods> queryCond=backendService.queryCond(cond, startRowNo, endRowNo);
		 Set<Goods> queryAllCond=backendService.queryCond(cond, 0,0);
			request.setAttribute("searchKeyword", cond.getGoodsName());
			request.setAttribute("Cond", cond);
		System.out.println("共 "+queryAllCond.size()+" 件商品");
		 int totalPages = queryAllCond.size()!=0?(queryAllCond.size()+5)/6:0;
//		 for (Goods goods : queryCond) {
//				System.out.println(goods.toString());
//			}
		 
		 request.setAttribute("searchAllGoods", queryCond);
		    request.setAttribute("totalPages", totalPages);
		    request.setAttribute("pageNo", pageNo);
		    session.setAttribute("formCond", formCond);
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
//		System.out.println("查詢訂單總數："+queryOrderBetweenDate.size());
//		queryOrderBetweenDate.stream().forEach(g -> System.out.println(g));
		
		
		 request.setAttribute("queryOrderBetweenDate", queryOrderBetweenDate);
//		    request.setAttribute("totalPages", totalPages);
//		    request.setAttribute("searchKeyword", searchKeyword);
//		    request.setAttribute("pageNo", pageNo);
		
		ActionForward actionForward=mapping.findForward("salesReportView");
		return actionForward;
	}
	
	
	//新增商品 圖片
	public ActionForward addGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("addGoods...");
		Goods goods = new Goods();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items = upload.parseRequest(request);
		HttpSession session=request.getSession();
		String goodsName="";
		for (FileItem item : items) {
		    if (item.isFormField()) {
		        // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
		        String fieldName = item.getFieldName();
		        String fieldValue = item.getString().trim();
		        switch (fieldName) {
		            case "goodsName":
		            	if (fieldValue.equals("")) {
		            		session.setAttribute("modifyMsg", "請輸入商品名稱!!");
		            		return mapping.findForward("goodsCreate");
		            	}
		            	goodsName = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
		                goods.setGoodsName(goodsName);
		                break;
		            case "goodsPrice":
		            	if (fieldValue.equals("")||Integer.parseInt(fieldValue)==0) {
		            		session.setAttribute("modifyMsg", "請輸入商品價格!!");
		            		return mapping.findForward("goodsCreate");
		            	}
		                goods.setGoodsPrice(Integer.parseInt(fieldValue));
		                break;
		            case "goodsQuantity":
		            	if (fieldValue.equals("")||Integer.parseInt(fieldValue)==0) {
		            		session.setAttribute("modifyMsg", "請輸入商品數量!!");
		            		return mapping.findForward("goodsCreate");
		            	}
		                goods.setGoodsQuantity(Integer.parseInt(fieldValue));
		                break;
		            case "status":
		                goods.setStatus(fieldValue);
		                break;
		        }
		    } else {
		       //上傳圖片
		        String fieldName = item.getFieldName();
		        String fileName = FilenameUtils.getName(item.getName()).replaceAll("\\s+", "");
		        InputStream fileContent = item.getInputStream();
		        if (fileName.equals("")) {
            		session.setAttribute("modifyMsg", "請上傳商品圖片!!");
            		return mapping.findForward("goodsCreate");
            	}
		        if (fieldName.equals("goodsImage")) {
		            goods.setGoodsImage(fileContent);
		            goods.setGoodsImageName(fileName);
		            String filePath = "C:\\Eclipse_File\\Web_File\\JavaEE_Session4_Homework\\WebContent\\DrinksImage\\" + fileName;
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
//		System.out.println(message);
		message+="商品 "+goodsName+" 編號: "+createGood;
		session.setAttribute("modifyMsg", message);
		
		ActionForward actionForward=mapping.findForward("goodsCreate");
		return actionForward;
	}	
	
	
	public ActionForward queryGood(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		System.out.println("queryGoods...");
//		Set<Goods> goods=backendService.queryGoods();
//		System.out.println("商品總數："+goods.size());
//		goods.stream().forEach(g -> System.out.println(g));
		HttpSession session=request.getSession();
		 String searchKeyword="";
		 int pageNo=request.getParameter("pageNo")==null?1:Integer.parseInt(request.getParameter("pageNo"));
		 //判斷是否存在分頁資訊，如果存在轉至分頁查詢處理
		if (session.getAttribute("formCond")!=null) {
			request.setAttribute("pageNo", pageNo);
			return mapping.findForward("conditionalGoods");
		}
		
//		0-7  6-13  12-19 18-25
//		1     2      3     4
//	    7
	    int	endRowNo=(pageNo*6)+1;
	    int	startRowNo=endRowNo-7;
	   
	    GoodsResult goodsResult=backendService.searchAllGoods(searchKeyword,startRowNo,endRowNo);
	    Set<Goods> searchAllGoods=goodsResult.getGoods();
//	    for (Goods goods : searchAllGoods) {
//			System.out.println(goods.toString());
//		}
	    int totalPages = goodsResult.getTotalRecords()!=0?(goodsResult.getTotalRecords() + 5) / 6:0;
	    request.setAttribute("searchAllGoods", searchAllGoods);
	    request.setAttribute("totalPages", totalPages);
	    request.setAttribute("searchKeyword", searchKeyword);
	    request.setAttribute("pageNo", pageNo);
		
		
		
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
