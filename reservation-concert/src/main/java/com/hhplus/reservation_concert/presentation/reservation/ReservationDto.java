package com.hhplus.reservation_concert.presentation.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReservationDto {

    @Getter
    @NoArgsConstructor
    public static class ReservationRequest {
        private Long performanceId;
        private Long seatId;
    }

    @Getter
    @Builder
    public static class ReservationResponse {
        private Long reservationId;
        private Long seatId;
        private Integer price;
        private String status;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class PaymentResponse {
        private Long paymentId;
        private Integer price;
        private LocalDateTime createdAt;
    }
}
