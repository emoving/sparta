package com.hhplus.reservation_concert.intergration;

import com.hhplus.reservation_concert.application.ReservationFacade;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.concert.seat.SeatStatus;
import com.hhplus.reservation_concert.infrastructure.concert.SeatRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReservationDistributedLockTest {

    private static final Logger log = LoggerFactory.getLogger(ReservationDistributedLockTest.class);
    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private SeatRepositoryImpl seatRepository;

    @Autowired
    private RedissonClient redissonClient;

    @BeforeEach
    void setUp() {
        seatRepository.save(Seat.builder().status(SeatStatus.empty).build());
    }

    @Test
    void reserveSeatDistributedLock() throws Exception {
        Long seatId = 1L;
        int numberOfThreads = 1000;
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Exception> exceptions = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    reservationFacade.reserveSeat(1L, seatId);
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
        Seat seat = seatRepository.findById(seatId).get();
        assertThat(seat.getStatus()).isEqualTo(SeatStatus.reserved);
        assertThat(redissonClient.getLock("seatLocK: " + seatId).isLocked()).isFalse();
        System.out.println("ReservationDistributedLock Performance:: " + (endTime - startTime) + "ms");
    }
}
