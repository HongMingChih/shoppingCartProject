package tw.com.pcschool.springboot.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import tw.com.pcschool.springboot.Model.Beverage_Goods;
import tw.com.pcschool.springboot.Model.Beverage_Member;
import tw.com.pcschool.springboot.Model.SalesReport;

@Mapper
public interface BeverageMapper {
			

			// 新增
			@Insert("insert into Beverage_Goods(GOODS_ID,GOODS_NAME,PRICE,QUANTITY,IMAGE_NAME,STATUS) "
					+ "values(Beverage_Goods_seq.NEXTVAL,#{GOODS_NAME},#{PRICE},#{QUANTITY},#{IMAGE_NAME},#{STATUS})")
			void createGoods(Beverage_Goods beverage_Goods);
			
			// 新增帳號
			@Insert("insert into BEVERAGE_MEMBER(IDENTIFICATION_NO,PASSWORD,CUSTOMER_NAME) "
					+ "values(#{IDENTIFICATION_NO},#{PASSWORD},#{CUSTOMER_NAME})")
			void addMember(Beverage_Member beverage_Member);

			// 查詢帳號密碼
			@Select("select * from BEVERAGE_MEMBER where IDENTIFICATION_NO=#{IDENTIFICATION_NO} and PASSWORD=#{PASSWORD}")
			List<Beverage_Member>  queryUser(String IDENTIFICATION_NO,String PASSWORD);
			
			
			@Select("select * from BEVERAGE_MEMBER where IDENTIFICATION_NO=#{IDENTIFICATION_NO}")
			List<Beverage_Member> queryUser1(String IDENTIFICATION_NO);
			
			@Select("SELECT * FROM BEVERAGE_GOODS")
			List<Beverage_Goods> queryAll();
			
			@Select("SELECT * FROM BEVERAGE_GOODS WHERE GOODS_ID=#{GOODS_ID}")
			List<Beverage_Goods> queryBgId(Integer goodsID);
			
			@Select("SELECT QUANTITY FROM BEVERAGE_GOODS WHERE GOODS_NAME=#{GOODS_NAME}")
			String queryGoodsNameQuantity(String goodsName);
			
			//分頁搜尋
			@Select("SELECT * FROM (SELECT CEIL(ROWNUM / 6) C, "
					+ "G.* FROM BEVERAGE_GOODS G WHERE G.STATUS = '1' "
					+ "ORDER BY G.GOODS_ID ) WHERE C=#{pageNo}")
			List<Beverage_Goods> queryGroup(String pageNo);
			
			
			//分頁搜尋資料組別rowg
			@Select("SELECT COUNT(DISTINCT RN)AS rowg FROM (SELECT CEIL(ROWNUM / 6) RN, "
					+ "G.* FROM BEVERAGE_GOODS G WHERE G.STATUS = '1' ORDER BY G.GOODS_ID)")
			Integer queryRowg();
			
			@Select("SELECT * FROM Beverage_Goods WHERE LOWER(GOODS_NAME) LIKE '%' || #{GOODS_NAME} || '%'")
			List<Beverage_Goods> queryFrontend(String GOODS_NAME);
			
			@Select("SELECT * FROM BEVERAGE_MEMBER")
			List<Beverage_Member> queryAllMember();
			
			  @Select("SELECT o.ORDER_ID orderID, m.CUSTOMER_NAME customerName, o.ORDER_DATE orderDate, g.GOODS_NAME goodsName,"
			          + "g.PRICE goodsBuyPrice, o.BUY_QUANTITY buyQuantity, o.BUY_QUANTITY*g.PRICE buyAmount FROM BEVERAGE_ORDER o "
			          + "JOIN BEVERAGE_MEMBER m ON o.CUSTOMER_ID = m.IDENTIFICATION_NO "
			          + "JOIN BEVERAGE_GOODS g ON o.GOODS_ID = g.GOODS_ID "
			          + "WHERE o.ORDER_DATE "
			          + "BETWEEN TO_DATE(#{queryStartDate}, 'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(#{queryEndDate}, 'YYYY-MM-DD HH24:MI:SS') "
			          + "ORDER BY orderDate,orderID")
			  List<SalesReport> queryOrderBetweenDate(@Param("queryStartDate") String queryStartDate, @Param("queryEndDate") String queryEndDate);
			
			
			 @Select("SELECT o.ORDER_ID orderID, m.CUSTOMER_NAME customerName, o.ORDER_DATE orderDate, g.GOODS_NAME goodsName,"
			 		  + "g.PRICE goodsBuyPrice, o.BUY_QUANTITY buyQuantity,"
			          + "o.BUY_QUANTITY*g.PRICE buyAmount FROM BEVERAGE_ORDER o "
			          + "JOIN BEVERAGE_MEMBER m ON o.CUSTOMER_ID = m.IDENTIFICATION_NO "
			          + "JOIN BEVERAGE_GOODS g ON o.GOODS_ID = g.GOODS_ID "
			          + "ORDER BY orderID")
			 		List<SalesReport> queryOrder();
		
			// 更新
			@Update("update Beverage_Goods set GOODS_NAME=#{GOODS_NAME},"
					+ "PRICE=#{PRICE},QUANTITY=#{QUANTITY},"
					+ "IMAGE_NAME=#{IMAGE_NAME},STATUS=#{STATUS}where GOODS_ID=#{GOODS_ID}")
			void update(Beverage_Goods m);
			


			// 刪除
			@Delete("delete from Beverage_Goods where id=#{id}")
			void delete(int id);
}
