package com.example.hotel.service.internal;

import com.example.hotel.entity.Hotel;

import java.util.List;
import java.util.Set;

public interface HotelSearchInternalService {
    List<Hotel> search(String name, String brand, String city, String country, Set<String> amenities);
}