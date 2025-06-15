package com.example.hotel.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final ExceptionCode exceptionCode;
    private final HttpStatus httpStatus;
    private final String message;

    public AppException(ExceptionCode exceptionCode, HttpStatus httpStatus, Object... args) {
        super(exceptionCode.format(args));
        this.exceptionCode = exceptionCode;
        this.httpStatus = httpStatus;
        this.message = exceptionCode.format(args);
    }
}