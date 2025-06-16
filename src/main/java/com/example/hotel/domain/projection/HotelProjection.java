package com.example.hotel.domain.projection;

import com.example.hotel.domain.model.AddressLike;

public interface HotelProjection extends AddressLike {
    Long getId();

    String getName();

    String getDescription();

    String getPhone();
}