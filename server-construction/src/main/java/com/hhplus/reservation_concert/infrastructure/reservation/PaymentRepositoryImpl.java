package com.hhplus.reservation_concert.infrastructure.reservation;

import com.hhplus.reservation_concert.domain.reservation.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositoryImpl extends JpaRepository<Payment, Long> {
}
