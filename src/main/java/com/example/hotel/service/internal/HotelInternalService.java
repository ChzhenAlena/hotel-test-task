package com.example.hotel.service.internal;

import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.projection.HotelProjection;

import java.util.List;

public interface HotelInternalService {
    List<HotelProjection> findAll();

    Hotel findById(Long id);

    Hotel save(Hotel hotel);
}