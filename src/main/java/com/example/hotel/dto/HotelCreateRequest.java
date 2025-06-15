package com.example.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HotelCreateRequest(
        @NotBlank
        @Schema(description = "Name of the hotel", example = "DoubleTree by Hilton Minsk")
        String name,

        @Schema(description = "Description of the hotel", example = "The DoubleTree by Hilton Hotel Minsk offers 193 " +
                "luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...")
        String description,

        @NotBlank
        @Schema(description = "Hotel brand", example = "Hilton")
        String brand,

        @NotNull
        @Valid
        @Schema(description = "Address information of the hotel")
        AddressDto address,

        @NotNull
        @Valid
        @Schema(description = "Contact details of the hotel")
        ContactsDto contacts,

        @NotNull
        @Valid
        @Schema(description = "Arrival time details")
        ArrivalTimeDto arrivalTime
) {
}