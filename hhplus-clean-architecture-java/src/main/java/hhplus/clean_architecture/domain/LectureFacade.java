package hhplus.clean_architecture.domain;

import hhplus.clean_architecture.domain.lecture.Lecture;
import hhplus.clean_architecture.domain.lecture.LectureService;
import hhplus.clean_architecture.domain.lectureApplication.LectureApplication;
import hhplus.clean_architecture.domain.lectureApplication.LectureApplicationService;
import hhplus.clean_architecture.domain.user.User;
import hhplus.clean_architecture.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LectureFacade {

    private final UserService userService;
    private final LectureService lectureService;
    private final LectureApplicationService lectureApplicationService;

    @Transactional
    public LectureApplication apply(Long userId, Long lectureId) {
        User user = userService.find(userId);
        Lecture lecture = lectureService.getLecture(lectureId);

        lecture.apply(user);

        return lectureApplicationService.save(LectureApplication.from(user, lecture));
    }

    public List<Lecture> getLectures() {
        return lectureService.getLectures();
    }

    public LectureApplication getApplication(Long userId, Long lectureId) {
        User user = userService.find(userId);
        Lecture lecture = lectureService.getLecture(lectureId);

        return lectureApplicationService.find(user, lecture);
    }
}
