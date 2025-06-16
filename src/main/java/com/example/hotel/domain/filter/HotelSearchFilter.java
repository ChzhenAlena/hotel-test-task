package com.example.hotel.domain.filter;

import java.util.Set;

public record HotelSearchFilter(
        String name,
        String brand,
        String city,
        String country,
        Set<String> amenities
) {
}