package com.example.hotel.domain.repository;

import com.example.hotel.domain.filter.HotelSearchFilter;
import com.example.hotel.domain.model.Hotel;

import java.util.List;

public interface HotelSearchDataAccess {
    List<Hotel> search(HotelSearchFilter filter);
}
