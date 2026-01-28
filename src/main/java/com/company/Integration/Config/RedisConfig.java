package com.company.Integration.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 1. Create a "Smart" ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        // This line teaches Jackson how to handle LocalDateTime
        mapper.registerModule(new JavaTimeModule());
        // This line makes the date look like a String "2026-01-28"
        // instead of [2026, 1, 28]
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 2. Use the smart mapper in the Serializer
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

        // 1. Key is a simple String
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 2. Value is an Object (JSON) - This is crucial!
        redisTemplate.setValueSerializer(serializer);

        return redisTemplate;
    }
}
