package com.example.hotel.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrivalTime {
    private Long id;
    private String checkIn;
    private String checkOut;
    private Hotel hotel;
}