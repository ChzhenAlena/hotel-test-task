package com.example.hotel.service.strategy;

import com.example.hotel.domain.repository.HotelAnalyticsDataAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AmenitiesHistogramStrategy implements HistogramStrategy {

    private final HotelAnalyticsDataAccess analyticsDataAccess;

    @Override
    public boolean supports(String param) {
        return "amenities".equalsIgnoreCase(param);
    }

    @Override
    public Map<String, Long> getHistogram() {
        return analyticsDataAccess.countGroupedByAmenities();
    }
}
