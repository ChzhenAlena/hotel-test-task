package com.example.hotel.service;

import com.example.hotel.domain.model.Amenity;

import java.util.List;
import java.util.Set;

public interface AmenityService {
    Set<Amenity> getOrCreateAmenities(List<String> names);
}