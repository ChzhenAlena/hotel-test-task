package com.example.hotel.service.impl;

import com.example.hotel.domain.filter.HotelSearchFilter;
import com.example.hotel.domain.repository.HotelSearchDataAccess;
import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.service.HotelSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HotelSearchServiceImpl implements HotelSearchService {

    private final HotelSearchDataAccess dataAccess;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortDto> search(String name, String brand, String city, String country, Set<String> amenities) {
        HotelSearchFilter filter = new HotelSearchFilter(name, brand, city, country, amenities);
        return dataAccess.search(filter).stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }
}