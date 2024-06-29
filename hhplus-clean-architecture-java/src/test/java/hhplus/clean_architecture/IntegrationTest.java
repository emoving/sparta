package hhplus.clean_architecture;

import hhplus.clean_architecture.domain.LectureFacade;
import hhplus.clean_architecture.domain.lecture.Lecture;
import hhplus.clean_architecture.domain.lecture.LectureService;
import hhplus.clean_architecture.domain.lectureApplication.LectureApplication;
import hhplus.clean_architecture.domain.lectureApplication.LectureApplicationService;
import hhplus.clean_architecture.domain.user.User;
import hhplus.clean_architecture.domain.user.UserService;
import hhplus.clean_architecture.global.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureApplicationService lectureApplicationService;

    @Autowired
    private LectureFacade lectureFacade;

    private final Long lectureId = 1L;
    private final Long lecture2Id = 2L;

    @BeforeEach
    void setUp() {
        LocalDateTime lectureDate = LocalDateTime.of(2024, 6, 29, 17, 0, 0);
        LocalDateTime lectureApplyDate = LocalDateTime.of(2024, 6, 29, 0, 0, 0);

        lectureService.save(new Lecture(lectureId, "스프링", 0L, 30L, lectureDate, lectureApplyDate, new ArrayList<>()));
        lectureService.save(new Lecture(lecture2Id, "리액트", 0L, 50L, lectureDate, lectureApplyDate, new ArrayList<>()));
    }

    @Test
    void apply() {
        User user = userService.save(User.builder().name("이동주").build());
        LectureApplication appliedApplication = lectureFacade.apply(user.getId(), lectureId);

        assertThat("이동주").isEqualTo(appliedApplication.getUser().getName());
        assertThat(1L).isEqualTo(appliedApplication.getLecture().getCurrentApplications());
    }

    @Test
    void getLectures() {
        List<Lecture> lectures = lectureFacade.getLectures();

        assertThat(lectures).hasSize(2);
        assertThat(lectures).extracting(Lecture::getTitle).contains("스프링", "리액트");
        assertThat(lectures.get(0).getMaxApplications()).isEqualTo(30L);
        assertThat(lectures.get(1).getMaxApplications()).isEqualTo(50L);
    }

    @Test
    void getApplication() {
        User user = userService.save(User.builder().name("이동주").build());
        lectureFacade.apply(user.getId(), lectureId);

        LectureApplication application = lectureFacade.getApplication(user.getId(), lectureId);

        assertThat(application.getUser().getId()).isEqualTo(user.getId());
        assertThat(application.getLecture().getId()).isEqualTo(lectureId);
    }

    @Test
    void concurrentApply() throws InterruptedException {
        AtomicInteger errorCount = new AtomicInteger();
        AtomicLong atomicUserId = new AtomicLong(1L);

        int numThreads = 60;
        for (int i = 1; i <= numThreads; i++) {
            userService.save(User.builder().name("이동주").build());
        }

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                try {
                    lectureFacade.apply(atomicUserId.getAndIncrement(), lecture2Id);
                } catch (BaseException e) {
                    errorCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();

        Lecture savedLecture = lectureService.getLecture(lecture2Id);
        assertThat(savedLecture.getCurrentApplications()).isEqualTo(savedLecture.getMaxApplications());
        assertThat(errorCount.get()).isEqualTo(numThreads - savedLecture.getMaxApplications());
    }
}