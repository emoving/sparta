package com.hhplus.reservation_concert.domain.concert;

import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.global.exception.CustomException;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import com.hhplus.reservation_concert.infrastructure.concert.ConcertRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.concert.PerformanceRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.concert.SeatRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConcertService {

    private final ConcertRepositoryImpl concertRepository;
    private final PerformanceRepositoryImpl performanceRepository;
    private final SeatRepositoryImpl seatRepository;

    @Cacheable("concert")
    public Concert getConcert(Long id) {
        return concertRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CONCERT_NOT_FOUND));
    }

    @Cacheable(value = "concert", key = "#all")
    public List<Concert> getConcerts() {
        return concertRepository.findAll();
    }

    @CacheEvict(value = "concert", key = "#all")
    public void saveConcert(Concert concert) {
        concertRepository.save(concert);
    }

    @Cacheable("performance")
    public Performance getPerformance(Long id) {
        return performanceRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));
    }

    @Cacheable(value = "performance", key = "#concert.id")
    public List<Performance> getPerformances(Concert concert) {
        return performanceRepository.findAllByConcert(concert);
    }

    @CacheEvict(value = "performance", key = "#performance.concert.id")
    public void savePerformance(Performance performance) {
        performanceRepository.save(performance);
    }

    public Seat getSeat(Long id) {
        return seatRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));
    }

    public List<Seat> getSeats(Performance performance) {
        return seatRepository.findAllByPerformance(performance);
    }

    public void saveSeat(Seat seat) {
        seatRepository.save(seat);
    }
}
