package com.hhplus.reservation_concert.presentation.concert;

import com.hhplus.reservation_concert.presentation.concert.ConcertDto.SeatResponse;
import com.hhplus.reservation_concert.presentation.concert.ConcertDto.SeatsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.hhplus.reservation_concert.presentation.concert.ConcertDto.*;

@RestController
@RequestMapping("/concerts")
public class ConcertController {

    @GetMapping("/dates")
    public ResponseEntity<ConcertResponse> getDates(@RequestHeader("Authorization") String authorization) {
        List<ConcertRoundResponse> concertRoundResponses = new ArrayList<>();
        concertRoundResponses.add(ConcertRoundResponse.builder()
                .concertRoundId(1L)
                .date(LocalDateTime.now())
                .build());
        concertRoundResponses.add(ConcertRoundResponse.builder()
                .concertRoundId(1L)
                .date(LocalDateTime.now().plusDays(1L))
                .build());

        return ResponseEntity.ok(ConcertResponse.builder()
                .concertId(1L)
                .concertTitle("개그콘서트")
                .concertRoundResponses(concertRoundResponses).build());
    }


    @GetMapping("{roundId}/seats")
    public ResponseEntity<SeatsResponse> getSeats(@RequestHeader("Authorization") String authorization, @PathVariable Long id) {
        List<SeatResponse> seatResponses = new ArrayList<>();
        seatResponses.add(SeatResponse.builder().seatId(1L).seatNumber(1).price(10000).build());
        seatResponses.add(SeatResponse.builder().seatId(2L).seatNumber(2).price(10000).build());

        return ResponseEntity.ok(SeatsResponse.builder().concertSeatResponses(seatResponses).build());
    }
}
