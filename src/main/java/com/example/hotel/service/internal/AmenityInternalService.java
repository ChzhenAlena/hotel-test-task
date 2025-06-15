package com.example.hotel.service.internal;

import com.example.hotel.entity.Amenity;

import java.util.List;
import java.util.Set;

public interface AmenityInternalService {
    Set<Amenity> getOrCreateAmenities(List<String> names);
}