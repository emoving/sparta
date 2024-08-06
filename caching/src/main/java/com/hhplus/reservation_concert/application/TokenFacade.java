package com.hhplus.reservation_concert.application;

import com.hhplus.reservation_concert.domain.token.Token;
import com.hhplus.reservation_concert.domain.token.TokenService;
import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenFacade {

    private final TokenService tokenService;
    private final UserService userService;

    public Token issue(Long userId) {
        User user = userService.getUser(userId);

        return tokenService.issue(user);
    }

    public Token validate(String xToken) {
        return tokenService.validateActivate(xToken);
    }
}
