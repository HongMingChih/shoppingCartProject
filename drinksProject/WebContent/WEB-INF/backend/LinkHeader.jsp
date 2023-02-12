<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<p style="color:blue;">
	${sessionScope.account.getCustomerName()} 先生/小姐您好!
	<a href="LoginAction.do?action=logout" align="left">(登出)</a>
	</p>
	<br/>
<table border="1" style="border-collapse:collapse;margin-left:25px;">
		<tr>
			<td width="200" height="50" align="center">
				<a href="BackendAction.do?action=queryGood">商品列表</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="BackendAction.do?action=updateGoodView">商品維護作業</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="BackendAction.do?action=goodsCreateView">商品新增上架</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="BackendAction.do?action=salesReportView">銷售報表</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="FrontendAction.do?action=searchGoods">前往販賣機</a>	
			</td>
		</tr>
		
	</table>
	
	<br/><br/><HR>