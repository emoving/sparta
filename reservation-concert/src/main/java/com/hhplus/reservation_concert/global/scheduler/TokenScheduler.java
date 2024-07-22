package com.hhplus.reservation_concert.global.scheduler;

import com.hhplus.reservation_concert.domain.token.Token;
import com.hhplus.reservation_concert.domain.token.TokenStatus;
import com.hhplus.reservation_concert.infrastructure.token.TokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final TokenRepositoryImpl tokenRepository;

    @Scheduled(fixedRate = 30 * 1000)
    public void updateTokenStatus() {
        deleteExpiredTokens();
        activateWaitingTokens();
    }

    @Transactional
    public void deleteExpiredTokens() {
        long EXPIRE_MINUTE = 30;
        List<Token> activeTokens = tokenRepository.findByStatus(TokenStatus.ACTIVE);

        for (Token token : activeTokens) {
            if (token.getUsedAt().isBefore(LocalDateTime.now().minusMinutes(EXPIRE_MINUTE))) {
                tokenRepository.delete(token);
            }
        }
    }

    @Transactional
    public void activateWaitingTokens() {
        long LIMIT_USER_COUNT = 10;
        long activeTokenCount = tokenRepository.countByStatus(TokenStatus.ACTIVE);

        if (activeTokenCount < LIMIT_USER_COUNT) {
            List<Token> waitingTokens = tokenRepository.findAllByStatusOrderById(TokenStatus.WAITING);

            for (Token token : waitingTokens) {
                token.setActive();
                tokenRepository.save(token);
                activeTokenCount++;

                if (activeTokenCount >= LIMIT_USER_COUNT) {
                    break;
                }
            }
        }
    }
}
