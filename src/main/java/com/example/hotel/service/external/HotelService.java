package com.example.hotel.service.external;

import com.example.hotel.dto.HotelCreateRequest;
import com.example.hotel.dto.HotelDetailsDto;
import com.example.hotel.dto.HotelShortDto;

import java.util.List;

public interface HotelService {
    List<HotelShortDto> getAll();

    HotelDetailsDto getById(Long id);

    HotelShortDto create(HotelCreateRequest hotelCreateRequest);

    void addAmenities(Long id, List<String> amenities);
}
