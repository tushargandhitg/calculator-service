package service.calculator.responses;


public class AddCreditResponse {

	private Boolean successFlag;
	private Double credits;
	private Integer userId;
	
	
	public Boolean getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(Boolean successFlag) {
		this.successFlag = successFlag;
	}
	public Double getCredits() {
		return credits;
	}
	public void setCredits(Double credits) {
		this.credits = credits;
	}
	public Integer getUserId() {
		return userId;
	}
	
	public AddCreditResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AddCreditResponse(Boolean successFlag, Double credits, Integer userId) {
		super();
		this.successFlag = successFlag;
		this.credits = credits;
		this.userId = userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
