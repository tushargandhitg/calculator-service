package service.calculator.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AddCredits {

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${service.user.url}")
	private String url;
	
	@Value("${service.user.addCredits}")
	private String addCreditsUrl;
	
	@Value("${service.user.port}")
	private int port;
	
	public void addCredits(Integer userid, Double credits) {
		
		url = url + ":"+Integer.toString(port)+addCreditsUrl;
		
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("userid", Integer.toString(userid));
		queryParams.add("credits", Double.toString(credits));
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		builder.queryParams(queryParams);
		
		restTemplate.exchange(builder.build(false).toUri(), HttpMethod.GET, null, Double.class);
	}
	
}
