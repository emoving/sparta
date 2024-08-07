package com.hhplus.reservation_concert.infrastructure.concert;

import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatRepositoryImpl extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Seat s where s.id = :id")
    Optional<Seat> findByIdWithPessimisticLock(Long id);

    List<Seat> findAllByPerformance(Performance performance);
}
