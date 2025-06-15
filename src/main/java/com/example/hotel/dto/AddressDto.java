package com.example.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank
        @Schema(description = "House number of the address", example = "9")
        String houseNumber,

        @NotBlank
        @Schema(description = "Street name", example = "Pobediteley Avenue")
        String street,

        @NotBlank
        @Schema(description = "City name", example = "Minsk")
        String city,

        @NotBlank
        @Schema(description = "Country name", example = "Belarus")
        String country,

        @NotBlank
        @Schema(description = "Postal code", example = "220004")
        String postCode
) {
}