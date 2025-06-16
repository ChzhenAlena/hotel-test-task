package com.example.hotel.domain.repository;

import com.example.hotel.domain.model.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelDataAccess {
    Optional<Hotel> findById(Long id);

    Hotel save(Hotel hotel);

    List<Hotel> findAll();

    void deleteAll();

    List<Hotel> saveAll(List<Hotel> hotels);
}
