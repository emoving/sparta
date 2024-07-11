package com.hhplus.reservation_concert.infrastructure.reservation;

import com.hhplus.reservation_concert.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepositoryImpl extends JpaRepository<Reservation, Long> {

//    @Query("SELECT r FROM Reservation r LEFT JOIN Payment p ON r.id = p.reservationId WHERE r.seatId = :seatId")
//    Optional<Reservation> findReservationWithPaymentBySeatId(@Param("seatId") Long seatId);

    List<Reservation> findAllBySeatId(Long seatId);
}