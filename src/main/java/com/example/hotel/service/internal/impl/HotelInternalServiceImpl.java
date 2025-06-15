package com.example.hotel.service.internal.impl;

import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.projection.HotelProjection;
import com.example.hotel.exception.ExceptionCode;
import com.example.hotel.exception.AppException;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.service.internal.HotelInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelInternalServiceImpl implements HotelInternalService {
    private final HotelRepository hotelRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HotelProjection> findAll() {
        return hotelRepository.findAllHotelProjections();
    }

    @Override
    @Transactional(readOnly = true)
    public Hotel findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new AppException(ExceptionCode.HOTEL_NOT_FOUND_BY_ID, HttpStatus.NOT_FOUND, id));
    }

    @Override
    @Transactional
    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }
}