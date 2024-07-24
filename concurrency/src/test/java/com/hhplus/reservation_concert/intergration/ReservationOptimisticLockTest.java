package com.hhplus.reservation_concert.intergration;

import com.hhplus.reservation_concert.application.ReservationFacade;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.concert.seat.SeatStatus;
import com.hhplus.reservation_concert.infrastructure.concert.SeatRepositoryImpl;
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
public class ReservationOptimisticLockTest {

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private SeatRepositoryImpl seatRepository;

    @BeforeEach
    void setUp() {
        seatRepository.save(Seat.builder().status(SeatStatus.empty).build());
    }

    @Test
    void reserveSeatWithOptimisticLock() throws Exception {
        Long seatId = 1L;
        int numberOfThreads = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Exception> exceptions = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    reservationFacade.reserveSeat(1L, seatId);
                } catch (Exception e) {
                    exceptions.add(e);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        Seat seat = seatRepository.findById(seatId).get();
        assertThat(seat.getStatus()).isEqualTo(SeatStatus.reserved);
        assertThat(seat.getVersion()).isEqualTo(1L);
        assertThat(exceptions.size()).isEqualTo(numberOfThreads - 1);
        exceptions.forEach(exception -> assertThat(exception).isInstanceOf(OptimisticLockingFailureException.class));
    }
}
