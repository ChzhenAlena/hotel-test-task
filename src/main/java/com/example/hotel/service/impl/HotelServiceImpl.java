package com.example.hotel.service.impl;

import com.example.hotel.domain.model.Amenity;
import com.example.hotel.domain.model.Hotel;
import com.example.hotel.domain.repository.HotelDataAccess;
import com.example.hotel.dto.HotelCreateRequest;
import com.example.hotel.dto.HotelDetailsDto;
import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.exception.AppException;
import com.example.hotel.exception.ExceptionCode;
import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.service.AmenityService;
import com.example.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelDataAccess dataAccess;
    private final AmenityService amenityService;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortDto> getAll() {
        return dataAccess.findAll().stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDetailsDto getById(Long id) {
        Hotel hotel = dataAccess.findById(id)
                .orElseThrow(() -> new AppException(ExceptionCode.HOTEL_NOT_FOUND_BY_ID, HttpStatus.NOT_FOUND, id));
        return hotelMapper.toDetailsDto(hotel);
    }

    @Override
    @Transactional
    public HotelShortDto create(HotelCreateRequest hotelCreateRequest) {
        Hotel hotel = hotelMapper.toDomain(hotelCreateRequest);
        hotel.getAddress().setHotel(hotel);
        hotel.getContacts().setHotel(hotel);
        hotel.getArrivalTime().setHotel(hotel);
        Hotel saved = dataAccess.save(hotel);
        return hotelMapper.toShortDto(saved);
    }

    @Override
    @Transactional
    public void addAmenities(Long id, List<String> amenityNames) {
        Hotel hotel = dataAccess.findById(id)
                .orElseThrow(() -> new AppException(ExceptionCode.HOTEL_NOT_FOUND_BY_ID, HttpStatus.NOT_FOUND, id));

        Set<Amenity> amenities = amenityService.getOrCreateAmenities(amenityNames);

        hotel.getAmenities().addAll(amenities);
        dataAccess.save(hotel);
    }
}
