package com.hhplus.reservation_concert.domain.reservation;

import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.reservation.payment.Payment;
import com.hhplus.reservation_concert.global.exception.CustomException;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import com.hhplus.reservation_concert.infrastructure.reservation.PaymentRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.reservation.ReservationRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final RedissonClient redissonClient;
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

    public Reservation reserveSeatWithDistributedLock(Long userId, Seat seat) {
        RLock lock = redissonClient.getLock("seatLock: " + seat.getId());

        try {
            if (lock.tryLock(5, 2, TimeUnit.SECONDS)) {
                seat.reserve();
            } else {
                throw new CustomException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }
        } catch (InterruptedException e) {
            throw new CustomException(ErrorCode.LOCK_INTERRUPTED);
        } finally {
            lock.unlock();
        }

        return reservationRepository.save(Reservation.from(userId, seat));
    }

    public Payment pay(Reservation reservation) {
        reservation.setStatusPayed();

        return paymentRepository.save(Payment.from(reservation));
    }
}
