package service.calculator.kafka;


public class KafkaMessage {
	
	private Double currentCredits;
	private Double creditCharge;
	private String message;
	private String emailId;
	private String operation;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Double getCurrentCredits() {
		return currentCredits;
	}
	public void setCurrentCredits(Double currentCredits) {
		this.currentCredits = currentCredits;
	}
	public Double getCreditCharge() {
		return creditCharge;
	}
	public void setCreditCharge(Double creditCharge) {
		this.creditCharge = creditCharge;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public KafkaMessage() {}
	
	public KafkaMessage(Double currentCredits, Double creditCharge, String message, String emailId, String operation) {
		super();
		this.currentCredits = currentCredits;
		this.creditCharge = creditCharge;
		this.message = message;
		this.emailId = emailId;
		this.operation = operation;
	}

	
}
