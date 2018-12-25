package service.calculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.calculator.utils.AddCredits;
import service.calculator.utils.ExecutionService;

@RestController
public class CalculatorController {

	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private AddCredits addCredtis;
	
	@RequestMapping(value="/api/v1/addcredit")
	public String updateCredits(
			@RequestParam(value="userid") Integer userid,
			@RequestParam(value="credits") Double credits
			) {
		
		return addCredtis.addCredits(userid, credits);

	}
	
	@RequestMapping(value="/api/v1/evaluateoperation", method=RequestMethod.GET)
	public String evaluateOperation(
			@RequestParam(value="userid") Integer userid,
			@RequestParam(value="operation") String operation,
			@RequestParam(value="value1") Double value1,
			@RequestParam(value="value2") Double value2
			) {
		
		return executionService.performExecution(operation, userid, value1, value2);
	}
	
	
}
