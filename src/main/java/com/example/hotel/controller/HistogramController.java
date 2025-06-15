package com.example.hotel.controller;

import com.example.hotel.service.external.HistogramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/histogram")
@RequiredArgsConstructor
@Tag(name = "Histogram", description = "API for fetching histograms by parameter")
public class HistogramController {

    private final HistogramService histogramService;

    @Operation(summary = "Get histogram by parameter", description = "Parameter can be brand, city, country, or amenities")
    @GetMapping("/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(
            @Parameter(description = "Parameter to group by: brand, city, country, amenities", example = "brand")
            @PathVariable String param) {
        return ResponseEntity.ok(histogramService.getHistogram(param));
    }
}
