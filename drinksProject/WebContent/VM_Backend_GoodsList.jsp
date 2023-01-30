<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	<table border="1" style="border-collapse:collapse;margin-left:25px;">
		<tr>
			<td width="200" height="50" align="center">
				<a href="BackendAction.do?action=queryGood">商品列表</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="VM_Backend_GoodsReplenishment.jsp">商品維護作業</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="VM_Backend_GoodsCreate.jsp">商品新增上架</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="VM_Backend_GoodsSaleReport.jsp">銷售報表</a>
			</td>
		</tr>
	</table>
	<br/><br/><HR>
		
	<h2>商品列表</h2><br/>

	<form action="Cond.do?action=conditionalGoods" method="POST" accept-charset="UTF-8"
		name="formCond">
		<div class="container-fluid">
			<!-- 		<input type="hidden" name="action" value="conditionalGoods" /> -->
			<input type="hidden" name="pageNo" value="1" /> 
			商品編號: <input type="number" name="goodsID" /> 
			商品名稱(不區分大小寫): <input type="text" name="goodsName" /> <br><br> 
			商品價格最低價: <input type="number" name="minPrice" /> 商品價格最高價:
			<input type="number" name="maxPrice" /> 
			價格排序: <select name="sortPrice">
				<option value="asc">無</option>
				<option value="asc">升冪</option>
				<option value="desc">降冪</option>
			</select> <br><br> 
			商品低於庫存量: <input type="number" name="goodsQuantity" />
			商品狀態: <select name="status">
				<option value="">ALL</option>
				<option value="1">上架</option>
				<option value="0">下架</option>
			</select> <input class="btn btn-danger" type="submit" value="查詢">

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
			<tr height="30">
				<td>coke_original</td> 
				<td>100</td>
				<td>11</td>
				<td>上架</td>		
			</tr>		
			<tr height="30">
				<td>fanta_grape</td> 
				<td>7</td>
				<td>5</td>
				<td>上架</td>
			</tr>
			<tr height="30">
				<td>spring_original</td> 
				<td>20</td>
				<td>7</td>
				<td>上架</td>
			</tr>		
			<tr height="30">
				<td>coke_zero</td> 
				<td>10</td>
				<td>1</td>
				<td>上架</td>
			</tr>		
			<tr height="30">
				<td>pepsi_original</td> 
				<td>25</td>
				<td>4</td>
				<td>上架</td>
			</tr>
			<tr height="30">
				<td>fanta_orange</td> 
				<td>25</td>
				<td>0</td>
				<td>上架</td>
			</tr>	
		</tbody>
	</table>
	</div>
</body>

</html>