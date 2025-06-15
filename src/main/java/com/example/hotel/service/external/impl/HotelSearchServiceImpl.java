package com.example.hotel.service.external.impl;

import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.service.external.HotelSearchService;
import com.example.hotel.service.internal.HotelSearchInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HotelSearchServiceImpl implements HotelSearchService {

    private final HotelSearchInternalService internalService;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortDto> search(String name, String brand, String city, String country, Set<String> amenities) {
        return internalService.search(name, brand, city, country, amenities).stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }
}