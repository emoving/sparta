package com.hhplus.reservation_concert.presentation.user;

import com.hhplus.reservation_concert.presentation.user.UserDto.BalanceHistoryResponse;
import com.hhplus.reservation_concert.presentation.user.UserDto.BalanceRequest;
import com.hhplus.reservation_concert.presentation.user.UserDto.BalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/balances")
    public ResponseEntity<BalanceResponse> get(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(BalanceResponse.builder()
                .balance(20000)
                .build());
    }

    @PostMapping("/balances")
    public ResponseEntity<BalanceHistoryResponse> charge(@RequestHeader("Authorization") String authorization, @RequestBody BalanceRequest balanceRequest) {
        return ResponseEntity.ok(BalanceHistoryResponse.builder()
                .balanceHistoryId(1L)
                .amount(10000)
                .type("충전")
                .createdAt(LocalDateTime.now())
                .build());
    }
}
