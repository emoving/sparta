package com.hhplus.reservation_concert.presentation.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationDto {

    @Getter
    @NoArgsConstructor
    public static class ReservationRequest {
        private Long concertRoundId;
        private LocalDateTime date;
        private List<Long> seatIdList;
    }

    @Getter
    @Builder
    public static class ReservationResponse {
        private Long reservationId;
    }

    @Getter
    @Builder
    public static class PaymentResponse {
        private Long paymentId;
        private Integer price;
        private LocalDateTime createdAt;
        private LocalDateTime expiredAt;
    }
}
