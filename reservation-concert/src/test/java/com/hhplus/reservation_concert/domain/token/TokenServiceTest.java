package com.hhplus.reservation_concert.domain.token;

import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.infrastructure.token.TokenRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private TokenRepositoryImpl tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    private User user;
    private Token token;

    @BeforeEach
    void setUp() {
        user = new User(1L, "이동주", 0);
        token = Token.builder().id(1L).status(TokenStatus.WAITING).userId(1L).build();
    }

    @Test
    void getTokenAlreadyExits() {
        when(tokenRepository.findByUserId(user.getId())).thenReturn(Optional.of(token));

        Token getToken = tokenService.getOrSave(user);

        assertThat(getToken.getUserId()).isEqualTo(user.getId());
        assertThat(getToken.getStatus()).isEqualTo(token.getStatus());
    }

    @Test
    void getNewToken() {
        when(tokenRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(tokenRepository.save(any())).thenReturn(token);

        Token savedToken = tokenService.getOrSave(user);

        assertThat(savedToken.getUserId()).isEqualTo(user.getId());
        assertThat(savedToken.getStatus()).isEqualTo(token.getStatus());
    }
}