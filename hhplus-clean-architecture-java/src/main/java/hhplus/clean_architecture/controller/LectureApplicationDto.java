package hhplus.clean_architecture.controller;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class LectureApplicationDto {

    @Getter
    @Builder
    public static class LectureApplicationResponse {
        private Long userId;
        private Long lectureId;
        private LocalDateTime applyDate;
    }
}
