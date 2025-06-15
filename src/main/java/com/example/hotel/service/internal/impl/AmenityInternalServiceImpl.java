package com.example.hotel.service.internal.impl;

import com.example.hotel.entity.Amenity;
import com.example.hotel.repository.AmenityRepository;
import com.example.hotel.service.internal.AmenityInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenityInternalServiceImpl implements AmenityInternalService {
    private final AmenityRepository amenityRepository;

    @Override
    @Transactional
    public Set<Amenity> getOrCreateAmenities(List<String> names) {
        List<String> trimmedNames = names.stream()
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());

        List<Amenity> existingAmenities = amenityRepository.findByNameInIgnoreCase(trimmedNames);

        Set<String> existingNamesUpper = existingAmenities.stream()
                .map(a -> a.getName().toUpperCase())
                .collect(Collectors.toSet());

        List<Amenity> toCreate = trimmedNames.stream()
                .filter(name -> !existingNamesUpper.contains(name.toUpperCase()))
                .map(name -> Amenity.builder().name(name).build())
                .collect(Collectors.toList());

        List<Amenity> savedAmenities = toCreate.isEmpty()
                ? List.of()
                : amenityRepository.saveAll(toCreate);

        Set<Amenity> allAmenities = new HashSet<>(existingAmenities);
        allAmenities.addAll(savedAmenities);

        return allAmenities;
    }
}