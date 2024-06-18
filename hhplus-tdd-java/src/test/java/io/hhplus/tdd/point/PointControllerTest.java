package io.hhplus.tdd.point;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    // 포인트 조회 시, 존재하지 않는 id에 대한 테스트
    @Test
    void point() throws Exception {
        UserPoint userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
        when(pointService.getPoint(1L)).thenReturn(UserPoint.empty(1L));

        mockMvc.perform(get("/point/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.point").value(0L));

        verify(pointService, times(1)).getPoint(1L);
    }

    @Test
    void history() throws Exception {
        long id = 1L;
        List<PointHistory> mockHistory = Arrays.asList(
                new PointHistory(1L, id, 1000L, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, id, 500L, TransactionType.USE, System.currentTimeMillis())
        );

        Mockito.when(pointService.getHistory(id)).thenReturn(mockHistory);

        mockMvc.perform(get("/point/{id}/histories", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(id))
                .andExpect(jsonPath("$[0].amount").value(1000L))
                .andExpect(jsonPath("$[0].type").value("CHARGE"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userId").value(id))
                .andExpect(jsonPath("$[1].amount").value(500L))
                .andExpect(jsonPath("$[1].type").value("USE"));
    }

    // 충전 금액이 0원 보다 작을 경우, 포인트 충전 실패
    @Test
    void charge() throws Exception {
        long id = 1L;
        long amount = -1000L;

        given(pointService.chargePoint(eq(id), eq(amount)))
                .willThrow(new IllegalArgumentException("충전 금액은 0보다 커야 합니다."));

        mockMvc.perform(patch("/point/{id}/charge", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("충전 금액은 0보다 커야 합니다."));
    }

    // 사용 금액이 잔고 보다 클 경우, 포인트 사용 실패
    @Test
    void use() throws Exception {
        long id = 1L;
        long amount = 1000;

        given(pointService.usePoint(eq(id), eq(amount)))
                .willThrow(new IllegalArgumentException("잔액이 부족합니다."));

        mockMvc.perform(patch("/point/{id}/use", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잔액이 부족합니다."));
    }
}