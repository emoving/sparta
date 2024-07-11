package com.hhplus.reservation_concert.domain.reservation.payment;

import com.hhplus.reservation_concert.infrastructure.reservation.PaymentRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepositoryImpl paymentRepository;

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }
}
