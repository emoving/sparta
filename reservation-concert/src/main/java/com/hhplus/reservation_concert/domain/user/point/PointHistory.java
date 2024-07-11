package com.hhplus.reservation_concert.domain.user.point;

import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.global.error.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Integer point;

    private TransactionType type;

    @CreatedDate
    private LocalDateTime createdAt;

    public PointHistory (User user, Integer point, TransactionType type) {
        this.user = user;
        this.point = point;
        this.type = type;
    }

    public static PointHistory charge(User user, Integer point) {
        if (point <= 0) {
            throw new CustomException(ErrorCode.POINT_BELOW_ZERO);
        }

        return new PointHistory(user, point, TransactionType.CHARGE);
    }
}
