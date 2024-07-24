package com.hhplus.reservation_concert.domain.reservation;

import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.reservation.payment.Payment;
import com.hhplus.reservation_concert.global.exception.CustomException;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import com.hhplus.reservation_concert.infrastructure.reservation.PaymentRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.reservation.ReservationRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepositoryImpl reservationRepository;
    private final PaymentRepositoryImpl paymentRepository;

    @Transactional(readOnly = true)
    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Reservation reserveSeat(Long userId, Seat seat) {
        seat.reserve();

        return reservationRepository.save(Reservation.from(userId, seat));
    }

    public Reservation reserveSeatWithOptimisticLock(Long userId, Seat seat) {
        try {
            seat.reserve();
        } catch (OptimisticLockingFailureException e) {
            throw new CustomException(ErrorCode.SEAT_RESERVED, e);
        }

        return reservationRepository.save(Reservation.from(userId, seat));
    }

    public Payment pay(Reservation reservation) {
        reservation.setStatusPayed();

        return paymentRepository.save(Payment.from(reservation));
    }
}
