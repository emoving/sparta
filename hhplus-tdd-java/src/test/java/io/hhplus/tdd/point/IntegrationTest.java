package io.hhplus.tdd.point;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // 포인트 조회 시, 존재하지 않는 id에 대한 테스트
    @Test
    void point() throws Exception {
        mockMvc.perform(get("/point/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.point").value(0L));
    }

    @Test
    void history() throws Exception {
        long userId = 1L;
        long id = 1L;
        long amount = 1000L;
        long id2 = 2L;
        long amount2 = 500L;

        mockMvc.perform(patch("/point/{id}/charge", userId).contentType(MediaType.APPLICATION_JSON).content(String.valueOf(amount)));
        mockMvc.perform(patch("/point/{id}/use", userId).contentType(MediaType.APPLICATION_JSON).content(String.valueOf(amount2)));

        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pointHistoryListResponse").isArray())
                .andExpect(jsonPath("$.pointHistoryListResponse.length()").value(2))
                .andExpect(jsonPath("$.pointHistoryListResponse[0].id").value(id))
                .andExpect(jsonPath("$.pointHistoryListResponse[0].userId").value(userId))
                .andExpect(jsonPath("$.pointHistoryListResponse[0].amount").value(amount))
                .andExpect(jsonPath("$.pointHistoryListResponse[0].type").value("CHARGE"))
                .andExpect(jsonPath("$.pointHistoryListResponse[1].id").value(id2))
                .andExpect(jsonPath("$.pointHistoryListResponse[1].userId").value(userId))
                .andExpect(jsonPath("$.pointHistoryListResponse[1].amount").value(amount2))
                .andExpect(jsonPath("$.pointHistoryListResponse[1].type").value("USE"));
    }

    // 충전 금액이 0원 보다 작을 경우, 포인트 충전 실패
    @Test
    void charge() throws Exception {
        long id = 1L;
        long amount = -1000L;

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

        mockMvc.perform(patch("/point/{id}/use", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잔액이 부족합니다."));
    }
}
