package com.hhplus.reservation_concert.intergration;

import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.domain.user.UserService;
import com.hhplus.reservation_concert.infrastructure.user.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserOptimisticLockTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder().name("이동주").point(1000).build());
    }

    @Test
    void usePointWithOptimisticLock() throws Exception {
        Long userId = 1L;
        int usePoint = 200;
        int numberOfThreads = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Exception> exceptions = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    userService.usePointWithOptimisticLock(userId, usePoint);
                } catch (Exception e) {
                    exceptions.add(e);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        User getUser = userService.getUser(userId);
        assertThat(getUser.getPoint()).isEqualTo(1000 - usePoint);
        assertThat(getUser.getVersion()).isEqualTo(1);
        assertThat(exceptions.size()).isEqualTo(numberOfThreads - 1);
        exceptions.forEach(exception -> assertThat(exception).isInstanceOf(OptimisticLockingFailureException.class));
    }
}