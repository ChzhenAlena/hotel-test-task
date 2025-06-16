package com.example.hotel.domain.repository;

import com.example.hotel.domain.model.Amenity;

import java.util.List;

public interface AmenityDataAccess {
    List<Amenity> findByNameInIgnoreCase(List<String> names);

    List<Amenity> saveAll(List<Amenity> amenities);

    Amenity save(Amenity amenity);

    void deleteAll();
}
