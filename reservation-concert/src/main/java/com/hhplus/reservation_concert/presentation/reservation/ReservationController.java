package com.hhplus.reservation_concert.presentation.reservation;

import com.hhplus.reservation_concert.application.ReservationFacade;
import com.hhplus.reservation_concert.application.TokenFacade;
import com.hhplus.reservation_concert.domain.reservation.Reservation;
import com.hhplus.reservation_concert.domain.reservation.payment.Payment;
import com.hhplus.reservation_concert.domain.token.Token;
import com.hhplus.reservation_concert.presentation.reservation.ReservationDto.PaymentResponse;
import com.hhplus.reservation_concert.presentation.reservation.ReservationDto.ReservationRequest;
import com.hhplus.reservation_concert.presentation.reservation.ReservationDto.ReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "예약 API", description = "좌석 예약 및 결제, X-TOKEN 필요")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final TokenFacade tokenFacade;
    private final ReservationFacade reservationFacade;

    @Operation(summary = "좌석 예약", description = "예약 시 타 유저는 좌석 예약 불가, 해당 예약은 10분간 유효")
    @PostMapping
    public ResponseEntity<ReservationResponse> reserveSeat(@RequestHeader(name = "X-Token") String xToken, @RequestBody ReservationRequest reservationRequest) {
        Token token = tokenFacade.validate(xToken);
        Long performanceId = reservationRequest.getPerformanceId();
        Long seatId = reservationRequest.getSeatId();

        Reservation reservation = reservationFacade.reserveSeat(token.getUserId(), seatId);

        return ResponseEntity.ok(ReservationResponse.builder()
                .reservationId(reservation.getId())
                .seatId(reservation.getSeatId())
                .price(reservation.getPrice())
                .build());
    }

    @Operation(summary = "예약 좌석 결제", description = "결제 시 대기열 토큰 만료")
    @PostMapping("/{reservationId}/payment")
    public ResponseEntity<PaymentResponse> pay(@RequestHeader(name = "X-Token") String xToken, @PathVariable Long reservationId) {
        Token token = tokenFacade.validate(xToken);
        Payment payment = reservationFacade.pay(token, reservationId);

        return ResponseEntity.ok(PaymentResponse.builder()
                .paymentId(payment.getId())
                .price(payment.getPrice())
                .createdAt(payment.getCreatedAt())
                .build());
    }
}
