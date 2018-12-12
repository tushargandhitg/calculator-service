package service.calculator.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
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
	
	@Value("${service.multiply.subtractionUrl}")
	private String multiplicationUrlPath;
	
	@Value("${service.divide.url}")
	private String divisionUrl;
	
	@Value("${service.divide.port}")
	private String divisionPort;
	
	@Value("${service.divide.divisionUrl}")
	private String divisionUrlPath;
	
	
	public CalculationService() {
	}

	public double performOperation(String operation, Double value1, Double value2) {

		try {

			String url = urlDecider(operation);

			MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
			queryParams.add("value1", Double.toString(value1));
			queryParams.add("value2", Double.toString(value2));
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			builder.queryParams(queryParams);
			
			ResponseEntity<Double> responseEntity = restTemplate.exchange(builder.build(false).toUri(), HttpMethod.GET, null, Double.class);

			return responseEntity.getBody();
		} catch (HttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
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
