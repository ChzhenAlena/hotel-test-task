package com.example.hotel.service.impl;

import com.example.hotel.domain.model.Amenity;
import com.example.hotel.domain.repository.AmenityDataAccess;
import com.example.hotel.service.AmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenityServiceImpl implements AmenityService {

    private final AmenityDataAccess dataAccess;

    @Override
    @Transactional
    public Set<Amenity> getOrCreateAmenities(List<String> names) {
        List<String> trimmedNames = names.stream()
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());

        List<Amenity> existingAmenities = dataAccess.findByNameInIgnoreCase(trimmedNames);

        Set<String> existingNamesLower = existingAmenities.stream()
                .map(a -> a.getName().toLowerCase())
                .collect(Collectors.toSet());

        List<Amenity> toCreate = trimmedNames.stream()
                .filter(name -> !existingNamesLower.contains(name.toLowerCase()))
                .map(name -> Amenity.builder().name(name).build())
                .collect(Collectors.toList());

        List<Amenity> savedAmenities = toCreate.isEmpty()
                ? List.of()
                : dataAccess.saveAll(toCreate);

        Set<Amenity> allAmenities = new HashSet<>(existingAmenities);
        allAmenities.addAll(savedAmenities);

        return allAmenities;
    }
}