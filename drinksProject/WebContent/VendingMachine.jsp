<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
		function addCartGoods(goodsID, buyQuantityIdx){
			console.log("goodsID:", goodsID);			
			var buyQuantity = document.getElementsByName("buyQuantity")[buyQuantityIdx].value;
			console.log("buyQuantity:", buyQuantity);
			const formData = new FormData();
			formData.append('action', 'addCartGoods');
			formData.append('goodsID', goodsID);
			formData.append('buyQuantity', buyQuantity);
			// 送出商品加入購物車請求
			const request = new XMLHttpRequest();
			request.open("POST", "MemberAction.do?action=addCartGoods");			
			request.send(formData);
			//送出請求後跳轉當前頁面
			request.onload = function() {
				if (request.status === 200){
					location.reload();
				}
			}
		
			// 接回XMLHttpRequest非同步請求回傳
			request.onreadystatechange = function() {
		        if (this.readyState == 4 && this.status == 200) {
		            const response = request.responseText;		            
		            const responseJson = JSON.parse(response);		            
		            alert(JSON.stringify(responseJson, null, 3));
		      };
		   }
		}
		
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
					location.reload();
				}
			}
			// 接回XMLHttpRequest非同步請求回傳
			request.onreadystatechange = function() {
		        if (this.readyState == 4 && this.status == 200) {
		            const response = request.responseText;		            
		            const responseJson = JSON.parse(response);		            
		            alert(JSON.stringify(responseJson, null, 3));
		      };
		   }
		}
		function clearCartGoods(){
			const formData = new FormData();
			formData.append('action', 'clearCartGoods');
			// 送出清空購物車商品請求
			const request = new XMLHttpRequest();
			request.open("POST", "MemberAction.do?action=clearCartGoods");			
			request.send(formData);	
			
			request.onload = function() {
				if (request.status === 200){
					location.reload();
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
			<img border="0" src="DrinksImage/coffee.jpg" width="200" height="200" >			
		<form action="FrontendAction.do" method="post">
		 <input type="hidden" name="action" value="buyGoods"/>
		<input type="hidden" name="customerID"  value="${sessionScope.account.getIdentificationNo()}"/>
			<h1>歡迎光臨，${sessionScope.account.getCustomerName()}！</h1>	
			<a href="BackendAction.do?action=queryGood" align="left" >後臺頁面</a>&nbsp; &nbsp;
			<a href="LoginAction.do?action=logout" align="left">登出</a>
			<a href="MemberAction.do?action=goCart" align="left" >前往購物車</a>
			<br/><br/>
			
			<font face="微軟正黑體" size="4" >
				<b>投入:</b>
				<input type="number" name="inputMoney" max="100000" min="0"  size="5" value="0">
				<b>元</b>		
				<b><input type="submit" value="送出">					
				<br/><br/>
			</font>
		</form>
			<div style="border-width:3px;border-style:dashed;border-color:#FFAC55;
				padding:5px;width:300px;">
				<p style="color: blue;">~~~~ 消費明細 ~~~~</p>

				<p style="margin: 10px;">
<!-- 					投入金額：150 -->
					<c:if test="${not empty sessionScope.message}">
					    <span style="color:red;">${sessionScope.message}</span>
					<br>
					</c:if>
					
					<c:if test="${not empty sessionScope.message}">
					 投入金額： ${sessionScope.buyGoodsRtn.getInputMoney()} 元
					</c:if>
					
<%-- 					投入金額：<%= Rtn.getInputMoney() %> --%>
				</p>
				<p style="margin: 10px;">
<!-- 					購買金額：111 -->
<%-- 					購買金額：<%= Rtn.getAllMoney() %> --%>
					<c:if test="${not empty sessionScope.message}">
					 購買金額： ${sessionScope.buyGoodsRtn.getAllMoney()} 元
					</c:if>
				</p>
				<p style="margin: 10px;">
<!-- 					找零金額：39 -->
<%-- 					找零金額：<%= Rtn.getChange() %> --%>
					<c:if test="${not empty sessionScope.message}">
					 找零金額： ${sessionScope.buyGoodsRtn.getChange()} 元
					</c:if>
				</p>
					<c:if test="${not empty sessionScope.buyGoodsList}">
					<p style="color: blue;">~~~~ 購買商品 ~~~~</p>
						<c:forEach items="${sessionScope.buyGoodsList}" var="entry">
						
						   商品名稱: <c:out value="${entry.key.getGoodsName()}" />
					 	   商品金額: <c:out value="${entry.key.getGoodsPrice()}" />
						   購買數量: <c:out value="${entry.value}" />
					 	</c:forEach>
					</c:if>

<!-- 				<p style="margin: 10px;"> -->
<!-- 					fanta_grape 7 * 3 -->
<!-- 				</p> -->
<!-- 				<p style="margin: 10px;"> -->
<!-- 					spring_original 20 * 2 -->
<!-- 				</p> -->
<!-- 				<p style="margin: 10px;"> -->
<!-- 					pepsi_original 25 * 2 -->
<!-- 				</p>				 -->
			</div>	
		</td>
		
		
		
		<td width="600" height="400">
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
		          購買<input type="number" name="buyQuantity" min="0" max="${goods.goodsQuantity}" size="5" value="0">罐
		          <br><br><button onclick="addCartGoods(${goods.goodsID},${status.index})">加入購物車</button>
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
		
	</tr>
	
	<tr>
	<td colspan="2" align="right">
	 	 <c:if test="${totalPages > 1}">
	   		 <h3 class="page">
	     <c:if test="${pageNo-1 > 0}">
	      <a href="FrontendAction.do?action=searchGoods&pageNo=${pageNo - 1}&searchKeyword=${searchKeyword}">上一頁</a>
		    </c:if>
			    </h3>
				    <c:forEach var="i" begin="1" end="${totalPages}">
				      <h3 class="page">
				        <a href="FrontendAction.do?action=searchGoods&pageNo=${i}&searchKeyword=${searchKeyword}">${i}</a>
				      </h3>
				    </c:forEach>
			    <h3 class="page">
		     <c:if test="${pageNo+1 <= totalPages}">
	      <a href="FrontendAction.do?action=searchGoods&pageNo=${pageNo + 1}&searchKeyword=${searchKeyword}">下一頁</a>
	   </c:if>
    </h3>
  </c:if>
</td>
</tr>

</table>


</body>

</html>