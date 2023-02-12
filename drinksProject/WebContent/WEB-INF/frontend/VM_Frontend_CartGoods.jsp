<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	
		   function increaseQuantity(goodsID) {
			   const formData = new FormData();
			   formData.append('action', 'increaseQuantity');
			   formData.append('goodsID', goodsID);
				// 送出商品加入購物車請求
				const request = new XMLHttpRequest();
				request.open("POST", "MemberAction.do?action=increaseQuantity");			
				request.send(formData);
				//送出請求後跳轉當前頁面
				request.onload = function() {
						location.reload();
				}
		   }
		  function decreaseQuantity(goodsID) {
		    // 實現減少數量的功能
			  const formData = new FormData();
			   formData.append('action', 'decreaseQuantity');
			   formData.append('goodsID', goodsID);
				// 送出商品加入購物車請求
				const request = new XMLHttpRequest();
				request.open("POST", "MemberAction.do?action=decreaseQuantity");			
				request.send(formData);
				//送出請求後跳轉當前頁面
				request.onload = function() {
						location.reload();
				}
		   }
		  function deleteGoods(goodsID) {
			    // 實現減少數量的功能
				  const formData = new FormData();
				   formData.append('action', 'decreaseQuantity');
				   formData.append('goodsID', goodsID);
					// 送出商品加入購物車請求
					const request = new XMLHttpRequest();
					request.open("POST", "MemberAction.do?action=deleteGoods");			
					request.send(formData);
					//送出請求後跳轉當前頁面
					request.onload = function() {
							location.reload();
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
		<h3>總需消費金額: ${allPriceSession} 元</h1> 
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
							<th scope="col">取消商品</th>

						</tr>
					</thead>

					<tbody>
					  <c:forEach var="goods" items="${carGoods}">
					    <tr>
					      <th scope="row">${goods.key.goodsName}</th>
					      <td>${goods.key.goodsPrice}</td>
					    <td width="100" height="100">
					      <button onclick="decreaseQuantity('${goods.key.goodsID}')">-</button>
							${goods.value}
							<button onclick="increaseQuantity('${goods.key.goodsID}')">+</button>
						</td>
					      <td><img src="DrinksImage/${goods.key.goodsImageName}" width="100" height="100"/></td>
					    <td>
				          <button onclick="deleteGoods('${goods.key.goodsID}')">取消</button>
				        </td>
					    </tr>
					  </c:forEach>
					</tbody>

				</table>
		</td>
		
	</tr>
	
	

</table>


</body>

</html>