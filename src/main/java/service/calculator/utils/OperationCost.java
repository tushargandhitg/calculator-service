package service.calculator.utils;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OperationCost {

	@Id
	private String operation;
	
	private double cost;

	public OperationCost() {}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	
	
}
