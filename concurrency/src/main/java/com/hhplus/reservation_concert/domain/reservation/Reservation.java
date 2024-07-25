package com.hhplus.reservation_concert.domain.reservation;

import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.reservation.payment.Payment;
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
@Table(indexes = {@Index(name = "idx_reservation_status_expired_at", columnList = "status, expiredAt")})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long seatId;

    private Integer price;

    @OneToOne(mappedBy = "reservation")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt != null) {
            this.expiredAt = this.createdAt.plusMinutes(10);
        }
    }

    public Reservation(Long userId, Long seatId, Integer price, ReservationStatus status) {
        this.userId = userId;
        this.seatId = seatId;
        this.price = price;
        this.status = status;
    }

    public static Reservation from(Long userId, Seat seat) {
        return new Reservation(userId, seat.getId(), seat.getPrice(), ReservationStatus.reserved);
    }

    public void setStatusExpired() {
        this.status = ReservationStatus.expired;
    }

    public void setStatusPayed() {
        this.status = ReservationStatus.payed;
    }
}
