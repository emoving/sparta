package com.hhplus.reservation_concert.global.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomException extends RuntimeException {

    private ErrorCode errorCode;
    private Throwable originalException;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Throwable originalException) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.originalException = originalException;
    }
}
