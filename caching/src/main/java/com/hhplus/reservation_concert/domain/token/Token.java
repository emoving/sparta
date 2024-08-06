package com.hhplus.reservation_concert.domain.token;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
public class Token {

    private String token;
    private Long userId;

    private Token(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public static Token from(Long userId) {
        String token = UUID.randomUUID().toString() + ":" + userId;

        return new Token(token, userId);
    }

    public static Token validate(String xToken) {
        return new Token(xToken, Long.parseLong(xToken.split(":")[1]));
    }
}
