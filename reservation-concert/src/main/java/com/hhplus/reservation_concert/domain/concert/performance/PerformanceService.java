package com.hhplus.reservation_concert.domain.concert.performance;

import com.hhplus.reservation_concert.domain.concert.Concert;
import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.global.error.exception.CustomException;
import com.hhplus.reservation_concert.infrastructure.concert.PerformanceRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepositoryImpl performanceRepository;

    public Performance getPerformance(Long id) {
        return performanceRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));
    }

    public List<Performance> getPerformances(Concert concert) {
        return performanceRepository.findAllByConcert(concert);
    }

    public void savePerformance(Performance performance) {
        performanceRepository.save(performance);
    }
}
