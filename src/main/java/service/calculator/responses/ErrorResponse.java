package service.calculator.responses;


public class ErrorResponse {

	private Integer responseCode;
	private String message;
	private Boolean successFlag;
	
	public Boolean getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(Boolean successFlag) {
		this.successFlag = successFlag;
	}
	public Integer getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ErrorResponse(Integer responseCode, String message) {
		super();
		this.responseCode = responseCode;
		this.message = message;
	}
	
	public ErrorResponse() {}
}
