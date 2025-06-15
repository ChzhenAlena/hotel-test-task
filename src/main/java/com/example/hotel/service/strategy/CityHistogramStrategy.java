package com.example.hotel.service.strategy;

import com.example.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CityHistogramStrategy implements HistogramStrategy {

    private final HotelRepository hotelRepository;

    @Override
    public boolean supports(String param) {
        return "city".equalsIgnoreCase(param);
    }

    @Override
    public Map<String, Long> getHistogram() {
        return hotelRepository.countGroupedByCity();
    }
}