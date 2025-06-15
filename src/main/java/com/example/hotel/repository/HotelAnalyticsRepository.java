package com.example.hotel.repository;

import java.util.Map;

public interface HotelAnalyticsRepository {
    Map<String, Long> countGroupedByBrand();

    Map<String, Long> countGroupedByCity();

    Map<String, Long> countGroupedByCountry();

    Map<String, Long> countGroupedByAmenities();
}
