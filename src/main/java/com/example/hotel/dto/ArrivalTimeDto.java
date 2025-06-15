package com.example.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ArrivalTimeDto(
        @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$")
        @Schema(example = "12:00")
        @NotBlank
        String checkIn,

        @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$")
        @Schema(example = "14:00")
        String checkOut
) {
}