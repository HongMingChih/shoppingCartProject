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
<%-- <script src="${JS_PATH}/jquery-1.4.4.js"></script> --%>
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
				  url: '${WEB_PATH}BackendAction.do?action=getModifyGood', // 指定要進行呼叫的位址
				  type: "GET", // 請求方式 POST/GET
				  data: goodParam, // 傳送至 Server的請求資料(物件型式則為 Key/Value pairs)
				  dataType : 'JSON', // Server回傳的資料類型
				  success: function(goodsInfo) { // 請求成功時執行函式
				  	$("#goodsPrice").val(goodsInfo.goodsPrice);
				  	$("#goodsQuantity").html("商品庫存： <span style='color:blue;'>"+goodsInfo.goodsQuantity+" 件 </span>");
				  	$("#goodsStatus").val(goodsInfo.status);
				  	$("#goodsName").val(goodsInfo.goodsName);
				  	$("#goodsId").html("商品編號： <span style='color:blue;'>"+goodsInfo.goodsID+" 號 </span>")
				  	$("#currentState").remove();
				  	$("#modifyMsg").html("<br>");
				  },
				  error: function(error) { // 請求發生錯誤時執行函式
				  	alert("Ajax Error!");
				  }
				});
			}else{
			  	$("#goodsPrice").val('');
			  	$("#goodsQuantity").empty('');
			  	$("#goodsId").text('商品編號：');
			  	$("#goodsStatus").val('');
			  	$("#goodsReplenishment").val('');
			  	$("#goodsName").val('');
			  	$("#modifyMsg").html("<br>");
				$("#currentState").remove();
			}
		});
	});
	$(document).ready(function(){
	$("#updateSubmit").bind("click",function(){			
		// 也可一次送出From表單所有資料
		var fromData = $('#updateGoodsForm').serialize();
// 		alert(fromData);
		$.ajax({
			  url: '/JavaEE_Session4_Homework/BackendAction.do?action=updateGoods', // 指定要進行呼叫的位址
			  type: "GET", // 請求方式 POST/GET
			  data: fromData, // 傳送至 Server的請求資料(物件型式則為 Key/Value pairs)
			  dataType : 'json', // Server回傳的資料類型
			  async : false, // 是否同部請求
			  cache : false, // 從瀏覽器中抓 cache
			  success: function(goodsInfo) { // 請求成功時執行函式
				  $("#goodsPrice").val(goodsInfo.goodsList.goodsPrice);
				  	$("#goodsQuantity").html("商品庫存： <span style='color:blue;'>"+goodsInfo.goodsList.goodsQuantity+" 件 </span>");
				  	$("#goodsReplenishment").val('');
				  	$("#goodsStatus").val(goodsInfo.goodsList.status);
				  	$("#currentState").remove();
				  	$("#modifyMsg").html(goodsInfo.queryMessage);
			  	// JSON.stringify：JSON ->　String
			  	$('#goodsText').val(JSON.stringify(goodsInfo));
			  }, 
			  error: function() { // 請求發生錯誤時執行函式
			    alert("Ajax Error!");
			    $("#modifyMsg").html("<span style='color: red;'>商品維護作業失敗！</span>");
			  }
		});
	});			
	});	

	</script>
</head>
<body>
	<h1>Vending Machine Backend Service</h1><br/>		
	<!-- 上方標頭 -->
	<%@include file="LinkHeader.jsp" %>
	
	<h2>商品維護作業</h2><br />
	<div style="margin-left:25px;">
	<p style="color:blue;" id="modifyMsg"><br></p>
<%-- 	<% session.removeAttribute("modifyMsg"); %> --%>
	<form id="updateGoodsForm" name="updateGoodsForm" action="BackendAction.do" method="post">
		<input type="hidden" name="action" value="updateGoods" />
		
		<p>
			商品名稱：
			 <select size="1" id="goodsID" name="goodsID">
			 	<option value="">----- 請選擇 -----</option>
			 	<c:forEach items="${goods}" var="goods">
					<option <c:if test="${goods.goodsName eq modifyGoods.goodsName}">selected</c:if> 
						value="${goods.goodsID}">
						${goods.goodsName}
					</option>
				</c:forEach>
			</select>
			<input type="hidden" id="goodsName" name="goodsName" value=" ${modifyGoods.goodsName}" />
		</p>
		<p>
			<div id="goodsId" style="display: inline-block;">
					商品編號：
			</div>
				
		</p>
	
	
				
		<p>
			更改價格：
			<input type="number" id="goodsPrice" name="goodsPrice" size="5" value="${modifyGoods.goodsPrice}" min="0" max="1000">
		</p>
		<p>
			補貨數量：
			<input type="number" id="goodsReplenishment" name="goodsQuantity" size="5" value="0" min="0" max="1000">
			<div id="goodsQuantity">
				<c:if test="${modifyGoods.goodsQuantity!=null}">
					<span class="blue-text" style="color: blue;">庫存數量： ${modifyGoods.goodsQuantity} 件</span>
					
				</c:if> 
			
			</div>
		</p>
		
		
		<p>
			商品狀態：
			<select name="status" id="goodsStatus">
<!-- 				<option value="">----- 請選擇 -----</option> -->
				<option value="1">上架</option>
				<option value="0">下架</option>
			</select>
			<div id="currentState">
			<c:choose> 
				<c:when test="${modifyGoods.status == 1}">
					<span class="blue-text" style="color: blue;">當前狀態：上架</span>
				</c:when>
				<c:when test="${modifyGoods.status == 0}">
					<span style="color: red;">當前狀態：下架</span>
				</c:when>
			</c:choose>
			</div>
		</p>
			
		<p>
			<input type="button" id="updateSubmit" value="修改">
		</p>
	</form>
		<br/><br/>
	Goods Response: <br/> 
	<textarea id="goodsText" rows="5" cols="80"></textarea>	
	</div>
<%-- 	<% session.removeAttribute("modifyGoods"); %> --%>
<%-- 	<% session.removeAttribute("modifyGoodsID"); %> --%>
</body>
</html>