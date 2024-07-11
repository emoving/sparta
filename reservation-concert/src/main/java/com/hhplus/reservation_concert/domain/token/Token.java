package com.hhplus.reservation_concert.domain.token;

import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.global.error.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    @Builder.Default
    private String token = UUID.randomUUID().toString();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TokenStatus status = TokenStatus.WAITING;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime recentUsedAt;

    public void setActive() {
        this.status = TokenStatus.ACTIVE;
        this.recentUsedAt = LocalDateTime.now();
    }

    public void validateActive() {
        if (this.status != TokenStatus.ACTIVE) {
            throw new CustomException(ErrorCode.TOKEN_NOT_ACTIVE);
        }

        this.recentUsedAt = LocalDateTime.now();
    }
}
