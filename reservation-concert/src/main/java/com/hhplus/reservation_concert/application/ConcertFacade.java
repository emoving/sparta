package com.hhplus.reservation_concert.application;

import com.hhplus.reservation_concert.domain.concert.Concert;
import com.hhplus.reservation_concert.domain.concert.ConcertService;
import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    public List<Concert> getConcerts() {
        return concertService.getConcerts();
    }

    public List<Performance> getPerformances(Long concertId) {
        Concert concert = concertService.getConcert(concertId);

        return concertService.getPerformances(concert);
    }

    public List<Seat> getSeats(Long performanceId) {
        Performance performance = concertService.getPerformance(performanceId);

        return concertService.getSeats(performance);
    }
}
