package com.hhplus.reservation_concert.infrastructure.concert;

import com.hhplus.reservation_concert.domain.concert.Concert;
import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceRepositoryImpl extends JpaRepository<Performance, Long> {

    List<Performance> findAllByConcert(Concert concert);
}
