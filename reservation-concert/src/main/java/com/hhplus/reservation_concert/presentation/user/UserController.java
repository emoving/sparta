package com.hhplus.reservation_concert.presentation.user;

import com.hhplus.reservation_concert.application.TokenFacade;
import com.hhplus.reservation_concert.application.UserFacade;
import com.hhplus.reservation_concert.domain.token.Token;
import com.hhplus.reservation_concert.domain.user.point.PointHistory;
import com.hhplus.reservation_concert.presentation.user.UserDto.PointHistoryResponse;
import com.hhplus.reservation_concert.presentation.user.UserDto.PointRequest;
import com.hhplus.reservation_concert.presentation.user.UserDto.PointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 잔액 충전 및 조회, X-TOKEN 필요")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final TokenFacade tokenFacade;
    private final UserFacade userFacade;

    @Operation(summary = "잔액 조회")
    @GetMapping("/point")
    public ResponseEntity<PointResponse> get(@RequestHeader(name = "X-Token") String xToken) {
        Token token = tokenFacade.validate(xToken);
        Integer point = userFacade.getPoint(token);

        return ResponseEntity.ok(PointResponse.builder().point(point).build());
    }

    @Operation(summary = "잔액 충전")
    @PostMapping("/point")
    public ResponseEntity<PointHistoryResponse> charge(@RequestHeader(name = "X-Token") String xToken, @RequestBody PointRequest pointRequest) {
        Token token = tokenFacade.validate(xToken);
        PointHistory pointHistory = userFacade.chargePoint(token, pointRequest.getPoint());

        return ResponseEntity.ok(PointHistoryResponse.builder()
                .id(pointHistory.getId())
                .point(pointHistory.getPoint())
                .type(String.valueOf(pointHistory.getType()))
                .createdAt(pointHistory.getCreatedAt())
                .build());
    }
}
