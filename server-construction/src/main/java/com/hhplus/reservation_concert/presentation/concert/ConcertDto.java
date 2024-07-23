package com.hhplus.reservation_concert.presentation.concert;

import com.hhplus.reservation_concert.domain.concert.Concert;
import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ConcertDto {

    @Getter
    @Builder
    public static class ConcertResponse {
        private Long id;
        private String title;
        private String singer;

        public static ConcertResponse from(Concert concert) {
            return ConcertResponse.builder()
                    .id(concert.getId())
                    .title(concert.getTitle())
                    .singer(concert.getSinger())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ConcertsResponse {
        private List<ConcertResponse> concertResponses;

        public static ConcertsResponse from(List<Concert> concerts) {
            List<ConcertResponse> list = concerts.stream().map(ConcertResponse::from).toList();

            return ConcertsResponse.builder().concertResponses(list).build();
        }
    }

    @Getter
    @Builder
    public static class PerformanceResponse {
        private Long id;
        private LocalDateTime date;
        private String place;

        public static PerformanceResponse from(Performance performance) {
            return PerformanceResponse.builder()
                    .id(performance.getId())
                    .date(performance.getDate())
                    .place(performance.getPlace())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class PerformancesResponse {
        private List<PerformanceResponse> performanceResponses;

        public static PerformancesResponse from(List<Performance> performances) {
            List<PerformanceResponse> list = performances.stream().map(PerformanceResponse::from).toList();

            return PerformancesResponse.builder().performanceResponses(list).build();
        }
    }

    @Getter
    @Builder
    public static class SeatResponse {
        private Long id;
        private Integer number;
        private Integer price;
        private String status;

        public static SeatResponse from(Seat seat) {
            return SeatResponse.builder()
                    .id(seat.getId())
                    .number(seat.getNumber())
                    .price(seat.getPrice())
                    .status(String.valueOf(seat.getStatus()))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class SeatsResponse {
        private List<SeatResponse> seatResponses;

        public static SeatsResponse from(List<Seat> seats) {
            List<SeatResponse> list = seats.stream().map(SeatResponse::from).toList();

            return SeatsResponse.builder().seatResponses(list).build();
        }
    }
}
