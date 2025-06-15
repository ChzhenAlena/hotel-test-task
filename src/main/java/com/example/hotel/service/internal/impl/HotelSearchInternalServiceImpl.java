package com.example.hotel.service.internal.impl;

import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.projection.HotelProjection;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.repository.specification.HotelSpecifications;
import com.example.hotel.service.internal.HotelSearchInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HotelSearchInternalServiceImpl implements HotelSearchInternalService {

    private final HotelRepository hotelRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> search(String name, String brand, String city, String country, Set<String> amenities) {
        Specification<Hotel> spec = buildSearchSpecification(name, brand, city, country, amenities);
        return hotelRepository.findAll(spec);
    }

    private Specification<Hotel> buildSearchSpecification(String name, String brand, String city,
                                                          String country, Set<String> amenities) {
        return Stream.of(
                        Optional.ofNullable(name).map(HotelSpecifications::hasName),
                        Optional.ofNullable(brand).map(HotelSpecifications::hasBrand),
                        Optional.ofNullable(city).map(HotelSpecifications::hasCity),
                        Optional.ofNullable(country).map(HotelSpecifications::hasCountry),
                        Optional.ofNullable(amenities).map(HotelSpecifications::hasAllAmenities)
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());
    }
}