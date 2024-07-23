package com.hhplus.reservation_concert.domain.user;

import com.hhplus.reservation_concert.domain.user.point.PointHistory;
import com.hhplus.reservation_concert.domain.user.point.TransactionType;
import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.global.error.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer point;

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
