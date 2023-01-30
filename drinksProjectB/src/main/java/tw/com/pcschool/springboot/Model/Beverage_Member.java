package tw.com.pcschool.springboot.Model;

public class Beverage_Member {
	private String IDENTIFICATION_NO;// 身份證字號
	private String PASSWORD;// 密碼
	private String CUSTOMER_NAME;// 顧客姓名

	public Beverage_Member() {
		super();
	}

	public Beverage_Member(String iDENTIFICATION_NO, String pASSWORD, String cUSTOMER_NAME) {
		super();
		IDENTIFICATION_NO = iDENTIFICATION_NO;
		PASSWORD = pASSWORD;
		CUSTOMER_NAME = cUSTOMER_NAME;
	}

	public String getIDENTIFICATION_NO() {
		return IDENTIFICATION_NO;
	}

	public void setIDENTIFICATION_NO(String iDENTIFICATION_NO) {
		IDENTIFICATION_NO = iDENTIFICATION_NO;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getCUSTOMER_NAME() {
		return CUSTOMER_NAME;
	}

	public void setCUSTOMER_NAME(String cUSTOMER_NAME) {
		CUSTOMER_NAME = cUSTOMER_NAME;
	}

	@Override
	public String toString() {
		return "Beverage_Member [IDENTIFICATION_NO=" + IDENTIFICATION_NO + ", PASSWORD=" + PASSWORD + ", CUSTOMER_NAME="
				+ CUSTOMER_NAME + "]";
	}

}
