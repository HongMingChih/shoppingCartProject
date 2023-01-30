package tw.com.pcschool.springboot.Model;

public class Geography {
	private Integer GEOGRAPHY_ID;// 地區編號
	private String REGION_NAME;// 地區名稱

	public Geography() {
		super();
	}

	public Geography(Integer gEOGRAPHY_ID, String rEGION_NAME) {
		super();
		GEOGRAPHY_ID = gEOGRAPHY_ID;
		REGION_NAME = rEGION_NAME;
	}

	public Integer getGEOGRAPHY_ID() {
		return GEOGRAPHY_ID;
	}

	public void setGEOGRAPHY_ID(Integer gEOGRAPHY_ID) {
		GEOGRAPHY_ID = gEOGRAPHY_ID;
	}

	public String getREGION_NAME() {
		return REGION_NAME;
	}

	public void setREGION_NAME(String rEGION_NAME) {
		REGION_NAME = rEGION_NAME;
	}

	@Override
	public String toString() {
		return "Geography [GEOGRAPHY_ID=" + GEOGRAPHY_ID + ", REGION_NAME=" + REGION_NAME + "]";
	}

}
