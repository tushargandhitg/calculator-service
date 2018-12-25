package service.calculator.utils;

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

import service.calculator.kafka.KafkaMessage;
import service.calculator.kafka.KafkaProducer;
import service.calculator.responses.AddCreditResponse;

@Service
public class AddCredits {

	@Autowired
	private RestTemplate restTemplate;

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
		
		String finalUrl = url + ":"+Integer.toString(port)+addCreditsUrl;
		
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("userid", Integer.toString(userid));
		queryParams.add("credits", Double.toString(credits));
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finalUrl);
		builder.queryParams(queryParams);
		
		ResponseEntity<String> exchange = restTemplate.exchange(builder.build(false).toUri(), HttpMethod.GET, null, String.class);
		
		Gson json = new Gson();
		User userDetails = json.fromJson(exchange.getBody(), User.class);

		// update new result set in cache
		redisTemplate.opsForValue().set("user_"+userDetails.getUserid(), json.toJson(userDetails));
		
		// push the data in kafka for notification to user
		KafkaMessage message = new KafkaMessage(userDetails.getCredits(), 0.0 , "Updation Operation Successful", 
				userDetails.getEmail(), "update");
		
		kafkaProducer.send(json.toJson(message));
		
		AddCreditResponse response = new AddCreditResponse(true, userDetails.getCredits(), userid);
		return json.toJson(response);
	}
}
