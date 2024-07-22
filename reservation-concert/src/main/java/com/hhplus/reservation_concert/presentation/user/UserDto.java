package com.hhplus.reservation_concert.presentation.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class PointRequest {
        private Integer point;
    }

    @Getter
    @Builder
    public static class PointResponse {
        private Integer point;
    }

    @Getter
    @Builder
    public static class PointHistoryResponse {
        private Long id;
        private Integer point;
        private String type;
        private LocalDateTime createdAt;
    }
}
