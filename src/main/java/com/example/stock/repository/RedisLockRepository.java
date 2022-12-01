package com.example.stock.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean lock(Long key) {
        return Boolean.TRUE.equals(
                redisTemplate
                .opsForValue()  //String을 Serialize/Deserialize 해주는 인터페이스
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3000)));   //key가 없으면 저장
    }

    public boolean unlock(Long key) {
        return Boolean.TRUE.equals(redisTemplate.delete(generateKey(key)));
    }

    private String generateKey(Long key) {
        return key.toString();
    }
}
