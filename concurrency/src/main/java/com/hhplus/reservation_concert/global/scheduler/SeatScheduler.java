package com.hhplus.reservation_concert.global.scheduler;

import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.reservation.Reservation;
import com.hhplus.reservation_concert.domain.reservation.ReservationStatus;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import com.hhplus.reservation_concert.global.exception.CustomException;
import com.hhplus.reservation_concert.infrastructure.concert.SeatRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.reservation.ReservationRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatScheduler {

    private final SeatRepositoryImpl seatRepository;
    private final ReservationRepositoryImpl reservationRepository;

    @Scheduled(fixedRate = 30 * 1000)
    public void updateReservationAndSeatStatus() {
        List<Reservation> reservations = reservationRepository.findByStatusAndExpiredAtAfter(ReservationStatus.reserved, LocalDateTime.now());

        for (Reservation reservation : reservations) {
            Seat seat = seatRepository.findById(reservation.getSeatId()).orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));

            seat.setStatusEmpty();
            reservation.setStatusExpired();
        }
    }
}
