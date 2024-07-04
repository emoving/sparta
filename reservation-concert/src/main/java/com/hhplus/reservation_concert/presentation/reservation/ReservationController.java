package com.hhplus.reservation_concert.presentation.reservation;

import com.hhplus.reservation_concert.presentation.reservation.ReservationDto.PaymentResponse;
import com.hhplus.reservation_concert.presentation.reservation.ReservationDto.ReservationRequest;
import com.hhplus.reservation_concert.presentation.reservation.ReservationDto.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @PostMapping
    public ResponseEntity<ReservationResponse> reserveSeats(@RequestHeader("Authorization") String authorization, @RequestBody ReservationRequest reservationRequest) {
        Long concertRoundId = reservationRequest.getConcertRoundId();
        LocalDateTime date = reservationRequest.getDate();
        List<Long> seatIdList = reservationRequest.getSeatIdList();

        return ResponseEntity.ok(ReservationResponse.builder()
                .reservationId(1L)
                .build());
    }

    @PostMapping("/{reservationId}/payment")
    public ResponseEntity<PaymentResponse> pay(@RequestHeader("Authorization") String authorization, @PathVariable Long reservationId) {
        return ResponseEntity.ok(PaymentResponse.builder()
                .paymentId(1L)
                .price(20000)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build());
    }
}
