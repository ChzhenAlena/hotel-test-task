package com.example.hotel.service;

import com.example.hotel.dto.HotelShortDto;

import java.util.List;
import java.util.Set;

public interface HotelSearchService {
    List<HotelShortDto> search(String name, String brand, String city, String country, Set<String> amenities);
}
