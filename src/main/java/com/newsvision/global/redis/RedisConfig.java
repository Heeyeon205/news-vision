package com.newsvision.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration

public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        // Redis 동작 방식 Key - Value
        RedisTemplate<String, String> template = new RedisTemplate<>();
        // Redis 에 저장될 키를 문자열(String)로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        // Redis 에 저장될 값을 문자열(String)로 직렬화
        template.setValueSerializer(new StringRedisSerializer());
        // Redis 연결 설정 주입
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
