import org.springframework.kafka.core.KafkaTemplate;

public class KafkaHandler {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	
	public void send(String topic, String payload) {
		kafkaTemplate.send(topic, payload);
	}
	
}
