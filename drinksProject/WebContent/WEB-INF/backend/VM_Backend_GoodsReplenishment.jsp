<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>
	<script type="text/javascript">
		function goodsSelected(){
			if(document.updateGoodsForm.goodsID.value === ''){
				document.updateGoodsForm.reset();
				return;
			}
	        document.updateGoodsForm.action.value = "updateGoodView";
	        document.updateGoodsForm.submit();
		}
	</script>
</head>
<body>
	<h1>Vending Machine Backend Service</h1><br/>		
	<!-- 上方標頭 -->
	<%@include file="LinkHeader.jsp" %>
		
	<h2>商品維護作業</h2><br/>
	<div style="margin-left:25px;">
	<p style="color:blue;">${sessionScope.modifyMsg}</p>
	<% session.removeAttribute("modifyMsg"); %>
	<form name="updateGoodsForm" action="BackendAction.do" method="post">
	<input type="hidden" name="action" value="updateGoods"/>
		<p>
			飲料名稱：
			 <select size="1" name="goodsID" onchange="goodsSelected();">
			 <option value="">----- 請選擇 -----</option>
			 <c:forEach items="${goods}" var="goods">
					<option 
					<c:if test="${goods.goodsName eq modifyGoods.goodsName}">selected</c:if> 
						value="${goods.goodsID}">
						${goods.goodsName}
					</option>
				</c:forEach>
			</select>
		</p>		
		<p>
			更改價格： 
			<input type="number" name="goodsPrice" size="5" value="${modifyGoods.goodsPrice}" min="0" max="1000">
		</p>
		<p>
			補貨數量：
			<input type="number" name="goodsQuantity" size="5" value="0" min="0" max="1000">
				<c:if test="${modifyGoods.goodsQuantity!=null}">
					<span class="blue-text" style="color: blue;">庫存數量： ${modifyGoods.goodsQuantity}</span>
				</c:if> 
			
			
		</p>
		<p>
			商品狀態：
			<select name="status">
					<option value="${modifyGoods.status}">----- 請選擇 -----</option>
					<option value="1">上架</option>
					<option value="0">下架</option>				
			</select>
				  
				 <c:choose>
				
					    <c:when test="${modifyGoods.status == 1}">
					      <span class="blue-text" style="color: blue;">當前狀態：上架</span>
					    </c:when>
					     <c:when test="${modifyGoods.status == 0}">
					       <span style="color: red;">當前狀態：下架</span>
					    </c:when>
					  </c:choose>
		</p>
		<p>
			<input type="submit" value="送出">
		</p>
	</form>
	</div>
</body>
</html>