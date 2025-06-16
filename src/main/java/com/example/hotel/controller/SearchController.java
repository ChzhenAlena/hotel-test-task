package com.example.hotel.controller;

import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.service.HotelSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Endpoints for searching hotels")
public class SearchController {
    private final HotelSearchService hotelSearchService;

    @Operation(summary = "Search hotels by filters")
    @GetMapping
    public ResponseEntity<List<HotelShortDto>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Set<String> amenities
    ) {
        List<HotelShortDto> hotels = hotelSearchService.search(name, brand, city, country, amenities);
        return ResponseEntity.ok(hotels);
    }
}
