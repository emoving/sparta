package com.hhplus.reservation_concert.presentation.reservation;

import com.hhplus.reservation_concert.application.ReservationFacade;
import com.hhplus.reservation_concert.domain.reservation.Reservation;
import com.hhplus.reservation_concert.domain.reservation.payment.Payment;
import com.hhplus.reservation_concert.domain.token.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "예약 API", description = "좌석 예약 및 결제, X-TOKEN 필요")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @Operation(summary = "좌석 예약", description = "예약 시 타 유저는 좌석 예약 불가, 해당 예약은 10분간 유효")
    @PostMapping
    public ResponseEntity<ReservationDto.ReservationResponse> reserveSeat(HttpServletRequest request, @RequestBody ReservationDto.ReservationRequest reservationRequest) {
        Token token = (Token) request.getAttribute("token");
        Long seatId = reservationRequest.getSeatId();

        Reservation reservation = reservationFacade.reserveSeat(token.getUserId(), seatId);

        return ResponseEntity.ok(ReservationDto.ReservationResponse.builder()
                .reservationId(reservation.getId())
                .seatId(reservation.getSeatId())
                .price(reservation.getPrice())
                .status(String.valueOf(reservation.getStatus()))
                .createdAt(reservation.getCreatedAt())
                .build());
    }

    @Operation(summary = "예약 좌석 결제", description = "결제 시 대기열 토큰 만료")
    @PostMapping("/{reservationId}/payment")
    public ResponseEntity<ReservationDto.PaymentResponse> pay(HttpServletRequest request, @PathVariable Long reservationId) {
        Token token = (Token) request.getAttribute("token");
        Payment payment = reservationFacade.pay(token, reservationId);

        return ResponseEntity.ok(ReservationDto.PaymentResponse.builder()
                .paymentId(payment.getId())
                .price(payment.getPrice())
                .createdAt(payment.getCreatedAt())
                .build());
    }
}
