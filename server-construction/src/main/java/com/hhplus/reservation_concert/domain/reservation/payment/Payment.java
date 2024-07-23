package com.hhplus.reservation_concert.domain.reservation.payment;

import com.hhplus.reservation_concert.domain.reservation.Reservation;
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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reservation reservation;

    private Integer price;

    @CreatedDate
    private LocalDateTime createdAt;

    private Payment(Reservation reservation, Integer price) {
        this.reservation = reservation;
        this.price = price;
    }

    public static Payment from(Reservation reservation) {
        return new Payment(reservation, reservation.getPrice());
    }
}
