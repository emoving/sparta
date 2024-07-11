package com.hhplus.reservation_concert.global.error;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    TOKEN_IS_EMPTY(401, "요청 헤더에 토큰이 없습니다."),
    TOKEN_NOT_ACTIVE(403, "토큰이 활성화 상태가 아닙니다."),
    TOKEN_NOT_FOUND(404, "해당 토큰이 없습니다."),

    USER_NOT_FOUND(404, "해당 유저가 없습니다."),
    POINT_BELOW_ZERO(409, "충전 금액은 0원보다 커야 합니다."),
    POINT_NOT_ENOUGH(409, "포인트가 모자랍니다."),

    CONCERT_NOT_FOUND(404, "해당 콘서트가 없습니다."),
    PERFORMANCE_NOT_FOUND(404, "해당 공연이 없습니다."),
    SEAT_NOT_FOUND(404, "해당 좌석이 없습니다."),
    SEAT_TEMPORARY_RESERVED(409, "임시 배정된 좌석입니다."),
    SEAT_ALREADY_RESERVED(409, "이미 예약된 좌석입니다.");

    private final int status;
    private final String message;
}
