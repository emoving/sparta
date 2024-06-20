package io.hhplus.tdd.point;

import lombok.Builder;
import lombok.Getter;

public class UserPointDto {

    @Builder
    @Getter
    public static class UserPointResponse {
        private long id;
        private long point;
        private long updateMillis;
    }
}
