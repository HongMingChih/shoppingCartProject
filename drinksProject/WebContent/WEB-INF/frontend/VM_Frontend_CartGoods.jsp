<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.training.model.Goods"%>
<%@page import="com.training.model.Member"%>
<%@page import="com.training.vo.BuyGoodsRtn"%>
<%@page import="com.training.vo.BuyGoodsRtn"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
<title>購物車</title>
	<style type="text/css">
		.page {
			display:inline-block;
			padding-left: 10px;
		}
	</style>
	<script type="text/javascript">
		
	function queryCartGoods(){
		const formData = new FormData();
		formData.append('action', 'queryCartGoods');
		// 送出查詢購物車商品請求
		const request = new XMLHttpRequest();
		request.open("POST", "MemberAction.do?action=queryCartGoods");			
		request.send(formData);
		//送出請求後跳轉頁面
		request.onload = function() {
			if (request.status === 200){
				window.location.href = "../JavaEE_Session4_Homework/MemberAction.do?action=queryCartGoods";
			}
		}
	}
	function clearCartGoods(){
		const formData = new FormData();
		formData.append('action', 'clearCartGoods');
		// 送出清空購物車商品請求
		const request = new XMLHttpRequest();
		request.open("POST", "MemberAction.do?action=clearCartGoods");			
		request.send(formData);		
		//送出請求後跳轉頁面
		request.onload = function() {
			if (request.status === 200){
				window.location.href = "../JavaEE_Session4_Homework/MemberAction.do?action=queryCartGoods";
			}
			
		}
	}
	</script>
</head>

<body align="center">
<table width="1000" height="400" align="center">
	<tr>
		<td colspan="2" align="right">
			<button onclick="queryCartGoods()">購物車商品列表</button>
			<button onclick="clearCartGoods()">清空購物車</button>			
		</td>
	</tr>
	<tr>
		<td colspan="2" align="right">
			<form action="FrontendAction.do" method="get">
				<input type="hidden" name="action" value="searchGoods"/>
				<input type="hidden" name="pageNo" value="1"/>
				<input type="text" name="searchKeyword"/>
				<input type="submit" value="商品查詢"/>
			</form>
		</td>
	</tr>
	<tr>
			
		<td width="400" height="200" align="center">		
			<img border="0" src="DrinksImage/carGoods.jpg" width="200" height="200" >	
			 <h1>購物車</h1>	
			  <a href="FrontendAction.do?action=searchGoods" align="left" >返回販賣機</a>	
		<h3>總需消費金額: <%=request.getAttribute("allPrice")%> 元</h1> 
		<form action="FrontendAction.do" method="post">
		<input type="hidden" name="customerID"  value="<c:out value="${sessionScope.account.getIdentificationNo()}" />"/>
		 <input type="hidden" name="action" value="buyGoods"/>
		
			<font face="微軟正黑體" size="4" >
				<b>投入:</b>
				<input type="number" name="inputMoney" max="100000" min="0"  size="5" value="0">
				<b>元</b>		
				<b><input type="submit" value="確認購買">					
				<br/><br/>
			</font>
		</form>
		</td>
		
		
		
		<td width="600" height="400">
		
				<table class="table">
					<thead>
						<tr>
							<th scope="col">購買商品</th>
							<th scope="col">商品價格</th>
							<th scope="col">購買數量</th>
							<th scope="col">商品圖片</th>
							<th scope="col">更新</th>
							<th scope="col">刪除</th>

						</tr>
					</thead>

					<tbody>
						
						<%
						Map<Goods, Integer> carGoods  = (LinkedHashMap) request.getAttribute("carGoods");
						int allPrice=0;
						for (Map.Entry<Goods, Integer> entry : carGoods.entrySet()) {
							Goods goods=entry.getKey();
							allPrice+=goods.getGoodsPrice()*entry.getValue();
						%>
						<tr>
							<th scope="row"><%=goods.getGoodsName()%></th>
							<td><%=goods.getGoodsPrice()%></td>
							<td><%=entry.getValue()%></td>
							<td><img src="DrinksImage/<%=goods.getGoodsImageName()%>" width="100" height="100"></td>
							
<!-- 							<td><a -->
<%-- 								href="<%=request.getContextPath()%>/admin/storeupdate.jsp?rId= --%>
<%-- 					<%=restaurant.get(i).getId()%>&rName=<%=restaurant.get(i).getName()%>&rDescription= --%>
<%-- 					<%=restaurant.get(i).getDescription()%>&rPicture=<%=restaurant.get(i).getPicture()%>&rCategory= --%>
<%-- 					<%=restaurant.get(i).getCategory()%>&rAddress=<%=restaurant.get(i).getAddress()%>&rPhone= --%>
<%-- 					<%=restaurant.get(i).getPhone()%>">更新 --%>
<!-- 							</a></td> -->
<!-- 							<td><a -->
<%-- 								href="<%=request.getContextPath()%>/restaurantServlet?rId= --%>
<%-- 					<%=restaurant.get(i).getId()%>&status=delete">刪除</a></td> --%>
<!-- 						</tr> -->
						<%
						}
						%>
					</tbody>

				</table>
		</td>
		
	</tr>
	
	

</table>


</body>

</html>