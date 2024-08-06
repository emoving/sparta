package com.hhplus.reservation_concert.infrastructure.token;

import com.hhplus.reservation_concert.domain.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class TokenRedisRepositoryImpl implements TokenRepository {

    private static final String WAITING_KEY = "waiting_tokens";
    private static final String ACTIVE_KEY = "active_tokens";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveWaitingToken(String token) {
        redisTemplate.opsForZSet().add(WAITING_KEY, token, System.currentTimeMillis());
    }

    @Override
    public Set<String> getTopWaitingTokens(int count) {
        return redisTemplate.opsForZSet().range(WAITING_KEY, 0, count - 1);
    }

    @Override
    public void removeTopWaitingTokens(int count) {
        redisTemplate.opsForZSet().removeRange(WAITING_KEY, 0, count - 1);
    }

    @Override
    public void saveActiveToken(String token) {
        long expiryTime = Instant.now().plusSeconds(60 * 60).toEpochMilli();

        redisTemplate.opsForHash().put(ACTIVE_KEY, token, expiryTime);
    }

    @Override
    public Long getActiveTokenCount() {
        return redisTemplate.opsForHash().size(ACTIVE_KEY);
    }

    @Override
    public Map<Object, Object> getActiveTokens() {
        return redisTemplate.opsForHash().entries(ACTIVE_KEY);
    }

    @Override
    public boolean hasActiveToken(String token) {
        return redisTemplate.opsForHash().hasKey(ACTIVE_KEY, token);
    }

    @Override
    public void removeActiveToken(String token) {
        redisTemplate.opsForHash().delete(ACTIVE_KEY, token);
    }
}
