<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css">
<title>販賣機-後臺</title>
	<script type="text/javascript">

	</script>
</head>
<body>
	<h1>Vending Machine Backend Service</h1><br/>	
	<!-- 上方標頭 -->
	<%@include file="LinkHeader.jsp" %>
		
	<h2>商品列表</h2><br/>

	<form action="Cond.do?action=conditionalGoods" method="POST" accept-charset="UTF-8"
		name="formCond">
		<div class="container-fluid">
			<!-- 		<input type="hidden" name="action" value="conditionalGoods" /> -->
			<input type="hidden" name="pageNo" value="1" /> 
			商品編號: <input type="number" name="goodsID" value="<c:if test="${formCond.goodsID != 0}">${formCond.goodsID}</c:if>"/>
			商品名稱(不區分大小寫): <input type="text" name="goodsName" value="${formCond.goodsName}"/> <br><br> 
			商品價格最低價: <input type="number" name="minPrice" value="<c:if test="${formCond.minPrice != 0}">${formCond.minPrice}</c:if>" /> 商品價格最高價:
		
			<input type="number" name="maxPrice" value="<c:if test="${formCond.maxPrice != 0}">${formCond.maxPrice}</c:if>"/> 
			價格排序: <select name="sortPrice">
				<option value="asc">無</option>
				<option value="asc">升冪</option>
				<option value="desc">降冪</option>
			</select> <br><br> 
			商品低於庫存量: <input type="number" name="goodsQuantity" value="<c:if test="${formCond.goodsQuantity != 0}">${formCond.goodsQuantity}</c:if>"/>
			商品狀態: <select name="status">
				<option value="">ALL</option>
				<option value="1">上架</option>
				<option value="0">下架</option>
			</select> 
			<input class="btn btn-danger" type="submit" value="查詢">
		</div>
	</form>
	<br><br>
	<div style="margin-left:25px;">
	<table border="1">
		<tbody>
			<tr height="50">
				<td width="150"><b>商品名稱</b></td> 
				<td width="100"><b>商品價格</b></td>
				<td width="100"><b>現有庫存</b></td>
				<td width="100"><b>商品狀態</b></td>
			</tr>
			
			<c:forEach var="goods" items="${searchAllGoods}" varStatus="status">
			<tr height="30">
				   <td><c:out value="${goods.goodsName}" /></td>
				    <td><c:out value="${goods.goodsPrice}" /></td>
				    <td><c:out value="${goods.goodsQuantity}" /></td>
				     <td>
					  <c:choose>
					    <c:when test="${goods.status == 1}">
					      <span class="blue-text" style="color: blue;">上架</span>
					    </c:when>
					    <c:otherwise>
					      <span style="color: red;">下架</span>
					    </c:otherwise>
					  </c:choose>
					</td>
				 </tr>
				</c:forEach>		
		</tbody>
	</table>
		<tr>
	<td colspan="2" align="right">
	 	 <c:if test="${totalPages > 1}">
	     <c:if test="${pageNo-1 > 0}">
	      <a href="BackendAction.do?action=queryGood&pageNo=${pageNo - 1}">上一頁</a>
		    </c:if>
				    <c:forEach var="i" begin="1" end="${totalPages}">
				        <a href="BackendAction.do?action=queryGood&pageNo=${i}">${i}</a>
				    </c:forEach>
		     <c:if test="${pageNo+1 <= totalPages}">
	      <a href="BackendAction.do?action=queryGood&pageNo=${pageNo + 1}">下一頁</a>
	   </c:if>
  </c:if>
</td>
</tr>
	</div>

	
</body>

</html>