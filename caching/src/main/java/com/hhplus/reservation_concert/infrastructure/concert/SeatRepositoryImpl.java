package com.hhplus.reservation_concert.infrastructure.concert;

import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepositoryImpl extends JpaRepository<Seat, Long> {
    List<Seat> findAllByPerformance(Performance performance);
}
