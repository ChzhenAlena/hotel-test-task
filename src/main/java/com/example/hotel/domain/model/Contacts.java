package com.example.hotel.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacts {
    private Long id;
    private String phone;
    private String email;
    private Hotel hotel;
}