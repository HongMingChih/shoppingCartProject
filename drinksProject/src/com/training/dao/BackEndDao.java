/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.training.model.Cond;
import com.training.model.Goods;
import com.training.utils.DBConnectionFactory;
import com.training.vo.GoodsMaxMin;
import com.training.vo.GoodsResult;
import com.training.vo.SalesReport;


/**
*
*
*
*
*<br>author: MingChih Hong
*@since 11.0<br>
*TODO:
*
*/
public class BackEndDao {
private static BackEndDao backendDao = new BackEndDao();
	
	public final static SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private BackEndDao(){ }

	public static BackEndDao getInstance(){
		return backendDao;
	}
	
	/**
	 * 後臺分頁
	 */
	public GoodsResult searchAllGoods(String searchKeyword, int startRowNo, int endRowNo) {
		Set<Goods> goods = new LinkedHashSet<>();
		GoodsResult goodsResult=new GoodsResult();
		 int totalRecords = 0;
			// 分頁查詢
			StringBuffer querySQL = new StringBuffer();
			querySQL.append("SELECT * FROM ( ");
			querySQL.append("  SELECT ROWNUM ROW_NUM, G.* FROM BEVERAGE_GOODS G ");
			querySQL.append("  WHERE UPPER(G.GOODS_NAME) LIKE ? AND G.STATUS = '1' ");
			querySQL.append("  ORDER BY G.GOODS_ID ");
			querySQL.append(") WHERE ROW_NUM > ? AND ROW_NUM < ?");
		    // 獲取所有符合條件的記錄數
		    StringBuffer countSQL = new StringBuffer();
		    countSQL.append("SELECT COUNT(*) FROM BEVERAGE_GOODS G ");
		    countSQL.append("WHERE UPPER(G.GOODS_NAME) LIKE ? AND G.STATUS = '1'");
	    
			// Step1:取得Connection
			try (Connection conn = DBConnectionFactory.getOracleDBConnection();
			    // Step2:Create prepareStatement For SQL
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());
					PreparedStatement countStmt = conn.prepareStatement(countSQL.toString());){
				stmt.setString(1, "%" + searchKeyword.toUpperCase() + "%");
				stmt.setInt(2, startRowNo);
				stmt.setInt(3, endRowNo);
				  countStmt.setString(1, "%" + searchKeyword.toUpperCase() + "%");
				try (ResultSet rs = stmt.executeQuery();ResultSet countRs = countStmt.executeQuery()){			
					if (countRs.next()) {
		                totalRecords = countRs.getInt(1);
		                goodsResult.setTotalRecords(totalRecords);
		            }
					while(rs.next()){
						Goods good = new Goods();
						good.setGoodsID(rs.getInt("GOODS_ID"));
						good.setGoodsName(rs.getString("GOODS_NAME"));
						good.setGoodsPrice(rs.getInt("PRICE"));
						good.setGoodsQuantity(rs.getInt("QUANTITY"));
						good.setGoodsImageName(rs.getString("IMAGE_NAME"));
						good.setStatus(rs.getString("STATUS"));
						goods.add(good);
					}
					goodsResult.setGoods(goods);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return goodsResult;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public GoodsMaxMin queryMaxMin() {
		GoodsMaxMin gdmn = new GoodsMaxMin();
		
		String querySQL = "SELECT MAX(PRICE),MIN(PRICE),MAX(QUANTITY) FROM BEVERAGE_GOODS ";

		// Step1:取得Connection
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				// Step2:Create prepareStatement For SQL
				PreparedStatement stmt = conn.prepareStatement(querySQL);) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					gdmn.setMinPrice(rs.getInt("MIN(PRICE)"));
					gdmn.setMaxPrice(rs.getInt("MAX(PRICE)"));
					gdmn.setMaxGoodsQuantity(rs.getInt("MAX(QUANTITY)"));

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return gdmn;
	}
	/**
	 * 多條件查詢
	 */
	public Set<Goods> queryCond(Cond cond, int startRowNo, int endRowNo,Integer GoodsID) {
		Set<Goods> goods = new LinkedHashSet<>();
		// 
		boolean flag=false;
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("SELECT * FROM ( ");
		querySQL.append("  SELECT ROWNUM ROW_NUM, G.* FROM BEVERAGE_GOODS G ");
		querySQL.append("  WHERE G.GOODS_ID LIKE ? AND UPPER(G.GOODS_NAME) LIKE ? ");
		querySQL.append("  AND G.PRICE BETWEEN ? AND ? AND G.QUANTITY <= ? ");
		querySQL.append("  AND G.STATUS LIKE ? ORDER BY G.GOODS_ID "+cond.getSortPrice());
		if (0!=endRowNo) {
			querySQL.append(")WHERE ROW_NUM > ? AND ROW_NUM < ?");
			flag=true;
		}else {
			querySQL.append(")");
		}

		// Step1:取得Connection
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				// Step2:Create prepareStatement For SQL
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());) {
			if (0!=GoodsID) {
				stmt.setInt(1, GoodsID);
			}else {
				stmt.setString(1, "%"+"%" );
			}
			stmt.setString(2, "%" + cond.getGoodsName().toUpperCase() + "%");
			stmt.setInt(3,cond.getMinPrice());
			stmt.setInt(4,cond.getMaxPrice());
			stmt.setInt(5,cond.getGoodsQuantity());
			stmt.setString(6, "%" + cond.getStatus() + "%");
//			stmt.setString(7, cond.getSortPrice());
			if (flag) {
				stmt.setInt(7, startRowNo);
				stmt.setInt(8, endRowNo);
			}
			try (ResultSet rs = stmt.executeQuery()){			
				while(rs.next()){
					Goods good = new Goods();
					good.setGoodsID(rs.getInt("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.add(good);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return goods;
	}

	
	
	
	/**
	 * //透過id查詢商品
	 */
	public Goods queryGoodsById(BigDecimal goodsID){
		Goods goods = null;		
		// querySQL SQL
		String querySQL = "select * from BEVERAGE_GOODS WHERE GOODS_ID = ?";		
		// Step1:取得Connection
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
		    // Step2:Create prepareStatement For SQL
			PreparedStatement stmt = conn.prepareStatement(querySQL)){
			stmt.setBigDecimal(1, goodsID);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()){
					goods = new Goods();
					goods.setGoodsID(rs.getInt("GOODS_ID"));
					goods.setGoodsName(rs.getString("GOODS_NAME"));
					goods.setGoodsPrice(rs.getInt("PRICE"));
					goods.setGoodsQuantity(rs.getInt("QUANTITY"));
					goods.setGoodsImageName(rs.getString("IMAGE_NAME"));
					goods.setStatus(rs.getString("STATUS"));
				}				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return goods;
	}
	
	
	
	/**
	 * 後臺管理商品列表
	 * @return Set(Goods)
	 */
	public Set<Goods> queryGoods() {
	    Set<Goods> goods = new LinkedHashSet<>(); 

	    String querySQL = "SELECT * FROM BEVERAGE_GOODS";

	    try (Connection conn = DBConnectionFactory.getOracleDBConnection();
	         PreparedStatement stmt = conn.prepareStatement(querySQL);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Goods good = new Goods();
	            good.setGoodsID(rs.getInt("GOODS_ID"));
	            good.setGoodsName(rs.getString("GOODS_NAME"));
	            good.setGoodsPrice(rs.getInt("PRICE"));
	            good.setGoodsQuantity(rs.getInt("QUANTITY"));
	            good.setGoodsImageName(rs.getString("IMAGE_NAME"));
	            good.setStatus(rs.getString("STATUS"));

	            goods.add(good);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return goods;
	}

	
	/**
	 * 後臺管理新增商品
	 * @param goods
	 * @return int(商品編號)
	 */
	public int createGoods(Goods goods){
		int goodsID = 0;
		String insert_stmt = "INSERT INTO BEVERAGE_GOODS "
				+ "(GOODS_ID,GOODS_NAME,PRICE,QUANTITY,IMAGE_NAME,STATUS) "
				+ "VALUES (BEVERAGE_GOODS_seq.NEXTVAL, ?, ?, ?, ?, ?)";
		String cols[] = { "GOODS_ID" };
		
		try (Connection con = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement pstmt = con.prepareStatement(insert_stmt, cols)) {
			pstmt.setString(1, goods.getGoodsName());
			pstmt.setInt(2, goods.getGoodsPrice());
			pstmt.setInt(3, goods.getGoodsQuantity());
			pstmt.setString(4, goods.getGoodsImageName());
			pstmt.setString(5, goods.getStatus());
			pstmt.executeUpdate();
			
			ResultSet rsKeys = pstmt.getGeneratedKeys();
			ResultSetMetaData rsmd = rsKeys.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rsKeys.next()) {
				for (int i = 1; i <= columnCount; i++) {
					rsKeys.getString(i);					
					goodsID=rsKeys.getInt(i);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return goodsID;
	}
	
	/**
	 * 後臺管理更新商品
	 * @param goods
	 * @return boolean
	 */
	public boolean updateGoods(Goods goods) {
		boolean updateSuccess = false;
		// Step1:取得Connection
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();){
			// 設置交易不自動提交
			conn.setAutoCommit(false);
			// Update SQL
			String updateSQL = "UPDATE BEVERAGE_GOODS SET PRICE = ?, QUANTITY = ?, STATUS = ? WHERE GOODS_ID = ?";
			// Step2:Create prepareStatement For SQL
			try (PreparedStatement stmt = conn.prepareStatement(updateSQL)){
				// Step3:將"資料欄位編號"、"資料值"作為引數傳入				
				stmt.setInt(1, goods.getGoodsPrice());
				stmt.setInt(2, goods.getGoodsQuantity());
				stmt.setString(3, goods.getStatus());
				stmt.setInt(4, goods.getGoodsID());
				// Step4:Execute SQL
				int recordCount = stmt.executeUpdate();
				updateSuccess = (recordCount > 0) ? true : false;
				// Step5:Transaction commit(交易提交)
				conn.commit();
			} catch (SQLException e) {
				// 若發生錯誤則資料 rollback(回滾)
				conn.rollback();
				throw e;
			}			
		} catch (SQLException e) {
			updateSuccess = false;
			e.printStackTrace();
		}
		
		return updateSuccess;
	}

	/**
	 * 後臺管理刪除商品
	 * @param goodsID
	 * @return boolean
	 */
	public boolean deleteGoods(BigDecimal goodsID) {
		boolean deleteSuccess = false;
		String deleteSql = "DELETE FROM BEVERAGE_GOODS WHERE GOODS_ID = ?";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
			conn.setAutoCommit(false);
			stmt.setBigDecimal(1, goodsID);
			stmt.executeUpdate();
			conn.commit();
			deleteSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deleteSuccess;
	}
	
	/**
	 * 後臺管理顧客訂單查詢
	 * @param queryStartDate
	 * @param queryEndDate
	 * @return Set(SalesReport)
	 */
	public Set<SalesReport> queryOrderBetweenDate(String queryStartDate, String queryEndDate) {
		Set<SalesReport> reports = new LinkedHashSet<>();
		String querySQL = "SELECT o.ORDER_ID, m.CUSTOMER_NAME, o.ORDER_DATE, g.GOODS_NAME,"
                + "g.PRICE, o.BUY_QUANTITY, g.PRICE, o.BUY_QUANTITY*g.PRICE buyAmount FROM BEVERAGE_ORDER o "
                + "JOIN BEVERAGE_MEMBER m ON o.CUSTOMER_ID = m.IDENTIFICATION_NO "
                + "JOIN BEVERAGE_GOODS g ON o.GOODS_ID = g.GOODS_ID "
                + "WHERE o.ORDER_DATE "
                + "BETWEEN TO_DATE(?, 'YYYY-mm-DD HH24:MI:SS') AND TO_DATE(?, 'YYYY-mm-DD HH24:MI:SS') "
                + "ORDER BY o.ORDER_ID";
		
				try (Connection conn = DBConnectionFactory.getOracleDBConnection();
					PreparedStatement stmt = conn.prepareStatement(querySQL)){
					stmt.setString(1, queryStartDate + " 00:00:00");
					stmt.setString(2, queryEndDate + " 23:59:59");
					try (ResultSet rs = stmt.executeQuery()){
						while(rs.next()) {
							Long orderID=rs.getLong("ORDER_ID");
							String customerName = rs.getString("CUSTOMER_NAME");
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String orderDateStr = rs.getString("ORDER_DATE");
							Date orderDate = sdf.parse(orderDateStr);
							String formattedOrderDate = sdf.format(orderDate);
							
							String goodsName = rs.getString("GOODS_NAME");
							int goodsBuyPrice = rs.getInt("PRICE");
							int buyQuantity = rs.getInt("BUY_QUANTITY");
							int buyAmount = rs.getInt("BUYAMOUNT");

							SalesReport salesReport = new SalesReport();
							salesReport.setOrderID(orderID);
							salesReport.setCustomerName(customerName);
							salesReport.setOrderDate(formattedOrderDate);
							salesReport.setGoodsName(goodsName);
							salesReport.setGoodsBuyPrice(goodsBuyPrice);
							salesReport.setBuyQuantity(buyQuantity);
							salesReport.setBuyAmount(buyAmount);
							reports.add(salesReport);
						}	
					} catch (SQLException e) {
						throw e;
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		return reports;
	}
	/**
	 * 查詢所有報表
	 */
	public Set<SalesReport> queryAllOrder() {
		Set<SalesReport> reports = new LinkedHashSet<>();
		String querySQL = "SELECT o.ORDER_ID, m.CUSTOMER_NAME, o.ORDER_DATE, g.GOODS_NAME,"
                + "g.PRICE, o.BUY_QUANTITY, g.PRICE, o.BUY_QUANTITY*g.PRICE buyAmount FROM BEVERAGE_ORDER o "
                + "JOIN BEVERAGE_MEMBER m ON o.CUSTOMER_ID = m.IDENTIFICATION_NO "
                + "JOIN BEVERAGE_GOODS g ON o.GOODS_ID = g.GOODS_ID "
                + "ORDER BY o.ORDER_ID";
		
				try (Connection conn = DBConnectionFactory.getOracleDBConnection();
					PreparedStatement stmt = conn.prepareStatement(querySQL)){
					try (ResultSet rs = stmt.executeQuery()){
						while(rs.next()) {
							Long orderID=rs.getLong("ORDER_ID");
							String customerName = rs.getString("CUSTOMER_NAME");
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String orderDateStr = rs.getString("ORDER_DATE");
							Date orderDate = sdf.parse(orderDateStr);
							String formattedOrderDate = sdf.format(orderDate);
							
							String goodsName = rs.getString("GOODS_NAME");
							int goodsBuyPrice = rs.getInt("PRICE");
							int buyQuantity = rs.getInt("BUY_QUANTITY");
							int buyAmount = rs.getInt("BUYAMOUNT");

							SalesReport salesReport = new SalesReport();
							salesReport.setOrderID(orderID);
							salesReport.setCustomerName(customerName);
							salesReport.setOrderDate(formattedOrderDate);
							salesReport.setGoodsName(goodsName);
							salesReport.setGoodsBuyPrice(goodsBuyPrice);
							salesReport.setBuyQuantity(buyQuantity);
							salesReport.setBuyAmount(buyAmount);
							reports.add(salesReport);
						}	
					} catch (SQLException e) {
						throw e;
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		return reports;
	}
	
	
}
