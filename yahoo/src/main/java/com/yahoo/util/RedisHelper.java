package com.yahoo.util;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.repository.jpa.StockJpaRepo;
import com.yahoo.service.StockService;

public class RedisHelper {

  @Autowired
  private StockJpaRepo stockJpaRepo;

  private RedisTemplate<String, String> redisTemplate;
  private ObjectMapper objectMapper;

  public RedisHelper(RedisConnectionFactory factory, ObjectMapper objectMapper) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    redisTemplate.setKeySerializer(RedisSerializer.string());
    redisTemplate.setValueSerializer(RedisSerializer.json());
    redisTemplate.afterPropertiesSet();
    this.objectMapper = objectMapper;
    this.redisTemplate = redisTemplate;
  }

  public List<String> getSymbols() {
    return redisTemplate.opsForList().range("STOCK-:LIST", 0, -1);
  }
}
