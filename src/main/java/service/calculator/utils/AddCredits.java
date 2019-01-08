package service.calculator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.google.gson.Gson;

import service.calculator.controller.CalculatorController;
import service.calculator.kafka.KafkaMessage;
import service.calculator.kafka.KafkaProducer;
import service.calculator.responses.AddCreditResponse;
import service.calculator.responses.ErrorResponse;

@Service
public class AddCredits {

	private final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UtilityMethods utilityMethods;
	
	@Autowired
	private KafkaProducer kafkaProducer;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Value("${service.user.url}")
	private String url;
	
	@Value("${service.user.addCredits}")
	private String addCreditsUrl;
	
	@Value("${service.user.port}")
	private int port;
	
	public String addCredits(Integer userid, Double credits) {
		
		Gson json = new Gson();
		if( credits < 0 ) {
			logger.error("invalid user operation detected.");
			
			ErrorResponse response = new ErrorResponse();
			response.setMessage("Credits can't be negative.");
			response.setSuccessFlag(false);
			return json.toJson(response);
		}

		
		String finalUrl = url + ":"+Integer.toString(port)+addCreditsUrl;
		
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("userid", Integer.toString(userid));
		queryParams.add("credits", Double.toString(credits));
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finalUrl);
		builder.queryParams(queryParams);
		
		ResponseEntity<String> exchange = restTemplate.exchange(builder.build(false).toUri(), HttpMethod.GET, null, String.class);
		
		User userDetails = json.fromJson(exchange.getBody(), User.class);

		if( userDetails.getUserid() == null ) {
			ErrorResponse response = new ErrorResponse();
			response.setMessage("Operation not feasible. Invalid user detected.");
			response.setSuccessFlag(false);
			return json.toJson(response);
		}
		
		// update new result set in cache
		redisTemplate.opsForValue().set("user_"+userDetails.getUserid(), json.toJson(userDetails));
		
		// log transaction in db
		utilityMethods.logTransaction(userid, "update", TransactionStatus.SUCCESS, userDetails.getCredits(), 0.0);
		
		// push the data in kafka for notification to user
		KafkaMessage message = new KafkaMessage(userDetails.getCredits(), 0.0 , "Updation Operation Successful", 
				userDetails.getEmail(), "update");
		
		kafkaProducer.send(json.toJson(message));
		
		AddCreditResponse response = new AddCreditResponse(true, userDetails.getCredits(), userid);
		return json.toJson(response);
	}
}
