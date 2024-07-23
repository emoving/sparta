package com.hhplus.reservation_concert.domain.user;

import com.hhplus.reservation_concert.domain.user.point.PointHistory;
import com.hhplus.reservation_concert.global.exception.ErrorCode;
import com.hhplus.reservation_concert.global.exception.CustomException;
import com.hhplus.reservation_concert.infrastructure.user.PointHistoryRepositoryImpl;
import com.hhplus.reservation_concert.infrastructure.user.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @Mock
    private PointHistoryRepositoryImpl pointHistoryRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void chargePoint() {
        User user = new User(1L, "이동주", 1000);

        PointHistory pointHistory = userService.chargePoint(user, 1000);

        assertThat(user.getPoint()).isEqualTo(2000);
    }

    @Test
    void chargeMinusPoint() {
        User user = new User(1L, "이동주", 1000);

        assertThatThrownBy(() -> userService.chargePoint(user, -1000))
                .isInstanceOf(CustomException.class)
<<<<<<< Updated upstream
                .hasMessage(ErrorCode.POINT_CHARGE_BELOW_ZERO.getMessage());
=======
                .hasMessage(ErrorCode.POINT_BELOW_ZERO.getMessage());
>>>>>>> Stashed changes

        assertThat(user.getPoint()).isEqualTo(1000);
    }

    @Test
    void usePoint() {
        User user = new User(1L, "이동주", 1000);

        assertThatThrownBy(() -> userService.usePoint(user, 3000))
                .isInstanceOf(CustomException.class)
<<<<<<< Updated upstream
                .hasMessage(ErrorCode.POINT_USE_LACK.getMessage());
=======
                .hasMessage(ErrorCode.POINT_NOT_ENOUGH.getMessage());
>>>>>>> Stashed changes

        assertThat(user.getPoint()).isEqualTo(1000);
    }
}