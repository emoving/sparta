package com.hhplus.reservation_concert.domain.token;

import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.global.error.exception.CustomException;
import com.hhplus.reservation_concert.infrastructure.token.TokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepositoryImpl tokenRepository;

    public Token getToken(Long id) {
        return tokenRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));
    }

    public Token getToken(String token) {
        if (token == null) {
            throw new CustomException(ErrorCode.TOKEN_EMPTY);
        }

        return tokenRepository.findByToken(token).orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));
    }

    public Token getOrSaveToken(User user) {
        Long userId = user.getId();
        Optional<Token> byUserId = tokenRepository.findByUserId(userId);

        return byUserId.orElseGet(() -> tokenRepository.save(Token.from(userId)));
    }

    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }
}
