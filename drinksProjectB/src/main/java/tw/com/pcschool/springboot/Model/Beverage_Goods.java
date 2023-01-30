package tw.com.pcschool.springboot.Model;

public class Beverage_Goods {
	private Integer GOODS_ID;// 商品編號
	private String GOODS_NAME;// 商品名稱
	private Integer PRICE;// 商品價格
	private Integer QUANTITY;// 庫存數量
	private String IMAGE_NAME;// 商品圖片名稱
	private String STATUS;// 商品狀態(1上架、0下架)
	private Integer GROUP_ID;
	private int pageNum;
	private int pageSize;
	
	public int getPageNum() {
		return pageNum;
	}


	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public Beverage_Goods() {
		super();
	}

	
	public Integer getGROUP_ID() {
		return GROUP_ID;
	}


	public void setGROUP_ID(Integer gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}


	public Beverage_Goods(Integer gOODS_ID, String gOODS_NAME, Integer pRICE, Integer qUANTITY, String iMAGE_NAME,
			String sTATUS, Integer gROUP_ID) {
		super();
		GOODS_ID = gOODS_ID;
		GOODS_NAME = gOODS_NAME;
		PRICE = pRICE;
		QUANTITY = qUANTITY;
		IMAGE_NAME = iMAGE_NAME;
		STATUS = sTATUS;
		GROUP_ID = gROUP_ID;
	}


	public Beverage_Goods(String gOODS_NAME, Integer pRICE, Integer qUANTITY, String iMAGE_NAME, String sTATUS) {
		super();
		GOODS_NAME = gOODS_NAME;
		PRICE = pRICE;
		QUANTITY = qUANTITY;
		IMAGE_NAME = iMAGE_NAME;
		STATUS = sTATUS;
	}



	public Beverage_Goods(Integer gOODS_ID, String gOODS_NAME, Integer pRICE, Integer qUANTITY,
			String iMAGE_NAME, String sTATUS) {
		super();
		GOODS_ID = gOODS_ID;
		GOODS_NAME = gOODS_NAME;
		PRICE = pRICE;
		QUANTITY = qUANTITY;
		IMAGE_NAME = iMAGE_NAME;
		STATUS = sTATUS;
	}



	public Integer getGOODS_ID() {
		return GOODS_ID;
	}


	public void setGOODS_ID(Integer gOODS_ID) {
		GOODS_ID = gOODS_ID;
	}


	public String getGOODS_NAME() {
		return GOODS_NAME;
	}


	public void setGOODS_NAME(String gOODS_NAME) {
		GOODS_NAME = gOODS_NAME;
	}


	

	public Integer getPRICE() {
		return PRICE;
	}


	public void setPRICE(Integer pRICE) {
		PRICE = pRICE;
	}


	public Integer getQUANTITY() {
		return QUANTITY;
	}


	public void setQUANTITY(Integer qUANTITY) {
		QUANTITY = qUANTITY;
	}


	public String getIMAGE_NAME() {
		return IMAGE_NAME;
	}


	public void setIMAGE_NAME(String iMAGE_NAME) {
		IMAGE_NAME = iMAGE_NAME;
	}


	public String getSTATUS() {
		return STATUS;
	}


	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}


	@Override
	public String toString() {
		return "Beverage_Goods [GOODS_ID=" + GOODS_ID + ", GOODS_NAME=" + GOODS_NAME + ", PRICE=" + PRICE
				+ ", QUANTITY=" + QUANTITY + ", IMAGE_NAME=" + IMAGE_NAME + ", STATUS=" + STATUS + "]";
	}




	

}
