package com.hhplus.reservation_concert.application;

import com.hhplus.reservation_concert.domain.concert.Concert;
import com.hhplus.reservation_concert.domain.concert.ConcertService;
import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.domain.concert.performance.PerformanceService;
import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.concert.seat.SeatService;
import com.hhplus.reservation_concert.domain.reservation.Reservation;
import com.hhplus.reservation_concert.domain.reservation.ReservationService;
import com.hhplus.reservation_concert.domain.reservation.payment.PaymentService;
import com.hhplus.reservation_concert.domain.token.TokenService;
import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.domain.user.UserService;
import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.global.error.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
class ReservationFacadeTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private UserService userService;

    private User user;
    private User user2;
    private Seat seat;
    private Seat seat2;

    @BeforeEach
    void setUp() {
        Concert concert = new Concert(1L, "흠뻑쇼", "싸이");
        Performance performance = new Performance(1L, concert, LocalDateTime.now().plusDays(3));
        user = new User(1L, "이동주", 0);
        user2 = new User(2L, "이동주", 0);
        seat = new Seat(1L, performance, 1, 10000);
        seat2 = new Seat(2L, performance, 2, 10000);

        concertService.saveConcert(concert);
        performanceService.savePerformance(performance);
        seatService.saveSeat(seat);
        seatService.saveSeat(seat2);
    }

    @Test
    @Transactional
    void reserveSeat() {
        Seat getSeat = seatService.getSeat(seat.getId());
        Reservation reservation = reservationService.reserve(user.getId(), getSeat);

        assertThat(reservation.getSeatId()).isEqualTo(getSeat.getId());
    }

    @Test
    @Transactional
    void reserveReservedSeat() {
        Seat savedSeat = seatService.getSeat(seat.getId());
        reservationService.reserve(user.getId(), savedSeat);

        try {
            reservationService.reserve(user2.getId(), savedSeat);
            fail("한 좌석 여러명 예약");
        } catch (CustomException e) {
            assertThat(e.getMessage()).isEqualTo(ErrorCode.SEAT_TEMPORARY_RESERVED.getMessage());
        }
    }

    @Test
    void concurrentReserve() throws InterruptedException {
        AtomicInteger errorCount = new AtomicInteger();
        AtomicLong atomicUserId = new AtomicLong(1L);
        int numThreads = 10;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                try {
                    reservationFacade.reserveSeat(atomicUserId.getAndIncrement(), seat.getId());
                } catch (CustomException e) {
                    errorCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();

        assertThat(errorCount.get()).isEqualTo(numThreads - 1);
    }

    @Test
    @Transactional
    void payFail() {
        Reservation reserved = reservationService.reserve(user.getId(), seat);

        try {
            userService.usePoint(user, reserved.getPrice());
            fail("돈 모자란데 결제 성공");
        } catch (CustomException e) {
            assertThat(e.getMessage()).isEqualTo(ErrorCode.POINT_NOT_ENOUGH.getMessage());
        }
    }
}