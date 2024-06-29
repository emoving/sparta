package hhplus.clean_architecture.controller;

import hhplus.clean_architecture.domain.lecture.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class LectureDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LectureRequest {
        private Long userId;
        private Long lectureId;
    }

    @Builder
    @Getter
    public static class LectureResponse {
        private Long currentApplications;
        private Long maxApplications;
        private LocalDateTime date;
        private LocalDateTime applyStartDate;
    }

    @Builder
    @Getter
    public static class LectureApplyResponse {
        private String title;
        private Long currentApplications;
        private Long maxApplications;
        private LocalDateTime lectureDate;
        private LocalDateTime applyDate;
    }

    @Builder
    @Getter
    public static class LectureListResponse {
        private List<LectureResponse> lectureResponses;

        public static List<LectureResponse> from(List<Lecture> lectures) {
            return lectures.stream()
                    .map(lecture -> LectureResponse.builder()
                            .currentApplications(lecture.getCurrentApplications())
                            .maxApplications(lecture.getMaxApplications())
                            .date(lecture.getDate())
                            .applyStartDate(lecture.getApplyStartDate())
                            .build())
                    .toList();
        }
    }
}
