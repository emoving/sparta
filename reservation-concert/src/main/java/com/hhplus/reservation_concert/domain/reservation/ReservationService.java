package com.hhplus.reservation_concert.domain.reservation;

import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.infrastructure.reservation.ReservationRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepositoryImpl reservationRepository;

    public Reservation get(Long id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation reserve(Long userId, Seat seat) {
        List<Reservation> reservations = reservationRepository.findAllBySeatId(seat.getId());

        for (Reservation reservation : reservations) {
            reservation.validateAvailableSeatReserve();
        }

        return reservationRepository.save(new Reservation(userId, seat.getId(), seat.getPrice()));
    }
}
