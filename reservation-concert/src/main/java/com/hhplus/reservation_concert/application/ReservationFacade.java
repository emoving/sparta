package com.hhplus.reservation_concert.application;

import com.hhplus.reservation_concert.domain.concert.seat.Seat;
import com.hhplus.reservation_concert.domain.concert.seat.SeatService;
import com.hhplus.reservation_concert.domain.reservation.Reservation;
import com.hhplus.reservation_concert.domain.reservation.ReservationService;
import com.hhplus.reservation_concert.domain.reservation.payment.Payment;
import com.hhplus.reservation_concert.domain.reservation.payment.PaymentService;
import com.hhplus.reservation_concert.domain.token.Token;
import com.hhplus.reservation_concert.domain.token.TokenService;
import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReservationFacade {

    private final TokenService tokenService;
    private final SeatService seatService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final UserService userService;

    public Reservation reserveSeat(Long userId, Long seatId) {
        Seat seat = seatService.getSeat(seatId);
        Reservation reservation = reservationService.reserve(userId, seat);

        return reservationService.save(reservation);
    }

    public Payment pay(Token token, Long reservationId) {
        User user = userService.getUser(token.getUserId());
        Reservation reservation = reservationService.get(reservationId);

        userService.usePoint(user, reservation.getPrice());
        Payment payment = paymentService.save(Payment.from(reservation));
        tokenService.deleteToken(token);

        return payment;
    }
}
