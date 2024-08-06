package com.hhplus.reservation_concert.domain.user;

import com.hhplus.reservation_concert.domain.user.point.PointHistory;
import com.hhplus.reservation_concert.domain.user.point.TransactionType;
import com.hhplus.reservation_concert.global.exception.CustomException;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer point;

    private String token;

    public PointHistory charge(Integer point) {
        if (point <= 0) {
            throw new CustomException(ErrorCode.POINT_CHARGE_BELOW_ZERO);
        }

        this.point += point;
        return new PointHistory(this, point, TransactionType.CHARGE);
    }

    public PointHistory use(Integer point) {
        if (this.point < point) {
            throw new CustomException(ErrorCode.POINT_USE_LACK);
        }

        this.point -= point;
        return new PointHistory(this, point, TransactionType.USE);
    }
}
