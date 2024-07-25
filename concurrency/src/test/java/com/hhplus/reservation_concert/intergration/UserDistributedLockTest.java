package com.hhplus.reservation_concert.intergration;

import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.domain.user.UserService;
import com.hhplus.reservation_concert.infrastructure.user.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDistributedLockTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder().point(1000).build());
    }

    @Test
    void usePointWithDistributedLock() throws Exception {
        Long userId = 1L;
        int usePoint = 800;
        int numberOfThreads = 1000;
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Exception> exceptions = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    userService.usePointWithDistributedLock(userId, usePoint);
                } catch (Exception e) {
                    synchronized (exceptions) {
                        exceptions.add(e);
                    }
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        User user = userService.getUser(userId);
        assertThat(user.getPoint()).isEqualTo(1000 - usePoint);
//        assertThat(exceptions.size()).isEqualTo(numberOfThreads - 1);
        System.out.println("UserDistributedLock Performance: " + (endTime - startTime) + "ms");
    }
}
