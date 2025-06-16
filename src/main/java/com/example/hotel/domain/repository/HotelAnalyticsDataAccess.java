package com.example.hotel.domain.repository;

import java.util.Map;

public interface HotelAnalyticsDataAccess {
    Map<String, Long> countGroupedByBrand();

    Map<String, Long> countGroupedByCity();

    Map<String, Long> countGroupedByCountry();

    Map<String, Long> countGroupedByAmenities();
}
