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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.training.model.Goods;
import com.training.model.Member;
import com.training.utils.DBConnectionFactory;
import com.training.vo.GoodsResult;



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
public class FrontEndDao {
	private static FrontEndDao frontEndDao = new FrontEndDao();

	private FrontEndDao() {
	}

	public static FrontEndDao getInstance() {
		return frontEndDao;
	}
	
	public Goods queryGoodsById(String goodsIDs) {
		// 分頁查詢
		String querySQL = "SELECT * FROM BEVERAGE_GOODS WHERE GOODS_ID=?";
		Goods good = new Goods();
		// Step1:取得Connection
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
		    // Step2:Create prepareStatement For SQL
			PreparedStatement stmt = conn.prepareStatement(querySQL);){
			stmt.setString(1, goodsIDs);
			try (ResultSet rs = stmt.executeQuery()){			
				while(rs.next()){
					good.setGoodsID(rs.getBigDecimal("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return good;
	}
	
	public Member register(Member m) {
		
		boolean insertSuccess = false;
	
			String insertSQL = "INSERT INTO BEVERAGE_MEMBER VALUES (?,?,?)";
			// Step1:取得Connection
			try (Connection conn = DBConnectionFactory.getOracleDBConnection();
					// Step2:Create prepareStatement For SQL
				PreparedStatement pstmt = conn.prepareStatement(insertSQL)){
				// Step3:將"資料欄位編號"、"資料值"作為引數傳入
				pstmt.setString(1, m.getIdentificationNo());
				pstmt.setString(2, m.getPassword());
				pstmt.setString(3, m.getCustomerName());
				// Step4:Execute SQL
				pstmt.executeUpdate();
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return m;
	}
	
	public Member queryMember(Member m) {
		Member member = null;	
				// querySQL SQL
				String querySQL = "select * from BEVERAGE_MEMBER WHERE IDENTIFICATION_NO = ? AND PASSWORD=?";		
				// Step1:取得Connection
				try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				    // Step2:Create prepareStatement For SQL
					PreparedStatement stmt = conn.prepareStatement(querySQL)){
					stmt.setString(1, m.getIdentificationNo());
					stmt.setString(2, m.getPassword());
					try(ResultSet rs = stmt.executeQuery()){
						if(rs.next()){
							member = new Member();
							member.setIdentificationNo(rs.getString("IDENTIFICATION_NO"));
							member.setPassword(rs.getString("PASSWORD"));
							member.setCustomerName(rs.getString("CUSTOMER_NAME"));
						}				
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		
		return member;
		
	}
	/**
	 * 前臺顧客登入查詢
	 * 
	 * @param identificationNo
	 * @return Member
	 */
	public Member queryMemberByIdentificationNo(String identificationNo) {
		Member member = null;
		String querySQL = "SELECT * FROM BEVERAGE_MEMBER WHERE IDENTIFICATION_NO = ?";

		try (Connection conn = DBConnectionFactory.getOracleDBConnection();

				PreparedStatement stmt = conn.prepareStatement(querySQL)) {
			stmt.setString(1, identificationNo);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String IdentificationNo = rs.getString("IDENTIFICATION_NO");
					String password = rs.getString("PASSWORD");
					String customerName = rs.getString("CUSTOMER_NAME");
					member = new Member();
					member.setIdentificationNo(IdentificationNo);
					member.setPassword(password);
					member.setCustomerName(customerName);
				}
			} catch (SQLException e) {
				throw e;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return member;
	}

	/**
	 * 前臺顧客瀏灠商品
	 * 
	 * @param searchKeyword
	 * @param startRowNo
	 * @param endRowNo
	 * @return Set(Goods)
	 */
	public GoodsResult searchGoods(String searchKeyword, int startRowNo, int endRowNo) {
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
					good.setGoodsID(rs.getBigDecimal("GOODS_ID"));
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
	 * 查詢顧客所購買商品資料(價格、庫存)
	 * 
	 * @param goodsIDs
	 * @return Map(BigDecimal, Goods)
	 */
	public Map<BigDecimal, Goods> queryBuyGoods(Set<BigDecimal> goodsIDs) {
		// key:商品編號、value:商品
		Map<BigDecimal, Goods> goods = new LinkedHashMap<>();
		List<BigDecimal> goodsIDsList = new ArrayList<>(goodsIDs);
		Collections.reverse(goodsIDsList);
		String querySQL = "SELECT GOODS_ID,GOODS_NAME,PRICE,QUANTITY FROM " + "BEVERAGE_GOODS WHERE GOODS_ID = ?";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL)) {
			for (BigDecimal goodsID : goodsIDsList) {
				stmt.setBigDecimal(1, goodsID);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						Goods good = new Goods();
						good.setGoodsID(rs.getBigDecimal("GOODS_ID"));
						good.setGoodsName(rs.getString("GOODS_NAME"));
						good.setGoodsPrice(rs.getInt("PRICE"));
						good.setGoodsQuantity(rs.getInt("QUANTITY"));
						goods.put(goodsID, good);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return goods;
	}

	/**
	 * 交易完成更新扣商品庫存數量
	 * 
	 * @param goods
	 * @return boolean
	 */
	public boolean batchUpdateGoodsQuantity(Set<Goods> goods){
		boolean updateSuccess = false;
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
			 Statement stmt = conn.createStatement()) {
			// 設置交易不自動提交
			conn.setAutoCommit(false);
			// addBatch 批次執行SQL指令
			String updateGoodsSql = "UPDATE BEVERAGE_GOODS SET QUANTITY = %s WHERE GOODS_ID = %s";
			for(Goods good : goods){
				stmt.addBatch(String.format(updateGoodsSql,good.getGoodsQuantity(),good.getGoodsID()));
			}
			int[] updateCounts = stmt.executeBatch();
			for(int count : updateCounts) {
				// 只要有一筆更新失敗視為整批更新失敗資料rollback
				if(count != 1){
					updateSuccess = false;
					conn.rollback();
					break;
				}
				updateSuccess = true;
			}
			// 交易提交
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return updateSuccess;
	}

	/**
	 * 建立訂單資料
	 * 
	 * @param customerID
	 * @param goodsOrders【訂單資料(key:購買商品、value:購買數量)】
	 * @return boolean
	 */
	public boolean batchCreateGoodsOrder(String customerID, Map<Goods,Integer> goodsOrders){
		boolean insertSuccess = false;
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
			 Statement stmt = conn.createStatement()) {
			// 設置交易不自動提交
			conn.setAutoCommit(false);			
			// Insert SQL
			String insertSQL = "INSERT INTO BEVERAGE_ORDER ";
			insertSQL += "(ORDER_ID, ORDER_DATE, CUSTOMER_ID, GOODS_ID, GOODS_BUY_PRICE, BUY_QUANTITY) "; 
			insertSQL += "VALUES (BEVERAGE_ORDER_SEQ.NEXTVAL, TO_DATE('%s', 'yyyy/MM/dd HH24:mi:ss'), '%s', '%s', '%s', '%s')";
			// 訂單日期
			String orderDate = BackEndDao.sf.format(new Date());
			// 訂單資料(key:購買商品、value:購買數量)
			for(Goods good : goodsOrders.keySet()){
				int buyQuantity = goodsOrders.get(good);				
				stmt.addBatch(String.format(insertSQL, orderDate, customerID, good.getGoodsID(), good.getGoodsPrice(), buyQuantity));
			}
			int[] insertCounts = stmt.executeBatch();
			for (int count : insertCounts) {
				// 只要有一筆更新失敗視為整批更新失敗資料rollback
				if(count != 1){
					insertSuccess = false;
					conn.rollback();
					break;
				}
				insertSuccess = true;
			}
			// 交易提交
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return insertSuccess;
	}	

}
