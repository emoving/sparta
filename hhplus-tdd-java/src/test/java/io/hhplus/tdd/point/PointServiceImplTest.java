package io.hhplus.tdd.point;

import io.hhplus.tdd.point.PointHistoryDto.PointHistoryListResponse;
import io.hhplus.tdd.point.UserPointDto.UserPointResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceImplTest {

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private UserPointRepository userPointRepository;

    @InjectMocks
    private PointServiceImpl pointService;

    private UserPoint userPoint;

    @BeforeEach
    public void setUp() {
        userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
    }

    @Test
    void getPoint() {
        long id = 1L;
        given(userPointRepository.selectById(1L)).willReturn(userPoint);

        UserPointResponse getUserPoint = pointService.getPoint(id);

        assertThat(getUserPoint.getPoint()).isEqualTo(1000L);
        verify(userPointRepository, times(1)).selectById(id);
    }

    @Test
    void getHistory() {
        long userId = 1L;
        List<PointHistory> mockHistory = Arrays.asList(
                new PointHistory(1L, userId, 1000L, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, userId, 500L, TransactionType.USE, System.currentTimeMillis())
        );
        given(pointHistoryRepository.selectAllByUserId(userId)).willReturn(mockHistory);

        PointHistoryListResponse history = pointService.getHistory(userId);

        assertThat(history.getPointHistoryListResponse()).usingRecursiveComparison().isEqualTo(mockHistory);
        verify(pointHistoryRepository, times(1)).selectAllByUserId(userId);
    }

    @Test
    void chargePoint() {
        given(userPointRepository.selectById(1L)).willReturn(userPoint);

        assertThatThrownBy(() -> pointService.chargePoint(1L, -1000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("충전 금액은 0보다 커야 합니다.");

        verify(userPointRepository, never()).insertOrUpdate(anyLong(), anyLong());
    }

    @Test
    void usePoint() {
        given(userPointRepository.selectById(1L)).willReturn(userPoint);

        assertThatThrownBy(() -> pointService.usePoint(1L, 2000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잔액이 부족합니다.");

        verify(userPointRepository, never()).insertOrUpdate(anyLong(), anyLong());
    }
}