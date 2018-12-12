package service.calculator.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataFetcher {

	@Autowired
	private RestTemplate restTemplate;
	
	
	
}
