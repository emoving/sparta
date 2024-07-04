package com.hhplus.reservation_concert.presentation.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public static class BalanceRequest {
        private Integer amount;
    }

    @Getter
    @Builder
    public static class BalanceResponse {
        private Integer balance;
    }

    @Getter
    @Builder
    public static class BalanceHistoryResponse {
        private Long balanceHistoryId;
        private Integer amount;
        private String type;
        private LocalDateTime createdAt;
    }
}
