package com.hhplus.reservation_concert.domain.token;

import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.global.error.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Table(indexes = @Index(name = "token_status", columnList = "status"))
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime usedAt;

    private Token(Long userId) {
        this.userId = userId;
        this.token = String.valueOf(UUID.randomUUID());
        this.status = TokenStatus.WAITING;
    }

    public static Token from(Long userId) {
        return new Token(userId);
    }

    public void setActive() {
        this.status = TokenStatus.ACTIVE;
        this.usedAt = LocalDateTime.now();
    }

    public void validateActive() {
        if (this.status != TokenStatus.ACTIVE) {
            throw new CustomException(ErrorCode.TOKEN_NOT_ACTIVE);
        }

        this.usedAt = LocalDateTime.now();
    }
}
