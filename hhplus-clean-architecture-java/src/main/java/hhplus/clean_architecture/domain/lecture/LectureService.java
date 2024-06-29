package hhplus.clean_architecture.domain.lecture;

import hhplus.clean_architecture.global.BaseException;
import hhplus.clean_architecture.global.ErrorCode;
import hhplus.clean_architecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;

    public Lecture getLecture(Long id) {
        return lectureRepository.findByWithPessimisticLock(id).orElseThrow(() -> new BaseException(ErrorCode.LECTURE_NOT_FOUND));
    }

    public List<Lecture> getLectures() {
        return lectureRepository.findAll();
    }

    @Transactional
    public Lecture save(Lecture lecture) {
        return lectureRepository.save(lecture);

    }
}
