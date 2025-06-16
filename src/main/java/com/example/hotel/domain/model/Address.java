package com.example.hotel.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address implements AddressLike {
    private Long id;
    private String houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
    private Hotel hotel;
}