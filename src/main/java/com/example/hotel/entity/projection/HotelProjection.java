package com.example.hotel.entity.projection;

import com.example.hotel.entity.AddressLike;

public interface HotelProjection extends AddressLike {
    Long getId();
    String getName();
    String getDescription();
    String getPhone();
}