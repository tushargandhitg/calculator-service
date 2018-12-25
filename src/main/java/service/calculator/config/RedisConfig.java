package service.calculator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private String redisPort;
	
	@Bean
	   public RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
		return connectionFactory;
	   }
	
	@Bean
	public RedisTemplate<String, String> redisTemplate() {
	    RedisTemplate<String, String> template = new RedisTemplate<>();
	    template.setConnectionFactory(redisConnectionFactory());
	    template.setKeySerializer(new StringRedisSerializer());
	    template.setValueSerializer(new StringRedisSerializer());
//	    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//	    template.setValueSerializer(new StringRedisSerializer());
	    return template;
	}
}
