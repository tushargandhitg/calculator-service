package service.calculator.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilityMethods {

	@Autowired
	private TransactionLogRepository transactionLog;
	
	
	public void logTransaction(Integer userid, String operation, TransactionStatus success, Double updatedUserCredits,
			Double cost) {
		
		TransactionLog log = new TransactionLog();
		log.setUserid(userid);
		log.setOperation(operation);
		log.setSuccess(success);
		log.setUpdatedCreditScore(updatedUserCredits);
		log.setOperationCost(cost);
		
		transactionLog.save(log);
		
	}
	
}
