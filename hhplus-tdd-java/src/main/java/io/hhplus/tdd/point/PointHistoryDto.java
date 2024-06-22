package io.hhplus.tdd.point;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class PointHistoryDto {

    @Builder
    @Getter
    public static class PointHistoryResponse {
        private long id;
        private long userId;
        private long amount;
        private TransactionType type;
        private long updateMillis;
    }

    @Getter
    public static class PointHistoryListResponse {
        private final List<PointHistoryResponse> pointHistoryListResponse;

        @Builder
        public PointHistoryListResponse(List<PointHistory> pointHistoryList) {
            this.pointHistoryListResponse = pointHistoryList.stream().map(
                    pointHistory -> PointHistoryResponse.builder()
                            .id(pointHistory.id())
                            .userId(pointHistory.userId())
                            .amount(pointHistory.amount())
                            .type(pointHistory.type())
                            .updateMillis(pointHistory.updateMillis())
                            .build()
            ).collect(Collectors.toList());
        }
    }
}
