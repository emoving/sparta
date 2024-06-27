package hhplus.clean_architecture.domain.lectureApplication;


import hhplus.clean_architecture.domain.lecture.Lecture;
import hhplus.clean_architecture.domain.user.User;
import hhplus.clean_architecture.global.BaseException;
import hhplus.clean_architecture.global.ErrorCode;
import hhplus.clean_architecture.repository.LectureApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LectureApplicationService {

    private final LectureApplicationRepository lectureApplicationRepository;

    @Transactional(readOnly = true)
    public LectureApplication find(User user, Lecture lecture) {
        return lectureApplicationRepository.findByUserAndLecture(user, lecture).orElseThrow(() -> new BaseException(ErrorCode.LECTURE_APPLICATION_NOT_FOUND));
    }

    @Transactional
    public LectureApplication save(LectureApplication lectureApplication) {
        return lectureApplicationRepository.save(lectureApplication);
    }
}
