<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.training.model.Goods"%>
<%@page import="com.training.model.Member"%>
<%@page import="com.training.vo.BuyGoodsRtn"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機</title>
	<style type="text/css">
		.page {
			display:inline-block;
			padding-left: 10px;
		}
	</style>
	<script type="text/javascript">

	</script>
</head>

<body align="center">
<table width="1000" height="400" align="center">
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
		<form action="FrontendAction.do" method="post">
			<%
			Object account = session.getAttribute("account");
			if (account != null) {
			Member member = (Member)account;
			%>
		<input type="hidden" name="customerID"  value="<%= member.getIdentificationNo() %>"/>
		<td width="400" height="200" align="center">		
			<img border="0" src="DrinksImage/coffee.jpg" width="200" height="200" >			
			<h1>歡迎光臨，<%= member.getCustomerName() %>！</h1>	
			
			<%
			}
			%>	
			<a href="BackendAction.do?action=queryGood" align="left" >後臺頁面</a>&nbsp; &nbsp;
			<a href="LoginAction.do?action=logout" align="left">登出</a>
			<br/><br/>
			<font face="微軟正黑體" size="4" >
				<b>投入:</b>
				<input type="number" name="inputMoney" max="100000" min="0"  size="5" value="0">
				<b>元</b>		
				<b><input type="submit" value="送出">					
				<br/><br/>
			</font>
			
			<div style="border-width:3px;border-style:dashed;border-color:#FFAC55;
				padding:5px;width:300px;">
				<p style="color: blue;">~~~~~~~ 消費明細 ~~~~~~~</p>

			
				<p style="margin: 10px;">
					投入金額：150
<%-- 					投入金額：<%= Rtn.getInputMoney() %> --%>
				</p>
				<p style="margin: 10px;">
					購買金額：111
<%-- 					購買金額：<%= Rtn.getAllMoney() %> --%>
				</p>
				<p style="margin: 10px;">
					找零金額：39
<%-- 					找零金額：<%= Rtn.getChange() %> --%>
				</p>
				

				<p style="margin: 10px;">
					fanta_grape 7 * 3
				</p>
				<p style="margin: 10px;">
					spring_original 20 * 2
				</p>
				<p style="margin: 10px;">
					pepsi_original 25 * 2
				</p>				
			</div>	
		</td>
		
		
		
		<td width="600" height="400">
  <input type="hidden" name="action" value="buyGoods"/>
  <table border="1" style="border-collapse: collapse">
    <c:forEach var="goods" items="${searchGoods}" varStatus="status">
      <td width="300">
        <font face="微軟正黑體" size="5" >
          ${goods.goodsName}
        </font>
        <br/>
        <font face="微軟正黑體" size="4" style="color: gray;" >
          ${goods.goodsPrice} 元/件 
        </font>
        <br/>
        <img border="0" src="DrinksImage/${goods.goodsImageName}" width="150" height="150" >
        <br/>
        <font face="微軟正黑體" size="3">
          <input type="hidden" name="goodsIDs" value="${goods.goodsID}">
          購買<input type="number" name="buyQuantitys" min="0" max="${goods.goodsQuantity}" size="5" value="0">罐
          <br><p style="color: red;">(庫存 ${goods.goodsQuantity} 罐)</p>
        </font>
      </td>
      <%-- 每3件商品換行 --%>
      <c:if test="${status.index % 3 == 2}">
        </tr><tr>
      </c:if>
    </c:forEach>
  </table>
</td>
		</form>
	</tr>
	<tr>
		<td colspan="2" align="right">				
			<h3 class="page"> <a href="FrontendAction.do?action=searchGoods&pageNo=1"> 上一頁 </a> </h3>
			<h3 class="page"> <a href="FrontendAction.do?action=searchGoods&pageNo=1"> 1 </a> </h3>
			<h3 class="page"> <a href="FrontendAction.do?action=searchGoods&pageNo=2"> 2 </a> </h3>
			<h3 class="page"> <a href="FrontendAction.do?action=searchGoods&pageNo=3"> 3 </a> </h3>  
			<h3 class="page"> <a href="FrontendAction.do?action=searchGoods&pageNo=4"> 下一頁 </a> </h3>
		</td>
	</tr>
</table>


</body>

</html>