package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointHistoryRepositoryImplTest {

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private PointHistoryRepositoryImpl pointHistoryRepository;

    // id값 0부터 쌓이는지에 대한 저장 테스트
    @Test
    void insert() {
        PointHistory pointHistory = new PointHistory(0L, 1L, 1000L, TransactionType.CHARGE, System.currentTimeMillis());
        when(pointHistoryTable.insert(anyLong(), anyLong(), any(TransactionType.class), anyLong())).thenReturn(pointHistory);

        PointHistory insertedPointHistory = pointHistoryRepository.insert(1L, 1000L, TransactionType.CHARGE, System.currentTimeMillis());

        assertThat(insertedPointHistory.id()).isEqualTo(0L);
        assertThat(insertedPointHistory.userId()).isEqualTo(1L);
    }
}