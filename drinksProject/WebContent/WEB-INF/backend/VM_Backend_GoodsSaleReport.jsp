<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>
	<script type="text/javascript">

	</script>
</head>
<body>
	<h1>Vending Machine Backend Service</h1><br/>		
	<!-- 上方標頭 -->
	<%@include file="LinkHeader.jsp" %>
		
	<h2>銷售報表</h2><br/>
	<div style="margin-left:25px;">
	<form action="BackendAction.do" method="get">
		<input type="hidden" name="action" value="querySalesReport"/>
		起 &nbsp; <input type="date" name="queryStartDate" style="height:25px;width:180px;font-size:16px;text-align:center;"/>
		&nbsp;
		迄 &nbsp; <input type="date" name="queryEndDate" style="height:25px;width:180px;font-size:16px;text-align:center;"/>	
		<input type="submit" value="查詢" style="margin-left:25px; width:50px;height:32px"/>
	</form>
	<br/>
	<table border="1">
		<tbody>
			<tr height="50">
				<td width="100"><b>訂單編號</b></td>
				<td width="100"><b>顧客姓名</b></td>
				<td width="100"><b>購買日期</b></td>
				<td width="125"><b>飲料名稱</b></td> 
				<td width="100"><b>購買單價</b></td>
				<td width="100"><b>購買數量</b></td>
				<td width="100"><b>購買金額</b></td>
			</tr>
			<c:forEach var="salesReport" items="${queryOrderBetweenDate}" varStatus="status">
			<tr height="30">
				<td><c:out value="${salesReport.orderID}" /></td>
				<td><c:out value="${salesReport.customerName}" /></td>
				<td><c:out value="${salesReport.orderDate}" /></td>
				<td><c:out value="${salesReport.goodsName}" /></td>
				<td><c:out value="${salesReport.goodsBuyPrice}" /></td> 
				<td><c:out value="${salesReport.buyQuantity}" /></td>
				<td><c:out value="${salesReport.buyAmount}" /></td>	
			</tr>
			</c:forEach>
			
		</tbody>
	</table>
	</div>
</body>
</html>