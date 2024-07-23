package com.hhplus.reservation_concert.infrastructure.token;

import com.hhplus.reservation_concert.domain.token.Token;
import com.hhplus.reservation_concert.domain.token.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepositoryImpl extends JpaRepository<Token, Long> {

    Optional<Token> findByUserId(Long userId);

    Optional<Token> findByToken(String token);

    long countByStatus(TokenStatus status);

    List<Token> findByStatus(TokenStatus status);

    List<Token> findAllByStatusOrderById(TokenStatus status);
}
