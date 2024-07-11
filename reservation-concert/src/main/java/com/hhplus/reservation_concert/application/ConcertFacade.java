package com.hhplus.reservation_concert.application;

import com.hhplus.reservation_concert.domain.concert.Concert;
import com.hhplus.reservation_concert.domain.concert.ConcertService;
import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.domain.concert.performance.PerformanceService;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.concert.seat.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private final PerformanceService performanceService;
    private final SeatService seatService;

    public List<Concert> getConcerts() {
        return concertService.getConcerts();
    }

    public List<Performance> getPerformances(Long concertId) {
        Concert concert = concertService.getConcert(concertId);

        return performanceService.getPerformances(concert);
    }

    public List<Seat> getSeats(Long performanceId) {
        Performance performance = performanceService.getPerformance(performanceId);

        return seatService.getSeats(performance);
    }
}
