package service.calculator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CalculationService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${service.add.url}")
	private String addUrl;
	
	@Value("${service.add.port}")
	private String addPort;
	
	@Value("${service.add.additionUrl}")
	private String addUrlPath;
	
	@Value("${service.subtract.url}")
	private String subtractionUrl;
	
	@Value("${service.subtract.port}")
	private String subtractPort;
	
	@Value("${service.subtract.subtractionUrl}")
	private String subtractionUrlPath;
	
	@Value("${service.multiply.url}")
	private String mulitplicationUrl;
	
	@Value("${service.multiply.port}")
	private String multiplicationPort;
	
	@Value("${service.multiply.multiplicationUrl}")
	private String multiplicationUrlPath;
	
	@Value("${service.divide.url}")
	private String divisionUrl;
	
	@Value("${service.divide.port}")
	private String divisionPort;
	
	@Value("${service.divide.divisionUrl}")
	private String divisionUrlPath;
	
	private final Logger logger = LoggerFactory.getLogger(ExecutionService.class);
	
	public CalculationService() {
	}

	public Double performOperation(String operation, Double value1, Double value2) {

		try {

			String url = urlDecider(operation);

			MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
			queryParams.add("value1", Double.toString(value1));
			queryParams.add("value2", Double.toString(value2));
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			builder.queryParams(queryParams);
			
			ResponseEntity<Double> responseEntity = restTemplate.exchange(builder.build(false).toUri(), HttpMethod.GET, null, Double.class);

			return responseEntity.getBody();
		} 
		catch (ResourceAccessException e) {
			// TODO Auto-generated catch block
			logger.error("Unable to connect to the service. Please check if the service is running. Service name : "+operation+" service");
			e.printStackTrace();
			return null;
		}
	}

	private String urlDecider(String operation) {
		
		switch(operation) {
		
		case "add":
			return addUrl + ":"+addPort+addUrlPath;
			
		case "subtract":
			return subtractionUrl + ":" + subtractPort + subtractionUrlPath;
			
		case "multiply":
			return mulitplicationUrl + ":" + multiplicationPort + multiplicationUrlPath;
			
		case "divide":
			return divisionUrl + ":" + divisionPort + divisionUrlPath;
		
		}
		
		return null;
	}
	
}
