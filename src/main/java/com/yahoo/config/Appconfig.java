package com.yahoo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yahoo.util.RedisHelper;
import com.yahoo.util.YHRestClient;

@Configuration
public class Appconfig {
  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  YHRestClient yhRestClient() {
    return new YHRestClient();
  }

  @Bean
  ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }

  @Bean
  RedisHelper redisHelper(RedisConnectionFactory factory,
      ObjectMapper objectMapper) {
    return new RedisHelper(factory, objectMapper);
  }

}
