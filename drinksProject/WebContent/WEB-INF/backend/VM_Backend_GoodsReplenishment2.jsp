<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url value="/" var="WEB_PATH"/>
<c:url value="/js" var="JS_PATH"/>
<!DOCTYPE html>
<html>
<head>
<script src="${JS_PATH}/jquery-1.11.1.min.js"></script>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#goodsID").bind("change",function(){
			
			var goodsID = $("#goodsID option:selected").val();
			
			var goodParam={id : goodsID};
			
			if(goodsID != ""){
				$.ajax({
				  url: '${WEB_PATH}BankAction.do?action=getModifyGoods', // 指定要進行呼叫的位址
				  type: "GET", // 請求方式 POST/GET
				  data: goodParam, // 傳送至 Server的請求資料(物件型式則為 Key/Value pairs)
				  dataType : 'JSON', // Server回傳的資料類型
				  success: function(goodsInfo) { // 請求成功時執行函式
				  	$("#goodsPrice").val(goodsInfo.name);
				  	$("#goodsQuantity").val(goodsInfo.pwd);
				  	$("#goodsStatus").val(goodsInfo.balanceText);
				  },
				  error: function(error) { // 請求發生錯誤時執行函式
				  	alert("Ajax Error!");
				  }
				});
			}else{
			  	$("#goodsPrice").val('');
			  	$("#goodsQuantity").val('');
			  	$("#goodsStatus").val('');
			}
		});
	});
	</script>
</head>
<body>

	<%@include file="LinkHeader.jsp" %>
	<br/><br/><HR>		
	<h2>商品維護作業</h2><br />
	<div style="margin-left:25px;">
	<p style="color:blue;">${sessionScope.modifyMsg}</p>
	<% session.removeAttribute("modifyMsg"); %>
	<form name="updateGoodsForm" action="BackendAction.do" method="post">
		<input type="hidden" name="action" value="updateGoods" />
		<p>
			身份證字號：
			 <select size="1" id="goodsID" name="id">
			 	<option value="">----- 請選擇 -----</option>
			 	<c:forEach items="${goods}" var="goods">
					<option <c:if test="${goods.goodsName eq modifyGoods.goodsName}">selected</c:if> 
						value="${goods.goodsID}">
						${goods.goodsName}
					</option>
				</c:forEach>
			</select>
		</p>		
		<p>
			更改價格：
			<input type="number" id="goodsPrice" name="goodsPrice" size="5" value="${modifyGoods.goodsPrice}" min="0" max="1000">
		</p>
		<p>
			補貨數量：
			<input type="number" id="goodsQuantity" name="goodsQuantity" size="5" value="0" min="0" max="1000">
			<c:if test="${modifyGoods.goodsQuantity!=null}">
				<span class="blue-text" style="color: blue;">庫存數量： ${modifyGoods.goodsQuantity}</span>
			</c:if>
		
		
		</p>
		<p>
			商品狀態：
			<select name="status" id="goodsStatus">
				<option value="${modifyGoods.status}">----- 請選擇 -----</option>
				<option value="1">上架</option>
				<option value="0">下架</option>
			</select>
		
<%-- 			<c:choose> --%>
		
<%-- 				<c:when test="${modifyGoods.status == 1}"> --%>
<!-- 					<span class="blue-text" style="color: blue;">當前狀態：上架</span> -->
<%-- 				</c:when> --%>
<%-- 				<c:when test="${modifyGoods.status == 0}"> --%>
<!-- 					<span style="color: red;">當前狀態：下架</span> -->
<%-- 				</c:when> --%>
<%-- 			</c:choose> --%>
		</p>
		<p>
			<input type="submit" value="修改">
		</p>
	</form>
	</div>
</body>
</html>