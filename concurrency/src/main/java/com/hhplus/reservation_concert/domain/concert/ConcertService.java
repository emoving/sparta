package com.hhplus.reservation_concert.domain.concert;

import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import com.hhplus.reservation_concert.global.exception.CustomException;
import com.hhplus.reservation_concert.infrastructure.concert.ConcertRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.concert.PerformanceRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.concert.SeatRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepositoryImpl concertRepository;
    private final PerformanceRepositoryImpl performanceRepository;
    private final SeatRepositoryImpl seatRepository;

    public Concert getConcert(Long id) {
        return concertRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CONCERT_NOT_FOUND));
    }

    public List<Concert> getConcerts() {
        return concertRepository.findAll();
    }

    public void saveConcert(Concert concert) {
        concertRepository.save(concert);
    }

    public Performance getPerformance(Long id) {
        return performanceRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_NOT_FOUND));
    }

    public List<Performance> getPerformances(Concert concert) {
        return performanceRepository.findAllByConcert(concert);
    }

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
