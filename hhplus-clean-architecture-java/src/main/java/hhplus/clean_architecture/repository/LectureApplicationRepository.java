package hhplus.clean_architecture.repository;


import hhplus.clean_architecture.domain.lecture.Lecture;
import hhplus.clean_architecture.domain.lectureApplication.LectureApplication;
import hhplus.clean_architecture.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureApplicationRepository extends JpaRepository<LectureApplication, Long> {

    Optional<LectureApplication> findByUserAndLecture(User user, Lecture lecture);
}
