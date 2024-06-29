package hhplus.clean_architecture.controller;


import hhplus.clean_architecture.controller.LectureApplicationDto.LectureApplicationResponse;
import hhplus.clean_architecture.controller.LectureDto.LectureApplyResponse;
import hhplus.clean_architecture.controller.LectureDto.LectureListResponse;
import hhplus.clean_architecture.controller.LectureDto.LectureRequest;
import hhplus.clean_architecture.domain.LectureFacade;
import hhplus.clean_architecture.domain.lecture.Lecture;
import hhplus.clean_architecture.domain.lectureApplication.LectureApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureFacade lectureFacade;

    @PostMapping("/apply")
    public ResponseEntity<LectureApplyResponse> apply(@RequestBody LectureRequest lectureRequest) {
        LectureApplication lectureApplication = lectureFacade.apply(lectureRequest.getUserId(), lectureRequest.getLectureId());
        Lecture lecture = lectureApplication.getLecture();

        return ResponseEntity.ok().body(LectureApplyResponse.builder()
                .title(lecture.getTitle())
                .currentApplications(lecture.getCurrentApplications())
                .maxApplications(lecture.getMaxApplications())
                .lectureDate(lecture.getDate())
                .applyDate(lectureApplication.getApplyDate())
                .build());
    }

    @GetMapping
    public ResponseEntity<LectureListResponse> getLectures() {
        List<Lecture> lectures = lectureFacade.getLectures();

        return ResponseEntity.ok().body(LectureListResponse.builder()
                .lectureResponses(LectureListResponse.from(lectures))
                .build());
    }

    @GetMapping("/{id}/application/{userId}")
    public ResponseEntity<LectureApplicationResponse> getLectureApplication(@PathVariable Long id, @PathVariable Long userId) {
        LectureApplication application = lectureFacade.getApplication(id, userId);

        return ResponseEntity.ok().body(LectureApplicationResponse.builder()
                .userId(application.getUser().getId())
                .lectureId(application.getLecture().getId())
                .applyDate(application.getApplyDate())
                .build());
    }
}
