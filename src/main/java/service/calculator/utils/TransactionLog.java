package service.calculator.utils;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="transaction_log")
public class TransactionLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="auto_id", updatable = false, nullable = false)
	private Long id;
	
	@Column(nullable = false)
	private Integer userid;
	
	@Column(nullable = false)
	private String operation;
	
	@Column(name="operation_cost" , nullable = false)
	private Double operationCost;
	
	@Column(name="updated_credit_score" , nullable = false)
	private Double updatedCreditScore;
	
	@Column(name="entry_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp entryDate;

	@Enumerated(EnumType.STRING)
	@Column( nullable = false)
	private TransactionStatus success;
	
	@Column(nullable = false)
	public TransactionStatus isSuccess() {
		return success;
	}

	public void setSuccess(TransactionStatus success) {
		this.success = success;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Double getOperationCost() {
		return operationCost;
	}

	public void setOperationCost(Double operationCost) {
		this.operationCost = operationCost;
	}

	public Double getUpdatedCreditScore() {
		return updatedCreditScore;
	}

	public void setUpdatedCreditScore(Double updatedCreditScore) {
		this.updatedCreditScore = updatedCreditScore;
	}

	public Timestamp getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Timestamp entryDate) {
		this.entryDate = entryDate;
	} 
	
}
