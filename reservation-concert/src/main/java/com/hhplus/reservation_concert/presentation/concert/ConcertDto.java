package com.hhplus.reservation_concert.presentation.concert;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ConcertDto {

    @Getter
    @Builder
    public static class ConcertResponse {
        private Long concertId;
        private String concertTitle;
        private List<ConcertRoundResponse> concertRoundResponses;
    }

    @Getter
    @Builder
    public static class ConcertRoundResponse {
        private Long concertRoundId;
        private LocalDateTime date;
    }

    @Getter
    @Builder
    public static class SeatResponse {
        private Long seatId;
        private Integer seatNumber;
        private Integer price;
    }

    @Getter
    @Builder
    public static class SeatsResponse {
        private List<SeatResponse> concertSeatResponses;
    }
}
