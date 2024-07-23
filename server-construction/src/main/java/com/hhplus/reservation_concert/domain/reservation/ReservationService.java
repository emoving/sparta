package com.hhplus.reservation_concert.domain.reservation;

import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.reservation.payment.Payment;
import com.hhplus.reservation_concert.infrastructure.reservation.PaymentRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.reservation.ReservationRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepositoryImpl reservationRepository;
    private final PaymentRepositoryImpl paymentRepository;

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Reservation reserve(Long userId, Seat seat) {
        Reservation reservation = Reservation.from(userId, seat);

        return reservationRepository.save(reservation);
    }

    public Payment pay(Reservation reservation) {
        Payment payment = Payment.from(reservation);
        reservation.setStatusPayed();

        return paymentRepository.save(payment);
    }
}
