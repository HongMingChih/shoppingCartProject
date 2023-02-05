/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.formbean.FormMember;
import com.training.model.Member;
import com.training.service.FrontendService;

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
public class RegisterAction extends DispatchAction{
	
	private FrontendService frontendService=FrontendService.getInstance();
	
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
		
		String message = acc_verification.getCustomerName()!=null ? "帳戶創建成功！" : "帳戶已存在！";
		System.out.println(message);
		ActionForward actionForward=null;
		if ( acc_verification.getCustomerName()!=null ) {
			actionForward=mapping.findForward("registerSuccess");
		}else {
			actionForward=mapping.findForward("fail");
		}
//		ActionForward actionForward=mapping.findForward("registerSuccess");
		return actionForward;
	}
	
}
