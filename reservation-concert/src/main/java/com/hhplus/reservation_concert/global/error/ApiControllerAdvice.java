package com.hhplus.reservation_concert.global.error;

import com.hhplus.reservation_concert.global.error.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getStatus(), errorCode.getMessage()));
    }
}
