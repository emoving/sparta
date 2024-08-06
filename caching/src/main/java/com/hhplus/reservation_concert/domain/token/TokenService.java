package com.hhplus.reservation_concert.domain.token;

import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.global.exception.CustomException;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import com.hhplus.reservation_concert.infrastructure.token.TokenRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRedisRepositoryImpl tokenRepository;

    public Token validateActivate(String token) {
        if (!tokenRepository.hasActiveToken(token)) {
            throw new CustomException(ErrorCode.TOKEN_NOT_ACTIVE);
        }

        return Token.validate(token);
    }

    public Token issue(User user) {
        Long userId = user.getId();
        Token token = Token.from(userId);
        
        tokenRepository.saveWaitingToken(token.getToken());

        return token;
    }

    public void deleteToken(Token token) {
        tokenRepository.removeActiveToken(token.getToken());
    }
}
