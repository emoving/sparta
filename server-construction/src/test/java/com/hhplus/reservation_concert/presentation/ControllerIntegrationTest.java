package com.hhplus.reservation_concert.presentation;

import com.hhplus.reservation_concert.domain.concert.Concert;
import com.hhplus.reservation_concert.domain.token.Token;
import com.hhplus.reservation_concert.domain.token.TokenStatus;
import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.infrastructure.concert.ConcertRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.token.TokenRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.user.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private ConcertRepositoryImpl concertRepository;

    @Autowired
    private TokenRepositoryImpl tokenRepository;

    @BeforeEach
    void setUp() {
        User user = new User(1L, "이동주", 0);
        userRepository.save(user);
        concertRepository.save(new Concert(1L, "흠뻑쇼", "싸이"));
    }

    @Test
    void issue() throws Exception {
        mockMvc.perform(post("/tokens/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-TOKEN"));
    }

    @Test
    void notExistToken() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Token", "test");

        mockMvc.perform(get("/concerts").headers(httpHeaders))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.TOKEN_NOT_FOUND.getMessage()));
    }

    @Test
    void haveWaitingToken() throws Exception {
        Token savedToken = tokenRepository.save(Token.builder().userId(1L).build());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Token", savedToken.getToken());

        mockMvc.perform(get("/concerts").headers(httpHeaders))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value(ErrorCode.TOKEN_NOT_ACTIVE.getMessage()));
    }

    @Test
    void haveActiveToken() throws Exception {
        Token savedToken = tokenRepository.save(Token.builder().userId(1L).status(TokenStatus.ACTIVE).build());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Token", savedToken.getToken());

        mockMvc.perform(get("/concerts").headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concertResponses.length()").value(1));
    }
}