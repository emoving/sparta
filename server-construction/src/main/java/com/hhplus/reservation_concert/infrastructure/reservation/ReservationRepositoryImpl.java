package com.hhplus.reservation_concert.infrastructure.reservation;

import com.hhplus.reservation_concert.domain.reservation.Reservation;
import com.hhplus.reservation_concert.domain.reservation.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepositoryImpl extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllBySeatId(Long seatId);

    List<Reservation> findByStatusAndExpiredAtAfter(ReservationStatus status, LocalDateTime expiredDateTime);
}