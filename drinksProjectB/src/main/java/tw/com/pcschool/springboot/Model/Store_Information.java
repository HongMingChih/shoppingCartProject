package tw.com.pcschool.springboot.Model;

import java.util.Date;

public class Store_Information {
	private Integer STORE_ID;// 商店編號
	private String STORE_NAME;// 商店名稱
	private Integer SALES;// 營業額

	// @Temporal(TemporalType.TIMESTAMP)
	private Date STORE_DATE; // 開始營業日
	private Integer GEOGRAPHY_ID;// 地區編號

	public Store_Information() {
		super();
	}

	public Store_Information(Integer sTORE_ID, String sTORE_NAME, Integer sALES, Date sTORE_DATE,
			Integer gEOGRAPHY_ID) {
		super();
		STORE_ID = sTORE_ID;
		STORE_NAME = sTORE_NAME;
		SALES = sALES;
		STORE_DATE = sTORE_DATE;
		GEOGRAPHY_ID = gEOGRAPHY_ID;
	}

	public Integer getSTORE_ID() {
		return STORE_ID;
	}

	public void setSTORE_ID(Integer sTORE_ID) {
		STORE_ID = sTORE_ID;
	}

	public String getSTORE_NAME() {
		return STORE_NAME;
	}

	public void setSTORE_NAME(String sTORE_NAME) {
		STORE_NAME = sTORE_NAME;
	}

	public Integer getSALES() {
		return SALES;
	}

	public void setSALES(Integer sALES) {
		SALES = sALES;
	}

	public Date getSTORE_DATE() {
		return STORE_DATE;
	}

	public void setSTORE_DATE(Date sTORE_DATE) {
		STORE_DATE = sTORE_DATE;
	}

	public Integer getGEOGRAPHY_ID() {
		return GEOGRAPHY_ID;
	}

	public void setGEOGRAPHY_ID(Integer gEOGRAPHY_ID) {
		GEOGRAPHY_ID = gEOGRAPHY_ID;
	}

	@Override
	public String toString() {
		return "Store_Information [STORE_ID=" + STORE_ID + ", STORE_NAME=" + STORE_NAME + ", SALES=" + SALES
				+ ", STORE_DATE=" + STORE_DATE + ", GEOGRAPHY_ID=" + GEOGRAPHY_ID + "]";
	}

}
