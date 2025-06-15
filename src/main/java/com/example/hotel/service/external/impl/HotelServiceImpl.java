package com.example.hotel.service.external.impl;

import com.example.hotel.dto.HotelCreateRequest;
import com.example.hotel.dto.HotelDetailsDto;
import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.entity.Amenity;
import com.example.hotel.entity.Hotel;
import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.service.external.HotelService;
import com.example.hotel.service.internal.AmenityInternalService;
import com.example.hotel.service.internal.HotelInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelInternalService hotelInternalService;
    private final AmenityInternalService amenityInternalService;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortDto> getAll() {
        return hotelInternalService.findAll().stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDetailsDto getById(Long id) {
        Hotel hotel = hotelInternalService.findById(id);
        return hotelMapper.toDetailsDto(hotel);
    }

    @Override
    @Transactional
    public HotelShortDto create(HotelCreateRequest hotelCreateRequest) {
        Hotel entity = hotelMapper.toEntity(hotelCreateRequest);
        entity.getAddress().setHotel(entity);
        entity.getContacts().setHotel(entity);
        entity.getArrivalTime().setHotel(entity);
        Hotel saved = hotelInternalService.save(entity);
        return hotelMapper.toShortDto(saved);
    }

    @Override
    @Transactional
    public void addAmenities(Long id, List<String> amenityNames) {
        Hotel hotel = hotelInternalService.findById(id);

        Set<Amenity> amenities = amenityInternalService.getOrCreateAmenities(amenityNames);

        hotel.getAmenities().addAll(amenities);
        hotelInternalService.save(hotel);
    }
}
