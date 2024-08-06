package com.hhplus.reservation_concert.presentation.concert;

import com.hhplus.reservation_concert.application.ConcertFacade;
import com.hhplus.reservation_concert.domain.concert.Concert;
import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "콘서트 API", description = "콘서트, 공연, 좌석 조회, X-TOKEN 필요")
@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    @Operation(summary = "콘서트 목록 조회")
    @GetMapping
    public ResponseEntity<ConcertDto.ConcertsResponse> getConcerts() {
        List<Concert> concerts = concertFacade.getConcerts();

        return ResponseEntity.ok(ConcertDto.ConcertsResponse.from(concerts));
    }

    @Operation(summary = "해당 콘서트의 공연 목록 조회")
    @GetMapping("/{concertId}")
    public ResponseEntity<ConcertDto.PerformancesResponse> getPerformances(@PathVariable Long concertId) {
        List<Performance> performances = concertFacade.getPerformances(concertId);

        return ResponseEntity.ok(ConcertDto.PerformancesResponse.from(performances));
    }

    @Operation(summary = "해당 공연의 좌석 목록 조회")
    @GetMapping("/performances/{performanceId}/seats")
    public ResponseEntity<ConcertDto.SeatsResponse> getSeats(@PathVariable Long performanceId) {
        List<Seat> seats = concertFacade.getSeats(performanceId);

        return ResponseEntity.ok(ConcertDto.SeatsResponse.from(seats));
    }
}
