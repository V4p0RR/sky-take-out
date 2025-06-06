package com.sky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.slf4j.Slf4j;

//配置redis
@Slf4j
@Configuration
public class RedisConfiguration {
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Bean
  RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    log.info("开始创建redis模版对象");
    RedisTemplate redisTemplate = new RedisTemplate();
    // 设置redis的连接对象工厂
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    // 设置redis的key序列化器
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    log.info("redis序列化完成");
    return redisTemplate;
  }
}
