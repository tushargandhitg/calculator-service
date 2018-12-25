package service.calculator.responses;

public class SuccessResponse {

	private Double result;
	private String message;
	private Boolean successFlag;

	public SuccessResponse(Double result) {
		super();
		this.result = result;
	}

	public SuccessResponse(Double result, String message) {
		super();
		this.result = result;
		this.message = message;
	}

	public SuccessResponse(Double result, String message, Boolean successFlag) {
		super();
		this.result = result;
		this.message = message;
		this.successFlag = successFlag;
	}

	public Boolean getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(Boolean successFlag) {
		this.successFlag = successFlag;
	}

	public SuccessResponse() {
		result = null;
		message = null;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	} 
	
	
}
