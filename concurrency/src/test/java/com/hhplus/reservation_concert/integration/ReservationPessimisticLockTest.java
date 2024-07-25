package com.hhplus.reservation_concert.integration;

import com.hhplus.reservation_concert.application.ReservationFacade;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.concert.seat.SeatStatus;
import com.hhplus.reservation_concert.infrastructure.concert.SeatRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.user.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReservationPessimisticLockTest {

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SeatRepositoryImpl seatRepository;

    @BeforeEach
    void setUp() {
        seatRepository.save(Seat.builder().status(SeatStatus.empty).build());
    }

    @Test
    void reserveSeatWithPessimisticLock() throws Exception {
        Long seatId = 1L;
        int numberOfThreads = 1000;
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Exception> exceptions = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    reservationFacade.reserveSeat(Mockito.any(), seatId);
                } catch (Exception e) {
                    synchronized (exceptions) {
                        exceptions.add(e);
                    }
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1,TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        Seat seat = seatRepository.findById(seatId).get();
        assertThat(seat.getStatus()).isEqualTo(SeatStatus.reserved);
        assertThat(exceptions.size()).isEqualTo(numberOfThreads - 1);
        System.out.println("ReservationPessimisticLock Performance: " + (endTime - startTime) + "ms");
    }
}