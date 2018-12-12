package service.calculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.calculator.utils.CalculationService;
import service.calculator.utils.ExecutionService;

@RestController
public class CalculatorController {

	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private CalculationService calculationService;
	
	@RequestMapping(value="/api/v1/calculate", method=RequestMethod.GET)
	public Double calculateResult(
			@RequestParam(value="value1", required=true) double value1,
			@RequestParam(value="value2", required=true) double value2,
			@RequestParam(value="operation", required=true) String operation
			) {
		return calculationService.performOperation(operation, value1, value2);
	}
	
	@RequestMapping(value="/api/v1/evaluateoperation", method=RequestMethod.GET)
	public Double evaluateOperation(
			@RequestParam(value="userid") Integer userid,
			@RequestParam(value="operation") String operation,
			@RequestParam(value="value1") Double value1,
			@RequestParam(value="value2") Double value2
			) {
		
		return executionService.performExecution(operation, userid, value1, value2);
		
	}
	
	
}
