package service.calculator.responses;


public class ErrorResponse {

	private String message;
	private Boolean successFlag;
	
	public Boolean getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(Boolean successFlag) {
		this.successFlag = successFlag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ErrorResponse(String message) {
		super();
		this.message = message;
	}
	
	public ErrorResponse() {}
}
