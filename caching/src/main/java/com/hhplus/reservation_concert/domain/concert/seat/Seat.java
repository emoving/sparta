package com.hhplus.reservation_concert.domain.concert.seat;

import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import com.hhplus.reservation_concert.global.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_seat_performance", columnList = "performance_id"))
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Performance performance;

    private Integer number;

    private Integer price;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    public void setStatusEmpty() {
        this.status = SeatStatus.empty;
    }

    public void reserve() {
        if (this.status == SeatStatus.reserved) {
            throw new CustomException(ErrorCode.SEAT_RESERVED);
        }

        this.status = SeatStatus.reserved;
    }
}
