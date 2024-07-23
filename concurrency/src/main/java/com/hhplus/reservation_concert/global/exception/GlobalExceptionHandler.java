package com.hhplus.reservation_concert.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        log.error("ERROR CODE: {}", errorCode.getStatus());
        log.error("ERROR MESSAGE: {}", errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getStatus(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("[exceptionHandler] ex", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
