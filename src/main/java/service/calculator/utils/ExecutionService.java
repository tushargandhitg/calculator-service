package service.calculator.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExecutionService {

	@Autowired
	private CalculationService calculationService;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private OperationCostRepository operationCostRepo;
	
	@Value("${service.user.url}")
	private String url;
	
	@Value("${service.user.port}")
	private int port;
	
	@Value("${service.user.credits}")
	private String creditUrl;
	
	@Value("${service.user.email}")
	private String emailUrl;
	
	@Value("${service.user.updateCredits}")
	private String updateCreditUrl;
	
	public Double performExecution(String operation, Integer userid, Double value1, Double value2) {
		
		// get cost of operation
		Optional<OperationCost> findByIdResult = operationCostRepo.findById(operation);
		Double cost = findByIdResult.get().getCost();
		
		// get current credits
		Double currentCredits = getCurrentCredits(userid);
		
		// check eligibility if feasible
		if(!isOperationFeasible(currentCredits, cost)) {
			return -999999.99999;
		}
		
		// perform operation
		Double updatedUserCredits = currentCredits - cost;
			
		// update db
		updateCurrentCredits(userid, updatedUserCredits);
		
		// set/update cache redis
		updateCacheRedis(userid,updatedUserCredits);
		
		// return result
		return evaluateOperationResult(operation,value1, value2);
	}
	
	private boolean updateCacheRedis(Integer userid, Double credits) {
		
		redisTemplate.opsForValue().set("user_"+userid.toString(), credits);
		return true;
	}
	
	private Double evaluateOperationResult(String operation, Double value1, Double value2) {
		
		return calculationService.performOperation(operation, value1, value2);
	}
	
		private boolean isOperationFeasible(Double currentCredits, Double cost) {
			
			if(currentCredits < cost) {
				return false;
			}
			
			return true;
		}
		
		private void updateCurrentCredits(Integer userid, Double credits) {
			
			url = url + ":"+Integer.toString(port)+updateCreditUrl;
			
			MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
			queryParams.add("userid", Integer.toString(userid));
			queryParams.add("credits", Double.toString(credits));
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			builder.queryParams(queryParams);
			
			restTemplate.exchange(builder.build(false).toUri(), HttpMethod.GET, null, Double.class);
		}
	
		private Double getCurrentCredits(Integer userid) {
			
			url = url + ":"+Integer.toString(port)+creditUrl;
			
			MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
			queryParams.add("userid", Integer.toString(userid));
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			builder.queryParams(queryParams);
			
			ResponseEntity<Double> responseEntity = restTemplate.exchange(builder.build(false).toUri(), HttpMethod.GET, null, Double.class);
			
			return responseEntity.getBody();
		}
}
