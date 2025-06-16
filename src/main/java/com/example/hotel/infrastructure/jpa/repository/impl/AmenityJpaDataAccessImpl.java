package com.example.hotel.infrastructure.jpa.repository.impl;

import com.example.hotel.domain.model.Amenity;
import com.example.hotel.domain.repository.AmenityDataAccess;
import com.example.hotel.infrastructure.jpa.entity.AmenityEntity;
import com.example.hotel.infrastructure.jpa.repository.AmenityJpaRepository;
import com.example.hotel.mapper.AmenityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AmenityJpaDataAccessImpl implements AmenityDataAccess {

    private final AmenityJpaRepository repository;
    private final AmenityMapper amenityMapper;

    @Override
    public List<Amenity> findByNameInIgnoreCase(List<String> names) {
        return repository.findByNameInIgnoreCase(names)
                .stream().map(amenityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Amenity> saveAll(List<Amenity> amenities) {
        List<AmenityEntity> entities = amenities.stream()
                .map(amenityMapper::toEntity)
                .toList();

        List<AmenityEntity> savedEntities = repository.saveAll(entities);

        return savedEntities.stream()
                .map(amenityMapper::toDomain)
                .toList();
    }

    @Override
    public Amenity save(Amenity amenity) {
        AmenityEntity saved = repository.save(amenityMapper.toEntity(amenity));
        return amenityMapper.toDomain(saved);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

}