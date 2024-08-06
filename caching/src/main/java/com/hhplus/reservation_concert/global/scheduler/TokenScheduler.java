package com.hhplus.reservation_concert.global.scheduler;

import com.hhplus.reservation_concert.infrastructure.token.TokenRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private static final int LIMIT_USER_COUNT = 30;

    private final TokenRedisRepositoryImpl tokenRepository;

    @Scheduled(fixedRate = 30 * 1000)
    public void updateTokens() {
        expireTokens();
        activateTokens();
    }

    @Transactional
    public void expireTokens() {
        Map<Object, Object> activeTokens = tokenRepository.getActiveTokens();

        activeTokens.forEach((token, expireTime) -> {
            if ((Long) expireTime < Instant.now().toEpochMilli()) {
                tokenRepository.removeActiveToken((String) token);
            }
        });
    }

    @Transactional
    public void activateTokens() {
        int activateCount = (int) (LIMIT_USER_COUNT - tokenRepository.getActiveTokenCount());
        Set<String> waitingTokens = tokenRepository.getTopWaitingTokens(activateCount);
        tokenRepository.removeTopWaitingTokens(activateCount);

        for (String token: waitingTokens) {
            tokenRepository.saveActiveToken(token);
        }
    }
}
