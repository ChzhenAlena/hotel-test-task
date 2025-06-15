package com.example.hotel.controller;

import com.example.hotel.dto.HotelCreateRequest;
import com.example.hotel.dto.HotelDetailsDto;
import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.service.external.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Endpoints for managing hotels")
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Get all hotels")
    @GetMapping
    public ResponseEntity<List<HotelShortDto>> getAll() {
        return ResponseEntity.ok(hotelService.getAll());
    }

    @Operation(summary = "Get hotel by ID")
    @GetMapping("/id")
    public ResponseEntity<HotelDetailsDto> getById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(hotelService.getById(id));
    }

    @Operation(summary = "Create a new hotel")
    @PostMapping
    public ResponseEntity<HotelShortDto> create(@RequestBody @Valid HotelCreateRequest hotelCreateRequest) {
        HotelShortDto createdHotel = hotelService.create(hotelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @Operation(summary = "Add amenities to a hotel")
    @PostMapping("/{id}/amenities")
    public ResponseEntity<Void> addAmenitiesToHotel(
            @Parameter(description = "ID of the hotel", required = true)
            @PathVariable Long id,

            @RequestBody @NotEmpty List<@NotBlank String> amenities
    ) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok().build();
    }
}
