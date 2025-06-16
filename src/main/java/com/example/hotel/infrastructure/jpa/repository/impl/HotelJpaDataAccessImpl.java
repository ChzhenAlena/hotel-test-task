package com.example.hotel.infrastructure.jpa.repository.impl;

import com.example.hotel.domain.model.Hotel;
import com.example.hotel.domain.repository.HotelDataAccess;
import com.example.hotel.infrastructure.jpa.entity.HotelEntity;
import com.example.hotel.infrastructure.jpa.repository.HotelJpaRepository;
import com.example.hotel.mapper.HotelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HotelJpaDataAccessImpl implements HotelDataAccess {

    private final HotelJpaRepository repository;
    private final HotelMapper hotelMapper;

    @Override
    public Optional<Hotel> findById(Long id) {
        return repository.findById(id)
                .map(hotelMapper::toDomain);
    }

    @Override
    public Hotel save(Hotel hotel) {
        HotelEntity saved = repository.save(hotelMapper.toEntity(hotel));
        return hotelMapper.toDomain(saved);
    }

    @Override
    public List<Hotel> findAll() {
        return repository.findAllHotelProjections().stream()
                .map(hotelMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
        ;
    }

    @Override
    public List<Hotel> saveAll(List<Hotel> hotels) {
        List<HotelEntity> hotelEntities = hotels.stream()
                .map(hotelMapper::toEntity)
                .toList();
        return repository.saveAll(hotelEntities).stream()
                .map(hotelMapper::toDomain)
                .toList();
    }
}