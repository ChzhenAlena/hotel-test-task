package com.example.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactsDto(
        @NotBlank
        @Schema(description = "Contact phone number", example = "+1-555-123-4567")
        String phone,

        @Email
        @NotBlank
        @Schema(description = "Contact email address", example = "example@email.com")
        String email
) {
}