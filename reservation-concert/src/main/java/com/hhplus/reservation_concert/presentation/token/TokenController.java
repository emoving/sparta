package com.hhplus.reservation_concert.presentation.token;

import com.hhplus.reservation_concert.application.TokenFacade;
import com.hhplus.reservation_concert.domain.token.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "대기열 API", description = "대기열 X-TOKEN 발급")
@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final TokenFacade tokenFacade;

    @Operation(summary = "헤더를 통한 대기열 토큰 발급", description = "토큰이 이미 존재하는 경우, 존재 하는 토큰을 반환")
    @Parameter(name = "userId", description = "발급받을 유저 ID")
    @PostMapping("/{userId}")
    public ResponseEntity<Void> issue(@PathVariable Long userId) {
        Token token = tokenFacade.issue(userId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Token", token.getToken());

        return ResponseEntity.ok().headers(httpHeaders).build();
    }
}
