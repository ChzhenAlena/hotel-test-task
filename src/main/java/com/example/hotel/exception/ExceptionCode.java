package com.example.hotel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
    INTERNAL_ERROR("Internal server error"),
    VALIDATION_ERROR("Validation failed"),
    INVALID_REQUEST("Invalid request format"),

    HOTEL_NOT_FOUND_BY_ID("Hotel not found by id: %s"),
    UNKNOWN_HISTOGRAM_PARAM("Unknown histogram parameter: %s");

    private final String template;

    public String format(Object... args) {
        return String.format(template, args);
    }
}
