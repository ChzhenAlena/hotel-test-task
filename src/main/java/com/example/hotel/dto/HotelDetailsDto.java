package com.example.hotel.dto;

import java.util.Set;

public record HotelDetailsDto(
        Long id,
        String name,
        String description,
        String brand,
        AddressDto address,
        ContactsDto contacts,
        ArrivalTimeDto arrivalTime,
        Set<String> amenities
) {
}