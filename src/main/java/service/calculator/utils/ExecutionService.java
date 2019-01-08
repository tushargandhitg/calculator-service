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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

import service.calculator.kafka.KafkaMessage;
import service.calculator.kafka.KafkaProducer;
import service.calculator.responses.ErrorResponse;
import service.calculator.responses.SuccessResponse;
import service.calculator.utils.exception.InvalidOperationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ExecutionService {

	private final Logger logger = LoggerFactory.getLogger(ExecutionService.class);

	@Autowired
	private CalculationService calculationService;

	@Autowired
	UtilityMethods utilityMethods;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private OperationCostRepository operationCostRepo;

	@Value("${service.user.url}")
	private String url;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Value("${service.user.port}")
	private int port;

	@Value("${service.user.user-details}")
	private String userDetailsUrl;

	@Value("${service.user.updateCredits}")
	private String updateCreditUrl;

	public String performExecution(String operation, Integer userid, Double value1, Double value2) {

		Gson json = new Gson();
		// get cost of operation

		Optional<OperationCost> findByIdResult = operationCostRepo.findById(operation);
		try {

			if (!findByIdResult.isPresent()) {
				throw new InvalidOperationException("Invalid operation : " + operation);
			}
		} catch (InvalidOperationException ioe) {
			logger.error("Invalid Operation detected : " + operation + ", userid : " + userid);
			ErrorResponse response = new ErrorResponse("Invalid Operation : " + operation);
			return json.toJson(response);
		}

		Double cost = findByIdResult.get().getCost();

		// get current credits
		User userDetails = getCurrentUserInfo(userid);
		Double currentCredits = userDetails.getCredits();

		// check for invalid user
		if (currentCredits == null) {
			ErrorResponse response = new ErrorResponse();
			response.setMessage("Operation not feasible. Invalid user detected.");
			response.setSuccessFlag(false);
			return json.toJson(response);
		}

		// check eligibility if feasible
		if (!isOperationFeasible(currentCredits, cost)) {
			logger.error("Credits too low for operation : " + operation + ", userid : " + userid);
			utilityMethods.logTransaction(userid, operation, TransactionStatus.FAILURE, currentCredits, cost);
			ErrorResponse response = new ErrorResponse();
			response.setMessage("Operation not feasible. Low credits detected");
			response.setSuccessFlag(false);

			return json.toJson(response);
		}

		// handling for infinite result condition
		Double result = evaluateOperationResult(operation, value1, value2);

		if (result == null) {

			logger.error("Internal Server Error : value1 = " + value1 + ", value2 = " + value2
					+ ", operation = " + operation + ", userid : " + userid);
			utilityMethods.logTransaction(userid, operation, TransactionStatus.FAILURE, currentCredits, cost);
			ErrorResponse response = new ErrorResponse();
			response.setMessage("Internal Server Error : value1 = " + value1 + ", value2 = " + value2
					+ ", operation = " + operation + ", userid : " + userid);
			response.setSuccessFlag(false);

			return json.toJson(response);
		}

		if (result.isInfinite()) {

			logger.error("Operation not feasible, invalid inputs : value1 = " + value1 + ", value2 = " + value2
					+ ", operation = " + operation + ", userid : " + userid);
			utilityMethods.logTransaction(userid, operation, TransactionStatus.FAILURE, currentCredits, cost);
			ErrorResponse response = new ErrorResponse();
			response.setMessage("Operation not feasible, invalid inputs : value1 = " + value1 + ", value2 = " + value2
					+ ", operation = " + operation + ", userid : " + userid);
			response.setSuccessFlag(false);

			return json.toJson(response);

		}

		// perform operation
		Double updatedUserCredits = currentCredits - cost;
		userDetails.setCredits(updatedUserCredits);

		// update db
		updateCurrentCredits(userid, updatedUserCredits);

		// log transaction
		utilityMethods.logTransaction(userid, operation, TransactionStatus.SUCCESS, updatedUserCredits, cost);

		// set/update cache redis
		updateCacheRedis(userid, userDetails);

		// return result

		SuccessResponse response = new SuccessResponse(result, "success", true);

		// prepare message for kafka topic and push it to the topic
		KafkaMessage message = new KafkaMessage(updatedUserCredits, cost, "Operation Successful",
				userDetails.getEmail(), operation);

		kafkaProducer.send(json.toJson(message));

		return json.toJson(response);
	}

	private boolean updateCacheRedis(Integer userid, User userDetails) {

		Gson json = new Gson();
		String jsonData = json.toJson(userDetails);
		redisTemplate.opsForValue().set("user_" + userid, jsonData);
		return true;
	}

	private Double evaluateOperationResult(String operation, Double value1, Double value2) {

		return calculationService.performOperation(operation, value1, value2);
	}

	private boolean isOperationFeasible(Double currentCredits, Double cost) {

		if (currentCredits < cost) {
			return false;
		}

		return true;
	}

	private void updateCurrentCredits(Integer userid, Double credits) {

		String finalUrl = url + ":" + Integer.toString(port) + updateCreditUrl;

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finalUrl);
		builder.queryParam("userid", userid);
		builder.queryParam("credits", credits);

		restTemplate.exchange(builder.build(false).toUri(), HttpMethod.GET, null, Boolean.class);
	}

	private User getCurrentUserInfo(Integer userid) {

		// check data from cache
		String userDetails = redisTemplate.opsForValue().get("user_" + userid);

		// if no data in cache is found, read from user-service
		if (StringUtils.isEmpty(userDetails)) {
			userDetails = getUserDetailsFromDB(userid);
		}

		Gson gson = new Gson();
		User user = gson.fromJson(userDetails, User.class);
		return user;

	}

	private String getUserDetailsFromDB(Integer userid) {
		String finalUrl = url + ":" + Integer.toString(port) + userDetailsUrl;

		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("userid", Integer.toString(userid));

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finalUrl);
		builder.queryParams(queryParams);
		ResponseEntity<String> responseEntity = null;

		try {
			responseEntity = restTemplate.exchange(builder.build(false).toUri(), HttpMethod.GET, null, String.class);
		} catch (RestClientException e) {

			logger.error("Response Failure from user-detail service", e);
		}
		return responseEntity.getBody();
	}
}
