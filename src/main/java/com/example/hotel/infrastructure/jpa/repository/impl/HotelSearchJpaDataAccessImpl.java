package com.example.hotel.infrastructure.jpa.repository.impl;

import com.example.hotel.domain.filter.HotelSearchFilter;
import com.example.hotel.domain.model.Hotel;
import com.example.hotel.domain.repository.HotelSearchDataAccess;
import com.example.hotel.infrastructure.jpa.entity.HotelEntity;
import com.example.hotel.infrastructure.jpa.repository.HotelJpaRepository;
import com.example.hotel.infrastructure.jpa.repository.specification.HotelSpecifications;
import com.example.hotel.mapper.HotelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.hotel.infrastructure.jpa.repository.specification.HotelSpecifications.fetchAssociations;

@Component
@RequiredArgsConstructor
public class HotelSearchJpaDataAccessImpl implements HotelSearchDataAccess {

    private final HotelJpaRepository repository;
    private final HotelMapper hotelMapper;

    @Override
    public List<Hotel> search(HotelSearchFilter filter) {
        Specification<HotelEntity> spec = buildSpec(filter);
        return repository.findAll(spec).stream()
                .map(hotelMapper::toDomain)
                .toList();
    }

    private Specification<HotelEntity> buildSpec(HotelSearchFilter f) {
        return Stream.of(
                        Optional.ofNullable(f.name()).map(HotelSpecifications::hasName),
                        Optional.ofNullable(f.brand()).map(HotelSpecifications::hasBrand),
                        Optional.ofNullable(f.city()).map(HotelSpecifications::hasCity),
                        Optional.ofNullable(f.country()).map(HotelSpecifications::hasCountry),
                        Optional.ofNullable(f.amenities()).map(HotelSpecifications::hasAllAmenities),
                        Optional.of(fetchAssociations())
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());
    }
}