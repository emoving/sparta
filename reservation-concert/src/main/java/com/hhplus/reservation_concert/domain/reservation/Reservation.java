package com.hhplus.reservation_concert.domain.reservation;

import com.hhplus.reservation_concert.domain.reservation.payment.Payment;
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
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long seatId;

    private Integer price;

    @OneToOne(mappedBy = "reservation")
    private Payment payment;

    @CreatedDate
    private LocalDateTime createdAt;

    public Reservation(Long userId, Long seatId, Integer price) {
        this.userId = userId;
        this.seatId = seatId;
        this.price = price;
    }

    public void validateAvailableSeatReserve() {
        if (this.payment != null) {
            throw new CustomException(ErrorCode.SEAT_ALREADY_RESERVED);
        }

        if (LocalDateTime.now().isBefore(this.createdAt.plusMinutes(5))) {
            throw new CustomException(ErrorCode.SEAT_TEMPORARY_RESERVED);
        }
    }
}
