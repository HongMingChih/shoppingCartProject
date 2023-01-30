package tw.com.pcschool.springboot.Model;

import java.sql.Date;

public class Beverage_Order {
	private Integer ORDER_ID;// 訂單編號
	private Date ORDER_DATE;// 訂單日期
	private String CUSTOMER_ID;// 顧客編號(BEVERAGE_MEMBER.IDENTIFICATION_NO)
	private Integer GOODS_ID;// 商品編號(BEVERAGE_GOODS.GOODS_ID)
	private Integer GOODS_BUY_PRICE;// 商品金額(購買單價)
	private Integer BUY_QUANTITY;// 購買數量
	public Beverage_Order() {
		super();
	}
	public Beverage_Order(Integer oRDER_ID, Date oRDER_DATE, String cUSTOMER_ID, Integer gOODS_ID,
			Integer gOODS_BUY_PRICE, Integer bUY_QUANTITY) {
		super();
		ORDER_ID = oRDER_ID;
		ORDER_DATE = oRDER_DATE;
		CUSTOMER_ID = cUSTOMER_ID;
		GOODS_ID = gOODS_ID;
		GOODS_BUY_PRICE = gOODS_BUY_PRICE;
		BUY_QUANTITY = bUY_QUANTITY;
	}
	public Integer getORDER_ID() {
		return ORDER_ID;
	}
	public void setORDER_ID(Integer oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}
	public Date getORDER_DATE() {
		return ORDER_DATE;
	}
	public void setORDER_DATE(Date oRDER_DATE) {
		ORDER_DATE = oRDER_DATE;
	}
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		CUSTOMER_ID = cUSTOMER_ID;
	}
	public Integer getGOODS_ID() {
		return GOODS_ID;
	}
	public void setGOODS_ID(Integer gOODS_ID) {
		GOODS_ID = gOODS_ID;
	}
	public Integer getGOODS_BUY_PRICE() {
		return GOODS_BUY_PRICE;
	}
	public void setGOODS_BUY_PRICE(Integer gOODS_BUY_PRICE) {
		GOODS_BUY_PRICE = gOODS_BUY_PRICE;
	}
	public Integer getBUY_QUANTITY() {
		return BUY_QUANTITY;
	}
	public void setBUY_QUANTITY(Integer bUY_QUANTITY) {
		BUY_QUANTITY = bUY_QUANTITY;
	}
	@Override
	public String toString() {
		return "Beverage_Order [ORDER_ID=" + ORDER_ID + ", ORDER_DATE=" + ORDER_DATE + ", CUSTOMER_ID=" + CUSTOMER_ID
				+ ", GOODS_ID=" + GOODS_ID + ", GOODS_BUY_PRICE=" + GOODS_BUY_PRICE + ", BUY_QUANTITY=" + BUY_QUANTITY
				+ "]";
	}
	
}
