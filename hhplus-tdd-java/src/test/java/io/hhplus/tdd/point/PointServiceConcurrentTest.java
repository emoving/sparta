package io.hhplus.tdd.point;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointServiceConcurrentTest {

    @Autowired
    private PointServiceImpl pointService;

    @Test
    public void ConcurrentChargeAndUse() throws InterruptedException {
        long id = 1L;
        long chargePoint = 1000L;
        long usePoint = 800L;
        int thread = 10;

        ExecutorService executor = Executors.newFixedThreadPool(thread);
        CountDownLatch latch = new CountDownLatch(thread);
        for (int i = 0; i < thread; i++) {
            executor.execute(() -> {
                pointService.chargePoint(id, chargePoint);
                pointService.usePoint(id, usePoint);
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();

        assertThat(pointService.getPoint(id).getPoint()).isEqualTo((chargePoint - usePoint) * 10);
    }
}
