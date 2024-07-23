package com.hhplus.reservation_concert.application;

import com.hhplus.reservation_concert.domain.token.Token;
import com.hhplus.reservation_concert.domain.user.User;
import com.hhplus.reservation_concert.domain.user.UserService;
import com.hhplus.reservation_concert.domain.user.point.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public PointHistory chargePoint(Token token, Integer point) {
        User user = userService.getUser(token.getUserId());

        return userService.chargePoint(user, point);
    }

    public Integer getPoint(Token token) {
        Long userId = token.getUserId();

        return userService.getUser(userId).getPoint();
    }
}
